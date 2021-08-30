package Server;

/**
 * Definição das mensagens enviadas durante a comunicação com o cliente.
 */
public class PROTOCOL_MESSAGES {
    public static final String TOWER_OF_HANOI = "*********************Tower of Hanoi*********************";
    public static final String NUMBER_OF_RODS = "Number of Rods:";
    public static final String OPTIMAL_NUMBER_OF_MOVEMENTS = "->The optimal number of movements for %d rods is %d<-\n";
    public static final String ASTERISKS = "********************************************************";
    public static final String NUMBER_OF_MOVEMENTS = "Number of movements=%d\n";
    public static final String QUESTION_ABORT_GAME = "Abort?([Y] for yes, any key to continue)";
    public static final String CONGRATULATIONS = "Congratulations!!!Done in %d steps.\n";
    public static final String MINIMAL_NUMBER_STEPS = "You did it in the minimal number of steps that was %d.\n";
    public static final String INPUT_RODE_TREATMENT = "INVALID INPUT!!! TYPE A NUMBER BETWEEN 3 AND 10 INCLUSIVE.";
    public static final String CHOSE_A_POSSIBLE_MOVEMENT = "Choose Possible Movement:";
    public static final String CHOICES = "1:A->B\t\t2:A->C\t\t3:B->A\t\t4:B->C\t\t5:C->A\t\t6:C->B\t\tOther:Exit";
    public static final String INVALID_CHOICE = "INVALID INPUT! TYPE A NUMBER BETWEEN 1 and 6";
    public static final String BASE_SPACE = " ";
    public static final String INVALID_MOVEMENT = "INVALID MOVEMENT";
    public static final String PIPE = "|";
    public static final String ASTERISK = "*";
    public static final String HASH = "#";
    public static final String ORIGIN_PIN = "Please insert the origin PIN:";
    public static final String TARGET_PIN = "Please insert the target PIN:";
    public static final String INVALID_PIN = "%s is not a valid pin.";
    public static final String EQUAL_PINS = "Target pin can't be the same as origin pin.";
    public static final String SELECT_OPTION = "------Select an option------";
    public static final String PLAY_AGAIN = "1 - Play again.";
    public static final String SHOW_STATISTICS = "2 - Show statistics.";
    public static final String QUIT = "Q - Stop.";
    public static final String STATS = "For %d rods you have in average %d moves and you played %d times.";
    public static final String END_MESSAGE = "END";
}
