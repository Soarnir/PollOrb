package pollorb.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract representation of a slash command
 * implementing a slash command requires many design choices regarding user interaction
 * subcommands and pure top level options cannot exist at the same time,
 * and the nesting for subcommands is inherently strict:
 * <p>
 * command
 * <p>
 * --> subcommandGroup<p>
 *      --> subcommand<p>
 * --> subcommandGroup<p>
 *      --> subcommand<p>
 * is for example the maximum nesting allowed.
 *
 * @author Soarnir
 * @since 0.1.0
 */
public abstract class AbstractSlashCommand extends AbstractCommand {

    // A slash command may only have one of the following lists, either it can contain subcommands
    // or options related to the top level command
    protected List<OptionData> slashCommandOptionList = new ArrayList<>();
    protected final List<SubcommandData> subcommandDataList = new ArrayList<>();
    private String response = "Baseline response to slash command";
    protected String helpMessage = "";
    private boolean guildOnly = true;
    private boolean nsfw = false;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSlashCommand.class);


    /**
     * Create basic slash command without additional parameters in the constructor
     *
     * @param name lowercase single word command
     * @param description description
     * @param requirements List of contextual requirements
     */
    protected AbstractSlashCommand(String name, String description, List<ContextualRequirements> requirements) {
        super(name, description, requirements);
    }

    /**
     * Create a slash command with basic text response
     *
     * @param name lowercase single word command
     * @param description description
     * @param response Simple text response
     * @param slashCommandOptionList List of slash command options
     * @param requirements List of contextual requirements
     */
    protected AbstractSlashCommand(String name, String description, String response, List<OptionData> slashCommandOptionList, List<ContextualRequirements> requirements) {
        super(name, description, requirements);
        this.response = response;
        this.slashCommandOptionList = slashCommandOptionList;
    }

    public String getDescription() {
        return description;
    }

    public List<OptionData> getSlashCommandOptionList() {
        return slashCommandOptionList;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public boolean isNSFW() {
        return nsfw;
    }

    /**
     * Build the help embeds for all slash commands
     */
    public void buildHelp() {
        StringBuilder stringBuilder = new StringBuilder();
        // Does the slash command contain options, subcommands, or neither?
        // Each option requires a different structure for help clarity
        if (!slashCommandOptionList.isEmpty()) {
            stringBuilder.append("/").append(name);
            slashCommandOptionList.forEach(optionData -> {
                if (optionData.isRequired()) {
                    stringBuilder.append(" `<").append(optionData.getName()).append(">`");
                } else {
                    stringBuilder.append(" `[").append(optionData.getName()).append("]`");
                }
            });
        } else if (!subcommandDataList.isEmpty()) {
            subcommandDataList.forEach(subcommandData -> {
                stringBuilder.append("\n").append(name).append(" ").append(subcommandData.getName());
                if (!subcommandData.getOptions().isEmpty()) {
                    subcommandData.getOptions().forEach(optionData -> {
                        if (optionData.isRequired()) {
                            stringBuilder.append(" `<").append(optionData.getName()).append(">`");
                        } else {
                            stringBuilder.append(" `[").append(optionData.getName()).append("]`");
                        }
                    });
                }
            });
        } else {
            stringBuilder.append("/").append(name);
        }
        if (examples != null) {
            stringBuilder.append("\n**Examples**\n").append(examples);
        }

        this.helpMessageEmbed =
            new EmbedBuilder()
                .addField("Command usage", stringBuilder.toString(), false)
                .addField("Description", (description + "\n" + helpMessage), false)
                .setFooter("<required> | [optional]")
                .setColor(new Color(79, 165, 22))
                .setTitle(name)
                .build();
    }

    public void errorEmbed(SlashCommandInteractionEvent event, String errorResponse) {
        MessageEmbed messageEmbed = new EmbedBuilder()
            .addField("Error", errorResponse, false)
            .setColor(new Color(140, 0, 0))
            .build();
        event.replyEmbeds(messageEmbed).setEphemeral(true).queue();
    }

    public void errorEmbed(InteractionHook hook, String errorResponse) {
        MessageEmbed messageEmbed = new EmbedBuilder()
            .addField("Error", errorResponse, false)
            .setColor(new Color(140, 0, 0))
            .build();
        hook.sendMessageEmbeds(messageEmbed).queue();
    }

    /**
     * What the command should do, this should almost always be overridden by the implementing command.
     *
     * @param event invoked command
     */
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        event.reply(response).queue();
    }


    /**
     * What, if any, buttons should do, this should almost always be overridden by the implementing command.
     *
     * @param event invoked command
     */
    public void handleButtonInteraction(ButtonInteractionEvent event) {
        event.reply(event.getButton().getLabel()).queue();
    }

    public List<SubcommandData> getSubcommandDataList() {
        return subcommandDataList;
    }
}
