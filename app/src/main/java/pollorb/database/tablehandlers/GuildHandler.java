package pollorb.database.tablehandlers;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.database.DatabaseManager;
import pollorb.database.Tables;
import pollorb.database.tables.Guilds;

import static org.jooq.impl.DSL.field;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class GuildHandler {

    private static final Logger logger = LoggerFactory.getLogger(GuildHandler.class);

    static DSLContext dslContext = new DefaultDSLContext(SQLDialect.POSTGRES);

    /**
     * Get something from the table
     */
    public static void get() {

    }

    /**
     * Get message command prefix from a guild.
     *
     * @return empty string if nothing found, else prefix
     */
    public static String getPrefix(long guildID) {
        String selectSql = dslContext.select(field(Guilds.GUILDS.PREFIX))
            .from(Tables.GUILDS)
            .where(field(Guilds.GUILDS.GUILD_ID).eq(guildID))
            .getSQL(ParamType.INLINED);

        return DatabaseManager.select(selectSql, resultSet -> {
            String result;
            if (resultSet.next()) {
                result = resultSet.getString(1);
            } else {
                logger.debug("No record found in db.");
                result = "";
            }
            return result;
        });
    }

    /**
     * Insert a new record into the table.
     */
    public static void insert() {

    }

    /**
     * Update an existing record.
     */
    public static void update() {

    }

    /**
     * Delete any record in the table.
     */
    public static void delete() {

    }
}