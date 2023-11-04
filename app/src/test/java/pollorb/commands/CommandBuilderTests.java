package pollorb.commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class CommandBuilderTests {

    private static final Logger logger = LoggerFactory.getLogger(CommandBuilderTests.class);

    static Set<Class<? extends AbstractCommand>> commandClasses;
    static Map<String, AbstractCommand> commandMap;

    @BeforeAll
    public static void preWork() {
        // Initialize command registrar
        CommandRegistrar.initialize();

        // Get all command classes
        commandClasses = new Reflections("pollorb.commands")
            .getSubTypesOf(AbstractCommand.class);

        logger.info("Testing building commands");

        // Filter out all abstract classes
        commandClasses.removeIf(aClass -> Modifier.isAbstract(aClass.getModifiers()));

        // Get CommandRegistrars built commands
        commandMap = CommandRegistrar.getFullCommandMap();

    }

    @Test
    public void testCommandBuild() {
        logger.info("BUILT | TOTAL");
        logger.info(commandMap.size() + "     | " + commandClasses.size());
        assert (commandClasses.size() == commandMap.size());
    }


}
