package pollorb.commands.listeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandRegistrar;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.HashMap;
import java.util.Map;

public class SlashCommandListener {

    private static final Logger logger = Loggers.getLogger(CommandListener.class);
    private static Map<String, AbstractSlashCommand> slashCommandMap = new HashMap<>();

    static {
        SlashCommandListener.slashCommandMap = CommandRegistrar.getSlashCommandMap();
    }

    public static Mono<Void> handle(ChatInputInteractionEvent event) {

        return Flux.fromIterable(slashCommandMap.values())
            .filter(command -> command.getName().equalsIgnoreCase(event.getCommandName()))
            .next()
            .flatMap(command -> command.handle(event));
    }

    public static void init() {
        logger.info("Slash Command Listener initialized");
    }
}
