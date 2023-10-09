package pollorb.commands.admin;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.ContextualRequirements;

import java.util.List;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class FunCommand extends AbstractSlashCommand {

    public FunCommand() {

        super("fun", "very fun indeed", List.of(
            new OptionData(STRING, "target", "pointer", true)
        ), List.of(ContextualRequirements.ROLE));
    }

}
