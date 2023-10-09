package pollorb.commands.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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
     *
     * @param event message command event
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromGuild()) {
            Message message = event.getMessage();
            if (message.getContentDisplay().startsWith(prefix)) {
                logger.debug("Entering matching");
                Pattern commandPattern = Pattern.compile(prefix + "(\\w+)");
                logger.debug("Pattern: " + commandPattern.pattern());
                String command = message.getContentDisplay();
                logger.debug("matching against: " + command);
                Matcher commandMatcher = commandPattern.matcher(command);
                if (commandMatcher.find()) {
                    logger.debug("found something");
                    commandMap.get(commandMatcher.group(1)).handle(event);
                } else {
                    logger.debug("found nothing");
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
        logger.debug("received slash event");
        if (event.isFromGuild()) {
            logger.debug("sending event to command handle");
            slashCommandMap.get(event.getName()).handle(event);
        }
    }

    /**
     * Initializes this class
     */
    public static void init() {
        logger.info("Command Listener initialized");
    }

}
