package pollorb.commands;


import net.dv8tion.jda.api.entities.Role;

/**
 * Enum representation of command requirements
 *
 * @author Soarnir
 * @since 0.1.0
 */
public enum ContextualRequirements {
    USER(),
    ROLE(Role.class),
    PERMISSIONS(),
    CHANNEL(),
    COMMANDLEVEL();
    
    private final Role role;
    
    

    ContextualRequirements(Class<Role> roleClass) {

        role = null;
    }

    ContextualRequirements() {

        role = null;
    }
}
