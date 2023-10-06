package pollorb.commands.admin;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandParameters;
import pollorb.commands.ContextualRequirements;
import reactor.core.publisher.Mono;

import java.util.List;

public class FunCommand extends AbstractSlashCommand {
    public FunCommand() {
        super("fun", List.of(CommandParameters.STRING), List.of(ContextualRequirements.ROLE));
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply().withEphemeral(true).withContent("LASER");
    }
}
