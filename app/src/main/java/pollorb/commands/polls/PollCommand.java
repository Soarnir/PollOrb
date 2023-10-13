package pollorb.commands.polls;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.ContextualRequirements;

import java.util.List;
import java.util.Objects;

public class PollCommand extends AbstractSlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(PollCommand.class);

    public PollCommand() {
        super("poll", "polls polls polls", List.of(ContextualRequirements.ROLE));

        List<SubcommandData> subcommandDataList = List.of(
            new SubcommandData("save", "Save a created poll to reuse later")
                .addOption(OptionType.STRING, "id", "Poll ID", true),
            new SubcommandData("create", "Start the Poll Wizard")
                .addOption(OptionType.STRING, "poll", "enter text formatted poll, skip wizard", false),
            new SubcommandData("update", "Update a saved poll")
                .addOption(OptionType.STRING, "id", "Poll ID", true),
            new SubcommandData("delete", "Delete a saved poll")
                .addOption(OptionType.STRING, "id", "Poll ID", true)
        );
        this.subcommandDataList.addAll(subcommandDataList);
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        switch (Objects.requireNonNull(event.getSubcommandName())) {
            case "create":
                if (event.getOption("poll") == null) {
                    // Init wizard
                    PollWizard.initiatePollWizard(event, embedBuilder);
                } else {
                    event.reply("Poll probably created, maybe").queue();
                }
                return;
            case "save":
                // Save by ID
                event.reply("save!").queue();

                return;
            case "update":
                // Update by ID
                event.reply("updoot!").queue();

                return;
            case "delete":
                // Delete by ID
                event.reply("delete!").queue();

        }
    }

    @Override
    public void handleButtonInteraction(ButtonInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        switch (Objects.requireNonNull(event.getButton().getId())) {
            case "poll.continue.1":
                PollWizard.continuePollWizard(event, embedBuilder, 1);
                return;
            case "poll.continue.2":
                PollWizard.continuePollWizard(event, embedBuilder, 2);
                return;
            case "poll.continue.3":
                PollWizard.continuePollWizard(event, embedBuilder, 3);
                return;
            case "poll.continue.4":
                PollWizard.continuePollWizard(event, embedBuilder, 4);
                return;
            case "poll.cancel":
                PollWizard.cancelPollWizard(event);
        }
    }
}