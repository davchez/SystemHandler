import java.util.*;

/**
 * User class which creates user objects for a SystemHandler object, so that accounts can be created and
 * recorded, logged into and logged out from, and edited based on user preference
 * 
 * Bugs: None
 * 
 * @author David Sanchez (@davchez on GitHub)
 */
public class User {

    private String name, password, timeCreated;
    private InformationHandler information;
    private FormatHelper formatHelper = new FormatHelper();

    // Unused variable, planned for future expansion of SystemHandler package
    private SystemTime clock = new SystemTime();

    /**
     * No-arg constructor method
     */
    public User() {
        this.name = null;
        this.password = null;
        this.information = new InformationHandler();
        getInformation().setTimeCreated();
    }
    
    /**
     * User constructor method which accepts two strings for name and password
     * when creating an account
     * @param name String, username
     * @param password String, password associated with account
     */
    public User( String name, String password ) {
        this.name = name.toUpperCase();
        this.password = password;
        this.information = new InformationHandler();
        getInformation().setTimeCreated();
    }

    /**
     * Getter method which returns the name of the user's account
     * @return String, returns username
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method which returns the user's password.  Will remove this method 
     * as it only heightens the potential for a "security breach"
     * @return String, user password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Getter method which returns the time of account creation
     * @return String, formatted and readable time of account creation
     */
    public String getTimeCreated() {
        return this.timeCreated;
    }

    /**
     * Getter method which returns the user's non-static information object
     * @return InformationHandler, specific user's information object
     */
    public InformationHandler getInformation() {
        return this.information;
    }

    /**
     * Extremely important User class method for processing terminal user input.
     * Handles execution of methods used to edit user profile
     * @param key String to be matched to a list of processor key Strings
     */
    public void executeProcessor( String key ) {
        getInformation().getProcessor().getExecutableProcessor().get( key ).run();
        System.out.println( getInformation().notificationNotice() );
    }

    /**
     * Sends user input to user's information object's processor so that the 
     * processor can match the inputted String to its own key values
     * to execute a Runnable method
     * @param string String to be compared to user's processor's key array
     */
    public void processUserInput( String string ) {
        for ( String key : getInformation().getProcessor().getKeys() ) {
            if ( key.equals( string ) ) { executeProcessor( key ); } }
    }

    /**
     * Private helper method which prompts the user for input in the terminal
     * @param input String, user input collected from terminal
     * @return Null if user quits system, otherwise returns the 
     * user String input in the terminal
     */
    private String promptUserInformation( Scanner input ) {
        // Upper case conversion to simplify the processing of user input
        String response = input.nextLine().toUpperCase();
        if ( response.equals( "QUIT" ) ) return null;
        return response;
    }

    /**
     * Reads all user input in the terminal.  All input will be sent through
     * the user's information object to be processed and this while loop
     * will not break until user quits
     */
    public void promptUser() {
        while (true) {
            String response = promptUserInformation( new Scanner( System.in ) );
            if ( response == null ) { break; }
            processUserInput( response ); 
        }
    }
}