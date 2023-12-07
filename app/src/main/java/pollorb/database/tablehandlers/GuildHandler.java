package pollorb.database.tablehandlers;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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