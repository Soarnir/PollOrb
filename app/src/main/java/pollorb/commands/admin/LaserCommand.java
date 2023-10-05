package pollorb.commands.admin;

import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;

import java.util.List;

public class LaserCommand extends AbstractCommand {

    public LaserCommand() {
        super("laser", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE));
    }
}
