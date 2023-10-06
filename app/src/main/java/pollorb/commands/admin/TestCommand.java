package pollorb.commands.admin;

import discord4j.core.event.domain.message.MessageCreateEvent;
import pollorb.commands.AbstractCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Testing purposes
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class TestCommand extends AbstractCommand {
    public TestCommand() {
        super("test", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE));
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {
        long guildId = event.getGuildId().get().asLong();
        return event.getMessage().getChannel()
            .flatMap(messageChannel -> messageChannel.createMessage("ID: " + guildId)).then();
    }
}
