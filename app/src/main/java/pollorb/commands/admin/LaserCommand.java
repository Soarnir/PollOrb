package pollorb.commands.admin;

import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;

import java.util.List;

/**
 * Lasers!
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class LaserCommand extends AbstractCommand {

    public LaserCommand() {
        super("laser", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE), "LASER!");
    }
}
