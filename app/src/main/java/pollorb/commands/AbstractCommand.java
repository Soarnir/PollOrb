package pollorb.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
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
    protected List<ContextualRequirements> contextualRequirements;
    protected CommandLevel commandLevel;
    protected String response = "Baseline response to text command";
    protected MessageEmbed helpMessageEmbed;

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommand.class);

    /**
     * Baseline constructor without basic response
     *
     * @param name Command name
     * @param requirements Contextual requirements
     */
    protected AbstractCommand(String name, String description, CommandLevel commandLevel, List<ContextualRequirements> requirements) {
        this.name = name;
        this.description = description;
        this.commandLevel = commandLevel;
        this.contextualRequirements = requirements;
    }

    /**
     * Baseline constructor with basic response override
     *
     * @param name Command name
     * @param requirements Contextual requirements
     * @param response Basic text response
     */
    protected AbstractCommand(String name, String description, CommandLevel commandLevel, List<ContextualRequirements> requirements, String response) {
        this.name = name;
        this.description = description;
        this.commandLevel = commandLevel;
        this.contextualRequirements = requirements;
        this.response = response;
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<ContextualRequirements> getRequirements() {
        return contextualRequirements;
    }

    public MessageEmbed getHelpMessageEmbed() {
        return helpMessageEmbed;
    }

    public CommandLevel getCommandLevel() {
        return commandLevel;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRequirements(List<ContextualRequirements> requirements) {
        this.contextualRequirements = requirements;
    }

    public void buildHelp() {
        this.helpMessageEmbed =
            new EmbedBuilder()
                .addField("Command usage", "!" + name, false)
                .addField("Description", description, false)
                .addField("Examples", examples,false)
                .setFooter("<required> | [optional]")
                .setColor(new Color(79, 165, 22))
                .setTitle(name)
                .build();
    }

    public static void errorEmbed(MessageReceivedEvent event, String errorResponse) {
        MessageEmbed messageEmbed = new EmbedBuilder()
            .addField("Error", errorResponse, false)
            .setColor(new Color(140, 0, 0))
            .build();
        event.getChannel().sendMessageEmbeds(messageEmbed).queue();
    }

    public void setHelpMessageEmbed(MessageEmbed helpMessageEmbed) {
        this.helpMessageEmbed = helpMessageEmbed;
    }

    public void handle(MessageReceivedEvent event) {
        event.getChannel().sendMessage(response).queue();
    }
}
