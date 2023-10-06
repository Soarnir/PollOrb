package pollorb.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class AbstractSlashCommand extends AbstractCommand {

    private final String description = "";

    protected AbstractSlashCommand(String name, List<CommandParameters> parameters, List<ContextualRequirements> requirements) {
        super(name, parameters, requirements);
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply().withEphemeral(true).withContent("Baseline response");
    }

    public String getDescription() {
        return description;
    }
}