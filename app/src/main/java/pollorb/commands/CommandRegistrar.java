package pollorb.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Reflectively analyzes all command files and generates command maps
 * Filters commands into text or slash command maps respectively
 * Manages updating registered slash commands with Discord
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class CommandRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(CommandRegistrar.class);
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

    public static void registerSlashCommands(JDA jda, long devGuild) {

        CommandListUpdateAction slashCommandList = Objects.requireNonNull(jda.getGuildById(devGuild)).updateCommands();

        slashCommands.values().forEach(slashCommand -> {
            logger.debug("Building slash command: " + slashCommand.getName());
            SlashCommandData slashCommandData = Commands.slash(slashCommand.getName(), slashCommand.getDescription());

            slashCommandData.addOptions(slashCommand.getSlashCommandParameters());

            slashCommandList.addCommands(slashCommandData);
        });

        slashCommandList.queue();


    }

    public static Map<String, AbstractCommand> getCommandMap() {
        return commands;
    }

    public static Map<String, AbstractSlashCommand> getSlashCommandMap() {
        return slashCommands;
    }
}
