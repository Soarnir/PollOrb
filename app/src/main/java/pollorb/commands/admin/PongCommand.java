package pollorb.commands.admin;

import pollorb.commands.AbstractCommand;
import pollorb.commands.ContextualRequirements;

import java.util.List;

/**
 * Pong!
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class PongCommand extends AbstractCommand {

    public PongCommand() {
        super("pong", List.of(ContextualRequirements.ROLE), "ping!");
    }
}
