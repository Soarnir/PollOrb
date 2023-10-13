package pollorb.commands.admin;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.ContextualRequirements;

import java.util.List;

public class FunCommand extends AbstractSlashCommand {

    public FunCommand() {
        super("fun", "very fun indeed", List.of(ContextualRequirements.ROLE));
        List<OptionData> optionDataSet = List.of(
            new OptionData(OptionType.USER, "user", "the user to mention", true),
            new OptionData(OptionType.STRING, "text", "what to say to them", true)
        );
        this.slashCommandOptionList.addAll(optionDataSet);
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        User target = event.getOption("user", OptionMapping::getAsUser);
        String message = event.getOption("text", OptionMapping::getAsString);
        assert target != null;
        event.reply(target.getAsMention() + " " + message).queue();
    }
}
