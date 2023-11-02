package pollorb.launcher;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.CommandRegistrar;
import pollorb.commands.listeners.CommandListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

/**
 * Main class from which bot will launch
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

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


        EnumSet<GatewayIntent> intents = EnumSet.of(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_MESSAGE_REACTIONS
        );

        try {
//            logger.debug("command test");
//            PollCommand command = new PollCommand();
//            logger.debug(command.toString());
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }


        // Create JDA object and register listeners
        logger.info("Initializing connection");
        try {
            JDA jda = JDABuilder.createLight(config.token, intents)
                .addEventListeners(new CommandListener())
                .build();

            jda.awaitReady();

            CommandRegistrar.registerSlashCommands(jda, config.dev_guild);

            jda.getRestPing().queue(ping -> logger.info("Logged in with ping: " + ping));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}