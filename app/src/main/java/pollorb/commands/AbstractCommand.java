package pollorb.commands;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
    private List<ContextualRequirements> requirements;
    private String response = "Baseline response to text command";

    /**
     * Baseline constructor without basic response
     *
     * @param name Command name
     * @param requirements Contextual requirements
     */
    protected AbstractCommand(String name, List<ContextualRequirements> requirements) {
        this.name = name;
        this.requirements = requirements;
    }

    /**
     * Baseline constructor with basic response override
     *
     * @param name Command name
     * @param requirements Contextual requirements
     * @param response Basic text response
     */
    protected AbstractCommand(String name, List<ContextualRequirements> requirements, String response) {
        this.name = name;
        this.requirements = requirements;
        this.response = response;
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<ContextualRequirements> getRequirements() {
        return requirements;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRequirements(List<ContextualRequirements> requirements) {
        this.requirements = requirements;
    }

    public void handle(MessageReceivedEvent event) {
        event.getChannel().sendMessage(response).queue();
    }
}
