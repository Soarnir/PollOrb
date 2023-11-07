/*
 * This file is generated by jOOQ.
 */
package pollorb.database.tables.records;


import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import pollorb.database.tables.Guilds;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GuildsRecord extends UpdatableRecordImpl<GuildsRecord> implements Record2<Long, JSON> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.guilds.guild_id</code>.
     */
    public GuildsRecord setGuildId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.guild_id</code>.
     */
    public Long getGuildId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.guilds.config</code>.
     */
    public GuildsRecord setConfig(JSON value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.config</code>.
     */
    public JSON getConfig() {
        return (JSON) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, JSON> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, JSON> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Guilds.GUILDS.GUILD_ID;
    }

    @Override
    public Field<JSON> field2() {
        return Guilds.GUILDS.CONFIG;
    }

    @Override
    public Long component1() {
        return getGuildId();
    }

    @Override
    public JSON component2() {
        return getConfig();
    }

    @Override
    public Long value1() {
        return getGuildId();
    }

    @Override
    public JSON value2() {
        return getConfig();
    }

    @Override
    public GuildsRecord value1(Long value) {
        setGuildId(value);
        return this;
    }

    @Override
    public GuildsRecord value2(JSON value) {
        setConfig(value);
        return this;
    }

    @Override
    public GuildsRecord values(Long value1, JSON value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GuildsRecord
     */
    public GuildsRecord() {
        super(Guilds.GUILDS);
    }

    /**
     * Create a detached, initialised GuildsRecord
     */
    public GuildsRecord(Long guildId, JSON config) {
        super(Guilds.GUILDS);

        setGuildId(guildId);
        setConfig(config);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised GuildsRecord
     */
    public GuildsRecord(pollorb.database.tables.pojos.Guilds value) {
        super(Guilds.GUILDS);

        if (value != null) {
            setGuildId(value.getGuildId());
            setConfig(value.getConfig());
            resetChangedOnNotNull();
        }
    }
}