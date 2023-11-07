/*
 * This file is generated by jOOQ.
 */
package pollorb.database;


import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import pollorb.database.tables.CommandStats;
import pollorb.database.tables.Guilds;
import pollorb.database.tables.records.CommandStatsRecord;
import pollorb.database.tables.records.GuildsRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CommandStatsRecord> COMMAND_STATS_PKEY = Internal.createUniqueKey(CommandStats.COMMAND_STATS, DSL.name("command_stats_pkey"), new TableField[] { CommandStats.COMMAND_STATS.COMMAND_NAME }, true);
    public static final UniqueKey<GuildsRecord> GUILDS_PKEY = Internal.createUniqueKey(Guilds.GUILDS, DSL.name("guilds_pkey"), new TableField[] { Guilds.GUILDS.GUILD_ID }, true);
}
