package pollorb.launcher;


import com.google.gson.Gson;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.reactivestreams.Publisher;
import pollorb.commands.CommandRegistrar;
import pollorb.commands.listeners.CommandListener;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main class from which bot will launch
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class Launcher {

    private static final Logger logger = Loggers.getLogger(Launcher.class);

    public static void main(String[] args) {
        // Initialize config with GSON
        Config config;
        Gson gson = new Gson();
        Path path = Path.of("ignored/config.json");
        try {
            config = gson.fromJson(Files.readAllLines(path).get(0), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Begin bot initializing
        // Be wary of order
        logger.info("Initializing Bot");
        CommandRegistrar.init();
        CommandListener.init();

        // Create DiscordClient and register listeners
        logger.info("Initializing connection");
        DiscordClientBuilder.create(config.token).build()
            .withGateway(client -> {
                Publisher<?> onSlashCommand = client.on(ChatInputInteractionEvent.class, CommandListener::handle);

                Publisher<?> onMessageCommand = client.on(MessageCreateEvent.class, CommandListener::handle);

                return CommandRegistrar.registerSlashCommands(client.getRestClient(), config.dev_guild)
                    .then(Mono.when(onSlashCommand, onMessageCommand));
            })
            .block();
    }
}