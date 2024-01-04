package pollorb.commands.polls;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public enum PollType {

    // Quick poll with simple count functionality
    BASIC(),
    // Pose a question with multiple answers
    QUESTION(),
    // Pose a question with a predetermined answer, multiple options available
    QUIZ(),
    // Find the best time out of several options to schedule events
    SCHEDULER(),
    // Get roles by clicking buttons, can be mutually exclusive (i.e. selecting one role prevents more roles)
    ROLE_GETTER(),

}
