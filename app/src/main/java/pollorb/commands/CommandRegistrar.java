package pollorb.commands;

import java.lang.reflect.Modifier;
import java.util.*;

import org.reflections.Reflections;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * Reflectively analyzes all command files and generates command maps
 * Filters commands into text or slash command maps respectively
 * Manages updating registered slash commands with Discord
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class CommandRegistrar {

    private static final Logger logger = Loggers.getLogger(CommandRegistrar.class);
    private static final Map<String, AbstractCommand> commands = new HashMap<>();
    private static final Map<String, AbstractSlashCommand> slashCommands = new HashMap<>();

    static {
        // Get all command classes
        Set<Class<? extends AbstractCommand>> commandClasses = new Reflections("pollorb.commands")
            .getSubTypesOf(AbstractCommand.class);

        // Filter out all abstract classes
        commandClasses.removeIf(aClass -> Modifier.isAbstract(aClass.getModifiers()));
        logger.info("Iterating through commands");

        // Iterate over each commandClass and place it in its appropriate command map
        commandClasses.forEach(commandClass -> {
            AbstractCommand command = null;
            try {
                logger.info("Initializing command: " + commandClass.getName());
                command = commandClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            if (command != null) {
                if (command instanceof AbstractSlashCommand) {
                    logger.info("Slash command: " + command.getName() + " added to slash command list");
                    slashCommands.put(command.getName(), (AbstractSlashCommand) command);
                } else {
                    logger.info("Text command: " + command.getName() + " added to command list");
                    commands.put(command.getName(), command);
                }
            }
        });
    }

    /**
     * Initialize the command handler
     */
    public static void init() {
        logger.info("Command Handler initialized");
    }

    private static void registerSlashCommands() {
        slashCommands.values().forEach(slashCommand -> {
            logger.info("info");
        });
    }

    public static Map<String, AbstractCommand> getCommandMap() {
        return commands;
    }
}
