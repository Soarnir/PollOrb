package pollorb.commands;

import discord4j.core.object.entity.Role;

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
