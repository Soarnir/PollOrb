package pollorb.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.*;

import java.util.List;
import java.util.Objects;

public class HelpCommand extends AbstractSlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand() {
        super("help", "Get help on parts of the bot", CommandLevel.EVERYONE, List.of(ContextualRequirements.ROLE));
        List<SubcommandData> subcommandDataList = List.of(
            new SubcommandData("command", "get help with a command")
                .addOption(OptionType.STRING, "name", "command name", true),
            new SubcommandData("module", "Get help with a module")
                .addOption(OptionType.STRING, "name", "module name", true),
            new SubcommandData("general", "Open general help menu")
        );
        this.subcommandDataList.addAll(subcommandDataList);
        this.setHelpMessageEmbed(
            new EmbedBuilder()
                .addField("Command usage","Help", false)
                .addField("Description", "Well this is redundant", false)
                .build()
        );
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        String input;
        switch (Objects.requireNonNull(event.getSubcommandName())) {
            case "command":
                input = Objects.requireNonNull(event.getOption("name")).getAsString();
                logger.debug(input);
                AbstractCommand command = CommandRegistrar.getFullCommandMap().get(input);
                if (command != null) {
                    event.replyEmbeds(command.getHelpMessageEmbed()).queue();
                } else {
                    errorEmbed(event, "Could not find a command with that name");
                }
                return;
            case "module":
                input = Objects.requireNonNull(event.getOption("name")).getAsString();
                errorEmbed(event, "modules aren't a thing yet!");
                return;
            case "general":
                errorEmbed(event, "general help isn't a thing yet!");
                return;
            default:

        }
    }
}
