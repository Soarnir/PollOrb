/*
 * This file is generated by jOOQ.
 */
package pollorb.database.tables;


import java.time.OffsetDateTime;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import pollorb.database.Keys;
import pollorb.database.Public;
import pollorb.database.tables.records.CommandStatsRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CommandStats extends TableImpl<CommandStatsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.command_stats</code>
     */
    public static final CommandStats COMMAND_STATS = new CommandStats();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CommandStatsRecord> getRecordType() {
        return CommandStatsRecord.class;
    }

    /**
     * The column <code>public.command_stats.command_name</code>.
     */
    public final TableField<CommandStatsRecord, String> COMMAND_NAME = createField(DSL.name("command_name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.command_stats.executions</code>.
     */
    public final TableField<CommandStatsRecord, Integer> EXECUTIONS = createField(DSL.name("executions"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.command_stats.last_executed_guild</code>.
     */
    public final TableField<CommandStatsRecord, Long> LAST_EXECUTED_GUILD = createField(DSL.name("last_executed_guild"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.command_stats.last_executed_time</code>.
     */
    public final TableField<CommandStatsRecord, OffsetDateTime> LAST_EXECUTED_TIME = createField(DSL.name("last_executed_time"), SQLDataType.TIMESTAMPWITHTIMEZONE(3), this, "");

    private CommandStats(Name alias, Table<CommandStatsRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommandStats(Name alias, Table<CommandStatsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.command_stats</code> table reference
     */
    public CommandStats(String alias) {
        this(DSL.name(alias), COMMAND_STATS);
    }

    /**
     * Create an aliased <code>public.command_stats</code> table reference
     */
    public CommandStats(Name alias) {
        this(alias, COMMAND_STATS);
    }

    /**
     * Create a <code>public.command_stats</code> table reference
     */
    public CommandStats() {
        this(DSL.name("command_stats"), null);
    }

    public <O extends Record> CommandStats(Table<O> child, ForeignKey<O, CommandStatsRecord> key) {
        super(child, key, COMMAND_STATS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<CommandStatsRecord> getPrimaryKey() {
        return Keys.COMMAND_STATS_PKEY;
    }

    @Override
    public CommandStats as(String alias) {
        return new CommandStats(DSL.name(alias), this);
    }

    @Override
    public CommandStats as(Name alias) {
        return new CommandStats(alias, this);
    }

    @Override
    public CommandStats as(Table<?> alias) {
        return new CommandStats(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommandStats rename(String name) {
        return new CommandStats(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommandStats rename(Name name) {
        return new CommandStats(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommandStats rename(Table<?> name) {
        return new CommandStats(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, Integer, Long, OffsetDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super String, ? super Integer, ? super Long, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super String, ? super Integer, ? super Long, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}