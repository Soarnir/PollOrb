package pollorb.commands.admin;

import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;

import java.util.List;

/**
 * Testing purposes
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class TestCommand extends AbstractCommand {

    public TestCommand() {
        super("test", "test", CommandLevel.DEVELOPMENT, List.of(ContextualRequirements.ROLE));
    }

}
