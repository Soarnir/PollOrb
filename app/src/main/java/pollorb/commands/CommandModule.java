package pollorb.commands;

/**
 * Enums used to indicate systems that can be independently toggled on or off.
 *
 * @author Soarnir
 * @since 0.1.0
 */
public enum CommandModule {
    SYSTEM("Internal bot commands regulating bot function", "You aren't the bot!"),
    DEVELOPMENT("Any command exclusive to bot dev", "You aren't the dev!"),
    ADMINISTRATIVE("Any command requiring administrative guild access to invoke", "You do not have administrative permissions!"),
    FUN("", ""),
    POLLS("", ""),
    MUSIC("", ""),
    EVERYONE("Any command usable by anyone", "This shouldn't happen");

    private final String description;
    private final String error;

    CommandModule (String description, String error) {
        this.description = description;
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public String getError() {
        return error;
    }
}
