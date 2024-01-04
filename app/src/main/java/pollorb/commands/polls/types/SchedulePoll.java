package pollorb.commands.polls.types;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class SchedulePoll extends BasicPoll {
    public SchedulePoll(String pollName, String pollDescription, long channelID, boolean allowMultipleVotes, PollState pollState) {
        super(pollName, pollDescription, channelID, allowMultipleVotes, pollState);
    }
}
