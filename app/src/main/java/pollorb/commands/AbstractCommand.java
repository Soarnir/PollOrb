package pollorb.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Main abstract class from which all commands originate.
 * Provides default functionality that each command overrides individually.
 *
 * @author Soarnir
 * @since 0.1.0
 */
public abstract class AbstractCommand {

    protected String examples = "example";
    // Declare package-private variables
    protected String name;
    protected String description;
    protected List<ContextualRequirements> requirements;
    protected String response = "Baseline response to text command";
    protected MessageEmbed helpMessageEmbed;

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommand.class);

    /**
     * Baseline constructor without basic response
     *
     * @param name Command name
     * @param requirements Contextual requirements
     */
    protected AbstractCommand(String name, String description, List<ContextualRequirements> requirements) {
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.helpMessageEmbed =
            new EmbedBuilder()
                .addField("Command", name, false)
                .addField("Description", description, false)
                .addField("Examples", examples,false)
                .build();
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

    public MessageEmbed getHelpMessageEmbed() {
        return helpMessageEmbed;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRequirements(List<ContextualRequirements> requirements) {
        this.requirements = requirements;
    }

    public void setHelpMessageEmbed(MessageEmbed helpMessageEmbed) {
        this.helpMessageEmbed = helpMessageEmbed;
    }

    public void handle(MessageReceivedEvent event) {
        event.getChannel().sendMessage(response).queue();
    }
}
