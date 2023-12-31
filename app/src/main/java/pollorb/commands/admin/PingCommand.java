package pollorb.commands.admin;

import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandLevel;
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
        super("ping", "ping the bot", CommandLevel.ADMINISTRATIVE, List.of(ContextualRequirements.ROLE), "pong!");
    }

}
