package pollorb.launcher;


import com.google.gson.Gson;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main class from which bot will launch
 *
 * @since 0.1.0
 */
public class Launcher {

    public static void main(String[] args) {
        Config config;
        Gson gson = new Gson();
        Path path = Path.of("ignored/config.json");
        try {
            config = gson.fromJson(Files.readAllLines(path).get(0), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DiscordClient.create(config.token)
            .withGateway(gatewayDiscordClient ->
                gatewayDiscordClient.on(MessageCreateEvent.class, messageCreateEvent -> {
                    Message message = messageCreateEvent.getMessage();

                    if (message.getContent().equalsIgnoreCase("!ping")) {
                        return message.getChannel().flatMap(messageChannel -> messageChannel.createMessage("Pong!"));
                    }

                    return Mono.empty();
            }))
            .block();
    }
}