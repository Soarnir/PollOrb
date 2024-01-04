package pollorb.commands.polls.types;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class RoleGetterPoll extends BasicPoll {
    public RoleGetterPoll(String pollName, String pollDescription, long channelID, boolean allowMultipleVotes, PollState pollState) {
        super(pollName, pollDescription, channelID, allowMultipleVotes, pollState);
    }
}
