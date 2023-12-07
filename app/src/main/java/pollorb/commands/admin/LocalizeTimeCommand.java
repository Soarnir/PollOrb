package pollorb.commands.admin;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;

import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class LocalizeTimeCommand extends AbstractSlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(LocalizeTimeCommand.class);
    private final String formatError = "Please follow the format: `YYYY-MM-DDThh:mm:ss` \nThis is the baseline but you do not need to enter any time values, just to the accuracy you need.";

    public LocalizeTimeCommand() {
        super("localize", "Create automatically localized dates and times, defaults to UTC", CommandLevel.EVERYONE, List.of(ContextualRequirements.ROLE));
        this.slashCommandOptionList = List.of(
            new OptionData(OptionType.STRING, "datetime", "ISO8601 formatted time, for example:\n2023-01-01T20:20:20Z", true),
            new OptionData(OptionType.STRING, "timezone", "what your timezone is, defaults to UTC", false),
            new OptionData(OptionType.BOOLEAN, "raw", "Get unformatted response", false)
        );
        this.helpMessage = "Use ISO8601 format, eg. `2023-07-23T22:45`";
        this.examples = """
            `/localize datetime 2023-09-23`
            `/localize datetime 2023-09-23T22`
            `/localize datetime 2023-09-23T14:20`
            `/localize datetime 2023-09-23T09:50:36`
            """;
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        // Check optional timezone input
        String timezoneStringInput = event.getOption("timezone", OptionMapping::getAsString);
        ZoneId timezoneInput = ZoneId.of(Objects.requireNonNullElse(timezoneStringInput, "UTC"));

        // Create a baseline ZonedDateTime
        ZonedDateTime baseline = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime inputDateTime;

        // Custom regex patterns
        Pattern dateTimePattern = Pattern.compile(
            "(\\d{4}-\\d{1,2}-\\d{1,2})?T?(\\d{1,2})?:?(\\d{1,2})?:?(\\d{1,2})?"
        );
        Pattern iso8601 = Pattern.compile(
            "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:Z|[+-][01]\\d:[0-5]\\d)$"
        );

        String dateTime = event.getOption("datetime", OptionMapping::getAsString);

        try {
            logger.debug(dateTime);
            assert dateTime != null;
            Matcher dateTimeMatcher = dateTimePattern.matcher(dateTime);
            LocalTime inputTime;
            LocalDate inputDate;
            // Try to match user input, if any match is found then begin parsing
            if (dateTimeMatcher.find()) {
                // We always expect at least a full date, any less, and we send an error response
                if (dateTimeMatcher.group(1) != null) {
                    String[] dateArray = dateTimeMatcher.group(1).split("-");
                    inputDate = LocalDate.of(
                        Integer.parseInt(dateArray[0]),
                        Integer.parseInt(dateArray[1]),
                        Integer.parseInt(dateArray[2])
                    );
                } else {
                    errorEmbed(event, formatError);
                    return;
                }

                // Perform group checks in backward fashion to reduce total number of checks to minimum
                // This way we can accept any amount of time information and provide default (0) values
                // As well as provide the current time should no information get entered
                if (dateTimeMatcher.group(4) != null) {
                    inputTime = LocalTime.of(
                        Integer.parseInt(dateTimeMatcher.group(2)),
                        Integer.parseInt(dateTimeMatcher.group(3)),
                        Integer.parseInt(dateTimeMatcher.group(4))
                    );
                } else if (dateTimeMatcher.group(3) != null) {
                    inputTime = LocalTime.of(
                        Integer.parseInt(dateTimeMatcher.group(2)),
                        Integer.parseInt(dateTimeMatcher.group(3)),
                        0
                    );
                } else if (dateTimeMatcher.group(2) != null) {
                    inputTime = LocalTime.of(
                        Integer.parseInt(dateTimeMatcher.group(2)),
                        0,
                        0
                    );
                } else {
                    inputTime = LocalTime.of(
                        baseline.getHour(),
                        baseline.getMinute(),
                        baseline.getSecond()
                    );
                }

            } else {
                errorEmbed(event, formatError);
                return;
            }

            inputDateTime = ZonedDateTime.of(inputDate, inputTime, timezoneInput);

            logger.debug(inputDateTime.toString());
            logger.debug(String.valueOf(inputDateTime.toEpochSecond()));

            String response = "<t:" + inputDateTime.toEpochSecond() + ">";

            if (event.getOption("raw") != null) {
                if (Objects.requireNonNull(event.getOption("raw")).getAsBoolean()) {
                    response = "\\<t:" + inputDateTime.toEpochSecond() + ">";
                }
            }

            event.getHook().sendMessage(response).queue();
        } catch (IllegalStateException | DateTimeException e) {
            errorEmbed(event, e.getMessage());
        }
    }
}
