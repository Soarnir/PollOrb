package pollorb.commands.polls.types;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class QuestionPoll extends BasicPoll {
    public QuestionPoll(String pollName, String pollDescription, long channelID, boolean allowMultipleVotes, PollState pollState) {
        super(pollName, pollDescription, channelID, allowMultipleVotes, pollState);
    }
}
