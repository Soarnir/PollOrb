package pollorb.commands.listeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import pollorb.commands.AbstractCommand;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandRegistrar;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.HashMap;
import java.util.Map;

/**
 * Static class responsible for deferring listening events to
 * each respective command depending on origin.
 * Provides default fallback in case command does not override response.
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class CommandListener {

    private static final Logger logger = Loggers.getLogger(CommandListener.class);
    private static Map<String, AbstractCommand> commandMap = new HashMap<>();
    private static Map<String, AbstractSlashCommand> slashCommandMap = new HashMap<>();

    static {
        CommandListener.commandMap = CommandRegistrar.getCommandMap();
        CommandListener.slashCommandMap = CommandRegistrar.getSlashCommandMap();
    }

    /**
     * Handles slash commands
     *
     * @param event Slash command event
     * @return response
     */
    public static Mono<Void> handle(ChatInputInteractionEvent event) {

        return Flux.fromIterable(slashCommandMap.values())
            .filter(command -> command.getName().equalsIgnoreCase(event.getCommandName()))
            .next()
            .flatMap(command -> command.handle(event));
    }

    /**
     * Handles message commands
     *
     * @param event message command event
     * @return response
     */
    public static Mono<Void> handle(MessageCreateEvent event) {
        String prefix = "!";
        return Flux.fromIterable(commandMap.values())
            .filter(command -> (prefix + command.getName()).equalsIgnoreCase(event.getMessage().getContent()))
            .next()
            .flatMap(command -> command.handle(event));
    }

    /**
     * Initializes this class
     */
    public static void init() {
        logger.info("Command Listener initialized");
    }

}
