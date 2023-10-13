package pollorb.commands.polls;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class PollWizard {

    static {
        // Grab db data probably
    }

    private static final int CANCEL_STAGE = 0;
    private static final int NAME_STAGE = 1;
    private static final int DESC_STAGE = 2;
    private static final int ROLE_STAGE = 3;
    private static final int END_STAGE = 4;

    private static boolean roleManagement = false;
    private static boolean multipleVotes = false;
    private static Role cancelRole = null;

    public static void initiatePollWizard(SlashCommandInteractionEvent event, EmbedBuilder embedBuilder) {
        embedBuilder.addField("Poll Wizard", "Welcome to the domain of the Poll Wizard!" +
            "\nFollow its instructions carefully to create your one-of-a-kind poll.", true);
        event.replyEmbeds(embedBuilder.build())
            .addActionRow(
                Button.primary("poll.continue.1", "Continue"),
                Button.danger("poll.cancel", "Cancel")
            )
            .queue();
    }

    public static void continuePollWizard(ButtonInteractionEvent event, EmbedBuilder embedBuilder, int wizardStage) {
        switch (wizardStage) {
            case NAME_STAGE:
                embedBuilder.addField("Poll Wizard", "Wise of you to continue on this path", true);
                event.editMessageEmbeds(embedBuilder.build())
                    .setActionRow(
                        Button.primary("poll.continue.2", "Continue"),
                        Button.danger("poll.cancel", "Cancel"))
                    .queue();
                return;
            case DESC_STAGE:
                embedBuilder.addField("Poll Wizard", "Wize of you to continue on this path", true);

                StringBuilder optionsBuilder = new StringBuilder();


                embedBuilder.addField("Current Options","", true);
                event.editMessageEmbeds(embedBuilder.build()).queue();
                return;
            case ROLE_STAGE:

                return;
            case END_STAGE:

        }
    }

    public static void cancelPollWizard(ButtonInteractionEvent event) {
        event.getMessage().delete().queue();
    }
}
