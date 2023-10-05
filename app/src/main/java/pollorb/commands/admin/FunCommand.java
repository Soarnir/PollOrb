package pollorb.commands.admin;

import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;

import java.util.List;

public class FunCommand extends AbstractSlashCommand {
    public FunCommand() {
        super("laser", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE));
    }
}
