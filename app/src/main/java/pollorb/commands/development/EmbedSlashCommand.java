package pollorb.commands.development;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class EmbedSlashCommand extends AbstractSlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(EmbedSlashCommand.class);

    public EmbedSlashCommand() {
        super("embed", "embedtester", CommandLevel.DEVELOPMENT, List.of(ContextualRequirements.ROLE));
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Poll")
            .setDescription("This is an example poll")
            .setTimestamp(ZonedDateTime.now());

        event.replyEmbeds(embedBuilder.build())
            .setActionRow(
                Button.of(ButtonStyle.PRIMARY, "embed.1", "1"),
                Button.of(ButtonStyle.PRIMARY, "embed.2", "2")
            )
            .queue();
    }

    @Override
    public void handleButtonInteraction(ButtonInteractionEvent event) {
        super.handleButtonInteraction(event);
    }
}
