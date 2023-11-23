package pollorb.commands.listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractCommand;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandRegistrar;
import pollorb.database.tablehandlers.CommandStatsHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static class responsible for deferring listening events to
 * each respective command depending on origin.
 * Works as the baseline permission matrix checker for executions
 * Provides default fallback in case command does not override response.
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class CommandListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CommandListener.class);
    private static Map<String, AbstractCommand> commandMap = new HashMap<>();
    private static Map<String, AbstractSlashCommand> slashCommandMap = new HashMap<>();
    private static String prefix = "!";

    static {
        CommandListener.commandMap = CommandRegistrar.getCommandMap();
        CommandListener.slashCommandMap = CommandRegistrar.getSlashCommandMap();
    }

    /**
     * Initializes this class
     */
    public static void initialize() {
        logger.info("Command Listener initialized");
    }

    /**
     * Handles message commands
     * Designed to stop processing in multiple steps in order to avoid attempting command invocation
     * if message provided in event is a simple message.
     *
     * @param event message command event
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromGuild()) {
            Message message = event.getMessage();
            String messageText = message.getContentDisplay();
            // Check if message contains prefix
            if (messageText.startsWith(prefix)) {
                try {
                    // Check if valid command format, if so store name
                    String commandName = commandNameExtract(messageText);
                    // Check if command exists
                    if (commandMap.get(commandName) != null) {
                        AbstractCommand command = commandMap.get(commandName);
                        // Check if user permissions level matches command level
                        if (checkCommandLevel(command, event)) {
                            // Handle command
                            updateCommandStats(command.getName(), event.getGuild().getIdLong(), event.getAuthor().getIdLong());
                            command.handle(event);
                        } else {
                            AbstractCommand.errorEmbed(event, command.getCommandLevel().getError());
                        }
                    } else {
                        AbstractCommand.errorEmbed(event, "No command found with that name");
                    }
                } catch (IllegalStateException r) {
                    AbstractCommand.errorEmbed(event, "No command found with that name");
                }
            }
        }
    }

    /**
     * Handles slash commands
     *
     * @param event Slash command event
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        logger.debug("Received slash event");
        // Check if command origin is in a guild
        if (event.isFromGuild()) {
            logger.debug("sending event to command handle");
            AbstractSlashCommand slashCommand = slashCommandMap.get(event.getName());
            // Check if user permissions level matches command level
            if (checkCommandLevel(slashCommand, event)) {
                // Handle command
                updateCommandStats(slashCommand.getName(), event.getGuild().getIdLong(), event.getUser().getIdLong());
                slashCommand.handleSlashCommand(event);
            } else {
                AbstractSlashCommand.errorEmbed(event, slashCommand.getCommandLevel().getError());
            }
        } else {
            AbstractSlashCommand.errorEmbed(event,"Only server commands for now.");
        }
    }

    /**
     * For text commands, extract command name from prefixed text
     *
     * @param command text containing command
     * @return String matching only command name
     */
    public String commandNameExtract(String command) {
        String commandName = "";
        logger.debug("Entering matching");
        Pattern commandPattern = Pattern.compile((prefix + "(\\w+)"), Pattern.CASE_INSENSITIVE);
        logger.debug("Pattern: " + commandPattern.pattern());
        logger.debug("matching against: " + command);
        Matcher commandMatcher = commandPattern.matcher(command);
        if (commandMatcher.find()) {
            commandName = commandMatcher.group(1);
        }
        logger.debug("Command: " + commandName);
        return commandName;
    }

    /**
     * Check if the invoker matches the appropriate permissions for the command.
     *
     * @param slashCommand command used
     * @param event slash command event
     * @return whether permissions match or not
     */
    public boolean checkCommandLevel(AbstractSlashCommand slashCommand, SlashCommandInteractionEvent event) {
        return switch (slashCommand.getCommandLevel()) {
            case SYSTEM -> event.getUser().getName().equalsIgnoreCase("pollorb");
            case DEVELOPMENT -> event.getUser().getName().equalsIgnoreCase("soarnir");
            case ADMINISTRATIVE -> event.getMember().hasPermission(Permission.ADMINISTRATOR);
            case EVERYONE -> true;
        };
    }

    /**
     * Check if the invoker matches the appropriate permissions for the command.
     *
     * @param command command used
     * @param event message received event
     * @return whether permissions match or not
     */
    public boolean checkCommandLevel(AbstractCommand command, MessageReceivedEvent event) {
        return switch (command.getCommandLevel()) {
            case SYSTEM -> event.getAuthor().getName().equalsIgnoreCase("pollorb");
            case DEVELOPMENT -> event.getAuthor().getName().equalsIgnoreCase("soarnir");
            case ADMINISTRATIVE -> event.getMember().hasPermission(Permission.ADMINISTRATOR);
            case EVERYONE -> true;
        };
    }

    /**
     * Increment command executions and update last guild which used command as well as time.
     *
     * @param commandName String command name
     * @param guildID long guild id
     */
    public void updateCommandStats(String commandName, long guildID, long userID) {
        int executions = CommandStatsHandler.getExecutions(commandName);

        if (executions > 0) {
            logger.debug("Record found for command, updating");
            logger.debug("Executions: " + executions);
            CommandStatsHandler.update(commandName, guildID, userID, ++executions);
        } else {
            logger.debug("No record found for command, inserting");
            CommandStatsHandler.insert(commandName, guildID, userID);
        }
    }

    /**
     * Handle button interaction events
     * IMPORTANT NAMING CONVENTIONS
     * Each button id must be <command name>.<button id>
     *
     * @param event Button interaction event
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        logger.debug("Received button event");
        if (event.isFromGuild()) {
            logger.debug("Sending button event to responsible handler");
            slashCommandMap.get(event.getComponentId().split("\\.")[0]).handleButtonInteraction(event);
        }
    }
}
