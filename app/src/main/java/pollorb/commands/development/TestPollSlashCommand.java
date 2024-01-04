package pollorb.commands.development;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;
import pollorb.commands.polls.PollHandler;
import pollorb.commands.polls.PollOption;
import pollorb.commands.polls.types.QuestionPoll;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Testing purposes
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class TestPollSlashCommand extends AbstractSlashCommand {

    public TestPollSlashCommand() {
        super("testpoll", "test poll", CommandLevel.DEVELOPMENT, List.of(ContextualRequirements.ROLE));
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        QuestionPoll questionPoll =
            PollHandler.createQuestionPoll("Test Poll", "Test Poll", event.getChannelIdLong(), true,
                new PollOption(0, "Yes"),
                new PollOption(1, "No"));

        embedBuilder.setTitle("Poll")
            .setDescription("This is an example poll")
            .setFooter(questionPoll.getPollID().toString())
            .setTimestamp(ZonedDateTime.now());

        List<Button> buttonCollection = new ArrayList<>();

        for (PollOption pollOption : questionPoll.getPollOptionList()) {
            buttonCollection.add(Button.of(ButtonStyle.PRIMARY, questionPoll.getPollID() + "." + pollOption.getOptionID(), pollOption.getOptionName()));
        }

        event.replyEmbeds(embedBuilder.build())
            .setActionRow(buttonCollection)
            .queue();
    }
}
