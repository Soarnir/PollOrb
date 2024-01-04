package pollorb.commands.polls;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.polls.types.BasicPoll;
import pollorb.commands.polls.types.BasicPollInterface;
import pollorb.commands.polls.types.PollState;
import pollorb.commands.polls.types.QuestionPoll;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Class responsible for keeping track of all active polls, as well as registering new ones
 * and receiving selection events
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class PollHandler {

    private static final Logger logger = LoggerFactory.getLogger(PollHandler.class);
    private static final HashMap<UUID, BasicPoll> activePolls;
    private static final HashMap<UUID, BasicPollInterface> activeIPolls;

    static {
        activePolls = new HashMap<>();
        activeIPolls = new HashMap<>();
    }

    public static void initialize() {
        logger.info("Poll Handler initialized");
    }

    public static HashMap<UUID, BasicPoll> getActivePolls() {
        return activePolls;
    }

    public static BasicPoll getActivePoll(UUID pollID) {
        return activePolls.get(pollID);
    }

    public static QuestionPoll createQuestionPoll(String name, String description, long channelID, boolean allowMultipleVotes, PollOption...pollOptions) {
        QuestionPoll questionPoll = new QuestionPoll(name, description, channelID, allowMultipleVotes, PollState.ACTIVE);
        questionPoll.setPollOptionList((Arrays.stream(pollOptions).collect(Collectors.toCollection(ArrayList::new))));

        activePolls.put(questionPoll.getPollID(), questionPoll);

        return questionPoll;
    }

    public static void voteForPoll(ButtonInteractionEvent event, UUID pollID, int option, User user) {
        if (getActivePoll(pollID).isAllowMultipleVotes()) {
            getActivePoll(pollID).getPollOption(option).addUserToOption(user);
            successEmbed(event.getHook(), "Your vote for " + getActivePoll(pollID).getPollOption(option).getOptionName() + " has been added.");
        } else {
            if (getActivePoll(pollID).hasUserVotedAnyOption(user)) {
                AbstractSlashCommand.errorEmbed(event.getHook(), "Vote could not be counted");
            } else {
                getActivePoll(pollID).getPollOption(option).addUserToOption(user);
                successEmbed(event.getHook(), "Your vote for " + getActivePoll(pollID).getPollOption(option).getOptionName() + " has been added.");
            }
        }
    }

    /**
     * Static method for sending a success embed to the user ephemerally
     *
     * @param hook
     * @param successResponse
     */
    public static void successEmbed(InteractionHook hook, String successResponse) {
        MessageEmbed messageEmbed = new EmbedBuilder()
            .addField("Vote Counted", successResponse, false)
            .setColor(new Color(79, 165, 22))
            .build();
        hook.sendMessageEmbeds(messageEmbed).setEphemeral(true).queue();
    }
}
