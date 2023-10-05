package pollorb.commands.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandRegistrar;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.HashMap;
import java.util.Map;


public class CommandListener {

    private static final Logger logger = Loggers.getLogger(CommandListener.class);
    private static Map<String, AbstractCommand> commandMap = new HashMap<>();

    static {
        CommandListener.commandMap = CommandRegistrar.getCommandMap();
    }

    /*
    public static Mono<Void> handle(ChatInputInteractionEvent event) {

        return Flux.fromIterable(commandMap.values())
            .filter(command -> command.getName().equalsIgnoreCase(event.getCommandName()))
            .next()
            .flatMap(command -> command.handle(event));
    }
    */

    public static Mono<Void> handle(MessageCreateEvent event) {

        return Flux.fromIterable(commandMap.values())
            .filter(command -> command.getName().equalsIgnoreCase(event.getMessage().getContent()))
            .next()
            .flatMap(command -> command.handle(event));
    }

    public static void init() {
        logger.info("Command Listener initialized");
    }

}
