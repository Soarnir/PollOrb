package pollorb.commands.polls;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class PollOption {
    private int optionID;
    private String optionName;
    private int maximumVotes;
    private Role appliedRole;
    private HashMap<User, ZonedDateTime> userMap;

    public PollOption(int optionID, String optionName) {
        this.optionID = optionID;
        this.optionName = optionName;
        this.userMap = new HashMap<>();
    }

    public int getOptionID() {
        return optionID;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getMaximumVotes() {
        return maximumVotes;
    }

    public Role getAppliedRole() {
        return appliedRole;
    }

    public HashMap<User, ZonedDateTime> getUserMap() {
        return userMap;
    }

    public void addUserToOption(User user) {
        this.userMap.put(user, ZonedDateTime.now());
    }

    public int getTotalVotes() {
        return this.userMap.size();
    }
}
