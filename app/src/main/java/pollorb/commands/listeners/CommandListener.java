package pollorb.commands.listeners;

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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static class responsible for deferring listening events to
 * each respective command depending on origin.
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
            if (messageText.startsWith(prefix)) {
                // Check if valid command format
                if (commandCheck(messageText)) {

                } else {
                    message.getChannel().sendMessage("Not a valid command format").queue();
                }

                // Check permissions

            }
        }
    }

    public boolean commandCheck(String command) {
        logger.debug("Entering matching");
        Pattern commandPattern = Pattern.compile(prefix + "(\\w+)");
        logger.debug("Pattern: " + commandPattern.pattern());
        logger.debug("matching against: " + command);
        Matcher commandMatcher = commandPattern.matcher(command);
        return commandMatcher.find();
    }

    /**
     * Handles slash commands
     *
     * @param event Slash command event
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        logger.debug("Received slash event");
        if (event.isFromGuild()) {
            logger.debug("sending event to command handle");
            slashCommandMap.get(event.getName()).handleSlashCommand(event);
        } else {
            event.reply("Only server commands for now.").queue();
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

    /**
     * Initializes this class
     */
    public static void initialize() {
        logger.info("Command Listener initialized");
    }
}
