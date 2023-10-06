package pollorb.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Main abstract class from which all commands originate.
 * Provides default functionality that each command overrides individually.
 *
 * @author Soarnir
 * @since 0.1.0
 */
public abstract class AbstractCommand {
    private String name;
    private List<CommandParameters> parameters;
    private List<ContextualRequirements> requirements;
    private String response = "Baseline response to text command";

    /**
     * Baseline constructor without basic response
     *
     * @param name Command name
     * @param parameters List of potential command parameters, optional or not
     * @param requirements Contextual requirements
     */
    protected AbstractCommand(String name, List<CommandParameters> parameters, List<ContextualRequirements> requirements) {
        this.name = name;
        this.parameters = parameters;
        this.requirements = requirements;
    }

    /**
     * Baseline constructor with basic response override
     *
     * @param name Command name
     * @param parameters List of potential command parameters, optional or not
     * @param requirements Contextual requirements
     * @param response Basic text response
     */
    protected AbstractCommand(String name, List<CommandParameters> parameters, List<ContextualRequirements> requirements, String response) {
        this.name = name;
        this.parameters = parameters;
        this.requirements = requirements;
        this.response = response;
    }

    /**
     * Provides baseline text response handle
     *
     * @param event Any message event
     * @return response
     */
    public Mono<Void> handle(MessageCreateEvent event) {
        return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(response)).then();
    }

    // Getters

    public String getName() {
        return name;
    }

    public List<CommandParameters> getParameters() {
        return parameters;
    }

    public List<ContextualRequirements> getRequirements() {
        return requirements;
    }

    // Setters
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
