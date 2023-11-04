package pollorb.commands;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.nio.channels.Channel;

/**
 * Enum representation of command requirements
 *
 * @author Soarnir
 * @since 0.1.0
 */
public enum ContextualRequirements {
    USER(User.class),
    ROLE(Role.class),
    PERMISSIONS(Permission.class),
    CHANNEL(Channel.class),
    GUILD(Guild.class),
    COMMANDLEVEL(CommandLevel.class);
    
    private final Class<?> clazz;

    ContextualRequirements(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }
}
