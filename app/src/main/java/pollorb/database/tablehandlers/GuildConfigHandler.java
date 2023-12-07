package pollorb.database.tablehandlers;

import com.google.gson.Gson;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.database.DatabaseManager;
import pollorb.database.Tables;
import pollorb.database.configs.GuildConfig;
import pollorb.database.tables.Guilds;

import java.util.HashMap;

import static org.jooq.impl.DSL.field;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class GuildConfigHandler {

    private static final Logger logger = LoggerFactory.getLogger(GuildConfigHandler.class);
    static DSLContext dslContext = new DefaultDSLContext(SQLDialect.POSTGRES);
    private static final Gson gson = new Gson();
    private static final HashMap<Long, GuildConfig> configCache = new HashMap<>();

    /**
     * Get something from the table
     */
    public static GuildConfig getGuildConfig(long guildID) {
        if (configCache.containsKey(guildID)) {
            return configCache.get(guildID);
        }

        String selectSql = dslContext.select(field(Guilds.GUILDS.CONFIG))
            .from(Tables.GUILDS)
            .where(field(Guilds.GUILDS.GUILD_ID).eq(guildID))
            .getSQL(ParamType.INLINED);

        return DatabaseManager.select(selectSql, resultSet -> {
            GuildConfig config;
            if (resultSet.next()) {
                config = gson.fromJson(resultSet.getString(1), GuildConfig.class);
            } else {
                logger.debug("No record found in db.");
                config = new GuildConfig();
                insert(guildID, config);
            }
            configCache.put(guildID, config);
            return config;
        });
    }

    /**
     * Insert a new record into the table.
     */
    public static void insert(long guildID, GuildConfig config) {
        String insertSql = dslContext.insertInto(Tables.GUILDS,
            field(Guilds.GUILDS.GUILD_ID),
            field(Guilds.GUILDS.CONFIG))
            .values(guildID, JSON.valueOf(gson.toJson(config, GuildConfig.class)))
            .getSQL(ParamType.INLINED);

        configCache.put(guildID, config);
        DatabaseManager.query(insertSql);
    }

    /**
     * Update an existing record.
     */
    public static void update(long guildID, GuildConfig config) {
        String updateSql = dslContext.update(Tables.GUILDS)
            .set(Guilds.GUILDS.CONFIG, JSON.valueOf(gson.toJson(config, GuildConfig.class)))
            .where(field(Guilds.GUILDS.GUILD_ID).eq(guildID))
            .getSQL(ParamType.INLINED);

        configCache.replace(guildID, config);
        DatabaseManager.query(updateSql);
    }

    /**
     * Delete any record in the table.
     */
    public static void delete(long guildID) {
        String deleteSql = dslContext.delete(Tables.GUILDS)
            .where(field(Guilds.GUILDS.GUILD_ID).eq(guildID))
            .getSQL(ParamType.INLINED);

        DatabaseManager.query(deleteSql);
    }
}