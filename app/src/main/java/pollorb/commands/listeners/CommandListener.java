package pollorb.commands.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
import pollorb.commands.polls.PollHandler;
import pollorb.database.tablehandlers.CommandStatsHandler;
import pollorb.database.tablehandlers.GuildConfigHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    private static final Pattern UUID_REGEX;

    static {
        CommandListener.commandMap = CommandRegistrar.getCommandMap();
        CommandListener.slashCommandMap = CommandRegistrar.getSlashCommandMap();
        // Courtesy of Baeldung https://www.baeldung.com/java-validate-uuid-string
        UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
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
                            updateCommandStats(command.getName(), event.getGuild(), event.getAuthor());
                            sendLogMessage(command, event.getMessage().getContentDisplay(), event.getGuild(), event.getAuthor(), event.getChannel().asTextChannel());
                            command.handleCommand(event);
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
            AbstractSlashCommand slashCommand = slashCommandMap.get(event.getName());
            // Check if user permissions level matches command level
            if (checkCommandLevel(slashCommand, event)) {
                User user = event.getUser();
                Guild guild = event.getGuild();
                TextChannel textChannel = event.getChannel().asTextChannel();

                // Handle command
                logger.debug("sending event to command handle");
                assert guild != null;
                updateCommandStats(slashCommand.getName(), guild, user);
                sendLogMessage(slashCommand, event.getCommandString(), guild, user, textChannel);
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
     * @param slashCommand slash command used
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
     * @param guild Guild object
     */
    public void updateCommandStats(String commandName, Guild guild, User user) {
        int executions = CommandStatsHandler.getExecutions(commandName);
        long guildID = guild.getIdLong();
        long userID = user.getIdLong();

        if (executions > 0) {
            logger.debug("Record found for command, updating");
            logger.debug("Executions: " + executions);
            CommandStatsHandler.update(commandName, guildID, userID, ++executions);
        } else {
            logger.debug("No record found for command, inserting");
            CommandStatsHandler.insert(commandName, guildID, userID);
        }
    }

    public void sendLogMessage(AbstractCommand command, String fullCommand, Guild guild, User user, TextChannel textChannel) {
        TextChannel logChannel = guild.getChannelById(TextChannel.class, GuildConfigHandler.getGuildConfig(guild.getIdLong()).getLoggingChannel());

        // We perform operations inside here to prevent unnecessary embed building if no log channel is configured
        if (logChannel != null) {
            int commandIndex = command.getName().length() + (command.getName().contains(" ") ? 2 : 1);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Text command used")
                .setThumbnail("https://cdn.discordapp.com/attachments/1162040736804507738/1182149531249410168/text.png")
                .setDescription("`" + command.getName() + "`")
                .addField("By user", user.getAsMention(), true)
                .addField("In channel", textChannel.getAsMention(), true);
            if (command.getName().contains(" ")) {
                embedBuilder.addField("Arguments", fullCommand.substring(commandIndex), false);
            }

            logChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    public void sendLogMessage(AbstractSlashCommand command, String fullCommand, Guild guild, User user, TextChannel textChannel) {
        TextChannel logChannel = guild.getChannelById(TextChannel.class, GuildConfigHandler.getGuildConfig(guild.getIdLong()).getLoggingChannel());

        // We perform operations inside here to prevent unnecessary embed building if no log channel is configured
        if (logChannel != null) {
            int commandIndex = command.getName().length() + (fullCommand.contains(" ") ? 2 : 1);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Slash command used")
                .setThumbnail("https://cdn.discordapp.com/attachments/1162040736804507738/1182148898857418822/slash.png")
                .setDescription("`" + command.getName() + "`")
                .addField("By user", user.getAsMention(), true)
                .addField("In channel", textChannel.getAsMention(), true);
            if (command.getName().contains(" ")) {
                embedBuilder.addField("Arguments", fullCommand.substring(commandIndex), false);
            }

            logChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    public void sendLogMessage(Guild guild, String title, String text) {
        TextChannel logChannel = guild.getChannelById(TextChannel.class, GuildConfigHandler.getGuildConfig(guild.getIdLong()).getLoggingChannel());

        if (logChannel != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(title)
                .setDescription(text);

            logChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    /**
     * Handle button interaction events
     * <p>
     * IMPORTANT NAMING CONVENTION
     * Each button id must be <command name>.<button id>
     * For polls this is <poll id>.<poll option id>
     *
     * @param event Button interaction event
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        logger.debug("Received button event");
        if (event.isFromGuild()) {
            Matcher UUIDMatcher = UUID_REGEX.matcher(event.getComponentId().split("\\.")[0]);
            if (UUIDMatcher.matches()) {
                UUID pollID = UUID.fromString(event.getComponentId().split("\\.")[0]);
                int option = Integer.parseInt(event.getComponentId().split("\\.")[1]);
                event.deferReply(true).queue();
                PollHandler.voteForPoll(event, pollID, option, event.getUser());
                sendLogMessage(event.getGuild(), "Button pressed", "Option #" + option);
            } else {
                logger.debug("Sending button event to responsible handler");
                slashCommandMap.get(event.getComponentId().split("\\.")[0]).handleButtonInteraction(event);
            }
        }
    }
}
