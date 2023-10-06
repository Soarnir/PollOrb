package pollorb.commands.admin;

import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;

import java.util.List;

/**
 * Ping!
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class PingCommand extends AbstractCommand {

    public PingCommand() {
        super("ping", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE), "ping!");
    }

}