package pollorb.launcher;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.CommandRegistrar;
import pollorb.commands.listeners.CommandListener;
import pollorb.database.DatabaseManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
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
        try {
            logger.info("Reading config");
            File file = Path.of("ignored/config.json").toFile();
            FileReader reader = new FileReader(file);
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(true);
            config = gson.fromJson(jsonReader, Config.class);
            logger.info("Config read and initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Begin bot initializing
        // Be wary of order
        logger.info("Initializing Bot");
        CommandRegistrar.initialize();
        CommandListener.initialize();
        DatabaseManager.url = config.database_url;
        DatabaseManager.username = config.database_username;
        DatabaseManager.password = config.database_password;
        try {
            DatabaseManager.initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Create gateway intents to send to discord for proper access permissions
        EnumSet<GatewayIntent> intents = EnumSet.of(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_MESSAGE_REACTIONS
        );

        try {
//            logger.debug("command test");
//            PollCommand command = new PollCommand();
//            logger.debug(command.toString());
            logger.info("testing db");
            DatabaseManager.testConnection();
        } catch (Exception e) {
            logger.info(e.getMessage());
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