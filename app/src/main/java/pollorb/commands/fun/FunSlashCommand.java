package pollorb.commands.fun;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;

import java.util.List;

public class FunSlashCommand extends AbstractSlashCommand {
    private static final Logger logger = LoggerFactory.getLogger(FunSlashCommand.class);

    public FunSlashCommand() {
        super("fun", "very fun indeed", CommandLevel.EVERYONE, List.of(ContextualRequirements.ROLE));
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
