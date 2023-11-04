package pollorb.commands;

/**
 * Enum representation of internal bot command levels
 * Used to limit command presence
 *
 * @author Soarnir
 * @since 0.1.0
 */
public enum CommandLevel {
    SYSTEM("Internal bot commands regulating bot function", "You aren't the bot!"),
    DEVELOPMENT("Any command exclusive to bot dev", "You aren't the dev!"),
    ADMINISTRATIVE("Any command requiring administrative guild access to invoke", "You do not have administrative permissions!"),
    EVERYONE("Any command usable by anyone", "This shouldn't happen");

    private final String description;
    private final String error;

    CommandLevel (String description, String error) {
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
