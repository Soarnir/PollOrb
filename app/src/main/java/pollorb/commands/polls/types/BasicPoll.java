package pollorb.commands.polls.types;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.polls.PollOption;
import pollorb.commands.polls.PollType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Baseline abstract poll implementation.
 */
public abstract class BasicPoll {

    private String pollName;
    private String pollDescription;
    private boolean allowMultipleVotes;
    private UUID pollID;
    private long guildID;
    private long channelID;
    private long messageID;
    private PollState pollState;
    private PollType pollType;
    private ArrayList<PollOption> pollOptionList;

    private static final Logger logger = LoggerFactory.getLogger(BasicPoll.class);

    public BasicPoll(String pollName, String pollDescription, long channelID, boolean allowMultipleVotes, PollState pollState) {
        this.pollName = pollName;
        this.pollDescription = pollDescription;
        this.channelID = channelID;
        this.allowMultipleVotes = allowMultipleVotes;
        this.pollID = UUID.randomUUID();
        this.pollState = pollState;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public String getPollDescription() {
        return pollDescription;
    }

    public void setPollDescription(String pollDescription) {
        this.pollDescription = pollDescription;
    }

    public boolean isAllowMultipleVotes() {
        return allowMultipleVotes;
    }

    public void setAllowMultipleVotes(boolean allowMultipleVotes) {
        this.allowMultipleVotes = allowMultipleVotes;
    }

    public UUID getPollID() {
        return pollID;
    }

    public void setPollID(UUID pollID) {
        this.pollID = pollID;
    }

    public PollType getPollType() {
        return pollType;
    }

    public void setPollType(PollType pollType) {
        this.pollType = pollType;
    }

    public List<PollOption> getPollOptionList() {
        return pollOptionList;
    }

    public void setPollOptionList(ArrayList<PollOption> pollOptionList) {
        this.pollOptionList = pollOptionList;
    }

    public PollOption getPollOption(int index) {
        return this.pollOptionList.get(index);
    }

    public boolean hasUserVotedAnyOption(User user) {
        for (PollOption pollOption : this.pollOptionList) {
            if (pollOption.getUserMap().containsKey(user)) {
                return true;
            }
        }
        return false;
    }

    public String getFormattedVotes() {
        StringBuilder formattedVoteBuilder = new StringBuilder("```\n");
        StringBuilder stringFormatBuilder = new StringBuilder();
        int maxNameLength = 10;
        int maxVoteLength = 10;

        for (PollOption pollOption : this.pollOptionList) {
            if (pollOption.getOptionName().length() > maxNameLength) {
                maxNameLength = pollOption.getOptionName().length();
                logger.info("new name max: " + maxNameLength);
            }
            if ((String.valueOf(pollOption.getTotalVotes()).length() > maxVoteLength)) {
                maxVoteLength = String.valueOf(pollOption.getTotalVotes()).length();
                logger.info("new option max: " + maxVoteLength);
            }
        }

        String maxNameLengthString = String.valueOf((int) (5 * (Math.ceil(Math.abs(maxNameLength / 5.0)))));
        String maxVoteLengthString = String.valueOf((int) (5 * (Math.ceil(Math.abs(maxVoteLength / 5.0)))));

        stringFormatBuilder.append("%-").append(maxNameLengthString).append("s");
        stringFormatBuilder.append("%-").append(maxVoteLengthString).append("s");
        stringFormatBuilder.append("%n");

        String format = stringFormatBuilder.toString();

        formattedVoteBuilder.append(String.format(format, "Option", "| Votes"));

        logger.info("Before sort: ");
        this.pollOptionList.forEach((s) -> logger.info(String.valueOf(s.getTotalVotes())));
        this.pollOptionList.sort((p1, p2) -> p2.getTotalVotes() - p1.getTotalVotes());
        logger.info("After sort: ");
        this.pollOptionList.forEach((s) -> logger.info(String.valueOf(s.getTotalVotes())));

        for (int i = 0; i < this.pollOptionList.size(); i++) {
            PollOption pollOption = this.pollOptionList.get(i);
            if (i == 0) {
                formattedVoteBuilder.append(String.format(format, pollOption.getOptionName(), "| " + pollOption.getUserMap().size()));
            } else {
                formattedVoteBuilder.append(String.format(format, pollOption.getOptionName(), "| " + pollOption.getUserMap().size()));
            }
        }

        formattedVoteBuilder.append("```");
        return formattedVoteBuilder.toString();
    }

    public void endPoll(SlashCommandInteractionEvent event, Guild guild) {
        this.pollState = PollState.ENDED;
        EmbedBuilder embedBuilder = new EmbedBuilder();

        PollOption winningOption = new PollOption(999, "removed");
        for (PollOption pollOption : this.pollOptionList) {
            if (pollOption.getTotalVotes() > winningOption.getTotalVotes() || winningOption.getOptionID() == 999) {
                winningOption = pollOption;
            }
        }

        embedBuilder
            .setTitle("Poll ended")
            .addField("Top option", winningOption.getOptionName(), true)
            .addField("Votes", String.valueOf(winningOption.getTotalVotes()), true)
            .addField("Tabulated results", getFormattedVotes(), false)
            .setFooter(getPollID().toString());

        TextChannel channel = guild.getTextChannelById(this.channelID);
        if (channel != null) {
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        } else {
            AbstractSlashCommand.errorEmbed(event, "Could not post poll end in channel: " + channel.getName());
        }
    }
}
