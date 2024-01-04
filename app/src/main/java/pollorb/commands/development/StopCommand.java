package pollorb.commands.development;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;

import java.util.List;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class StopCommand extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(StopCommand.class);

    public StopCommand() {
        super("stop", "stop the bot lol", CommandLevel.DEVELOPMENT, List.of(ContextualRequirements.ROLE));
    }

    @Override
    public void handleCommand(MessageReceivedEvent event) {
        event.getGuildChannel().sendMessage("stopping, sad time").queue();
        System.exit(0);
    }
}
