package pollorb.database.configs;

import pollorb.commands.CommandModule;

import java.util.HashMap;

/**
 * Class representing a guild config object.
 *
 * @author Soarnir
 * @since 0.1.0
 */
public class GuildConfig {

    protected long guildBotManager = 0L;
    protected long loggingChannel = 0L;
    protected String prefix = "!";
    protected HashMap<CommandModule, Boolean> commandModuleBooleanHashMap = new HashMap<>();

    public GuildConfig() {

    }

    public long getGuildBotManager() {
        return guildBotManager;
    }

    public void setGuildBotManager(long guildBotManager) {
        this.guildBotManager = guildBotManager;
    }

    public long getLoggingChannel() {
        return loggingChannel;
    }

    public void setLoggingChannel(long loggingChannel) {
        this.loggingChannel = loggingChannel;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public HashMap<CommandModule, Boolean> getCommandModuleBooleanHashMap() {
        return commandModuleBooleanHashMap;
    }

    public void setCommandModuleBooleanHashMap(HashMap<CommandModule, Boolean> commandModuleBooleanHashMap) {
        this.commandModuleBooleanHashMap = commandModuleBooleanHashMap;
    }
}
