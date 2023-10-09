/**
 * FormatHelper class helps format basic messages to reduce memory usage and length of code
 * 
 * Bugs: None
 * 
 * @author David Sanchez (@davchez on GitHub)
 */
public class FormatHelper {

    private final String LINE_SPACER = "\n///////////////////////////////////////////////////////////////////////////////////\n";

    /**
     * No-arg constructor class
     */
    public FormatHelper() {}

    /**
     * Getter method which returns a terminal-formatted spacer
     * @return String of forward slashes
     */
    public String getLineSpacer() {
        return this.LINE_SPACER;
    }

    /**
     * String formatter which returns account name and account creation date
     * @param user User account which information will be retrieved from
     */
    public void welcomeMessage( User user ) {
        System.out.println( String.format( "\nHello %s! Your account was created on %s.", user.getName(), user.getInformation().getTimeCreated() ) );
        menuInformation( user );
    }

    /**
     * String formatter which welcomes the user and notifys user of incomplete actions
     * @param user User account which information will be retrieved from
     */
    public void menuInformation( User user ) {
        System.out.println( String.format( "\nWelcome, %s.", user.getName() ) );
        System.out.println( user.getInformation().notificationNotice() );
    }

    /**
     * Basic message formatter for entire system
     * @param system String, name of the system that printed information
     * @param input String, printed information to be included
     * @return String containing necessary information by specific system
     */
    public String basicMessage( String system, String input ) {
        return String.format( "\n[%s] %s", system, input );
    }
}
