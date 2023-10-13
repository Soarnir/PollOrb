package pollorb.commands.polls;

public class PollOption {
    private String optionName;
    private String optionID;

    public PollOption(String optionID, String optionName) {
        this.optionID = optionID;
        this.optionName = optionName;
    }
}
