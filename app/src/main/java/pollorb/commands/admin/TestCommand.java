package pollorb.commands.admin;

import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;

import java.util.List;

public class TestCommand extends AbstractCommand {
    public TestCommand() {
        super("test", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE));
    }
}
