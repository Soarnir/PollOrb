package pollorb.commands.development;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;
import pollorb.commands.polls.PollHandler;

import java.util.List;
import java.util.UUID;

/**
 * Testing purposes
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class EndPollSlashCommand extends AbstractSlashCommand {

    public EndPollSlashCommand() {
        super("endpoll", "end test poll", CommandLevel.DEVELOPMENT, List.of(ContextualRequirements.ROLE));
        this.slashCommandOptionList = List.of(
            new OptionData(OptionType.STRING, "id", "Poll ID", true)
        );
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        UUID pollID = UUID.fromString(event.getOption("id", OptionMapping::getAsString));

        PollHandler.getActivePoll(pollID).endPoll(event, event.getGuild());
        PollHandler.getActivePolls().remove(pollID);

        event.reply("Poll ended").setEphemeral(true).queue();
    }
}
