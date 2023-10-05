package pollorb.launcher;


import com.google.gson.Gson;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import pollorb.commands.CommandRegistrar;
import pollorb.commands.listeners.CommandListener;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main class from which bot will launch
 *
 * @since 0.1.0
 */
public class Launcher {

    private static final Logger logger = Loggers.getLogger(Launcher.class);

    /**
     * Main class for launching PollOrb
     */
    public static void main(String[] args) {
        Config config;
        Gson gson = new Gson();
        Path path = Path.of("ignored/config.json");
        try {
            config = gson.fromJson(Files.readAllLines(path).get(0), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Initializing Bot");
        CommandRegistrar.init();
        CommandListener.init();

        logger.info("Initializing connection");
        final GatewayDiscordClient client = DiscordClientBuilder.create(config.token)
            .build()
            .login()
            .block();

        assert client != null;
        client.on(MessageCreateEvent.class, CommandListener::handle)
            .then(client.onDisconnect())
            .block();
    }
}