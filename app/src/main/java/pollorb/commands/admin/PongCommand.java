package pollorb.commands.admin;

import discord4j.core.event.domain.message.MessageCreateEvent;
import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;
import reactor.core.publisher.Mono;

import java.util.List;

public class PongCommand extends AbstractCommand {

    public PongCommand() {
        super("pong", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE));
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {
        return event.getMessage().getChannel().flatMap(channel -> channel.createMessage("ping!")).then();
    }
}
