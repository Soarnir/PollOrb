package pollorb.database.tablehandlers;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.database.DatabaseManager;
import pollorb.database.Tables;
import pollorb.database.tables.CommandStats;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.jooq.impl.DSL.field;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class CommandStatsHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandStatsHandler.class);
    static DSLContext dslContext = new DefaultDSLContext(SQLDialect.POSTGRES);

    /**
     * Get executions for a command.
     *
     * @return 0 if nothing is found, else total executions
     */
    public static int getExecutions(String commandName) {
        String selectSql = dslContext.select(field(CommandStats.COMMAND_STATS.COMMAND_NAME), field(CommandStats.COMMAND_STATS.EXECUTIONS))
            .from(Tables.COMMAND_STATS)
            .where(field(CommandStats.COMMAND_STATS.COMMAND_NAME).eq(commandName))
            .getSQL(ParamType.INLINED);

        return DatabaseManager.select(selectSql, resultSet -> {
            int executions;
            if (resultSet.next()) {
                executions = resultSet.getInt(2);
            } else {
                logger.debug("No record found in db.");
                executions = 0;
            }
            return executions;
        });
    }

    /**
     * Insert a new record into the table.
     */
    public static void insert(String commandName, long guildID, long userID) {
        String insertSql = dslContext.insertInto(Tables.COMMAND_STATS,
                field(CommandStats.COMMAND_STATS.COMMAND_NAME),
                field(CommandStats.COMMAND_STATS.EXECUTIONS),
                field(CommandStats.COMMAND_STATS.LAST_EXECUTED_GUILD),
                field(CommandStats.COMMAND_STATS.LAST_EXECUTED_USER),
                field(CommandStats.COMMAND_STATS.LAST_EXECUTED_TIME))
            .values(commandName, 1, guildID, userID, ZonedDateTime.now().toOffsetDateTime())
            .getSQL(ParamType.INLINED);

        DatabaseManager.query(insertSql);
    }

    /**
     * Update an existing record.
     */
    public static void update(String commandName, long guildID, long userID, int executions) {
        String updateSql = dslContext.update(Tables.COMMAND_STATS)
            .set(CommandStats.COMMAND_STATS.EXECUTIONS, executions)
            .set(CommandStats.COMMAND_STATS.LAST_EXECUTED_TIME, ZonedDateTime.now(ZoneId.of("UTC")).toOffsetDateTime())
            .set(CommandStats.COMMAND_STATS.LAST_EXECUTED_GUILD, guildID)
            .set(CommandStats.COMMAND_STATS.LAST_EXECUTED_USER, userID)
            .where(field(CommandStats.COMMAND_STATS.COMMAND_NAME).eq(commandName))
            .getSQL(ParamType.INLINED);

        DatabaseManager.query(updateSql);
    }

    /**
     * Delete any record in the table.
     */
    public static void delete(String commandName) {
        String deleteSql = dslContext.delete(Tables.COMMAND_STATS)
            .where(CommandStats.COMMAND_STATS.COMMAND_NAME.eq(commandName))
            .getSQL();

        DatabaseManager.query(deleteSql);
    }
}