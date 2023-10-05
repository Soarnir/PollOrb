package pollorb.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class AbstractCommand {
    private String name;
    private List<CommandParameters> parameters;
    private List<ContextualRequirements> requirements;

    protected AbstractCommand(String name, List<CommandParameters> parameters, List<ContextualRequirements> requirements) {
        this.name = name;
        this.parameters = parameters;
        this.requirements = requirements;
    }

    public Mono<Void> handle(MessageCreateEvent event) {
        return null;
    }

    public String getName() {
        return name;
    }

    public List<CommandParameters> getParameters() {
        return parameters;
    }

    public List<ContextualRequirements> getRequirements() {
        return requirements;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(List<CommandParameters> parameters) {
        this.parameters = parameters;
    }

    public void setRequirements(List<ContextualRequirements> requirements) {
        this.requirements = requirements;
    }

}
