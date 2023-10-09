package pollorb.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public abstract class AbstractSlashCommand extends AbstractCommand {

    private String description = "";
    private final List<OptionData> slashCommandParameters;
    private String response = "Baseline response to slash command";

    protected AbstractSlashCommand(String name, String description, List<OptionData> slashCommandParameters, List<ContextualRequirements> requirements) {
        super(name, requirements);
        this.description = description;
        this.slashCommandParameters = slashCommandParameters;
    }

    public String getDescription() {
        return description;
    }

    public List<OptionData> getSlashCommandParameters() {
        return slashCommandParameters;
    }

    public void handle(SlashCommandInteractionEvent event) {
        event.reply(response).queue();
    }
}
