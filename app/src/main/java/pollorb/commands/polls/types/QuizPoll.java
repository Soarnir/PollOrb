package pollorb.commands.polls.types;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class QuizPoll extends BasicPoll {
    public QuizPoll(String pollName, String pollDescription, long channelID, boolean allowMultipleVotes, PollState pollState) {
        super(pollName, pollDescription, channelID, allowMultipleVotes, pollState);
    }
}
