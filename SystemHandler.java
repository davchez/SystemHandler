import java.util.Scanner;

/**
 * Main class of the SystemHandler program package.  Handles user terminal input
 * and prints messages that assists the user.
 * 
 * Bugs: Cannot quit log-in or account creation prompt, notification notice
 * prints one too many times
 * 
 * @author David Sanchez (@davchez on GitHub)
 */
public class SystemHandler {

    /**
     * Main method for the SystemHandler package.  Prints information for user
     * and handles all capabilities of the SystemHandler object.
     * @param args
     */
    public static void main(String[] args) {

        // Will help format terminal messages
        FormatHelper formatHelper = new FormatHelper();

        final String STARTUP_MESSAGE = 
            formatHelper.basicMessage( "SYSTEM", 
                "Welcome, this is a new System Handler. Enter your new credentials.") ;
        final String LOG_OUT_MESSAGE = 
            formatHelper.basicMessage( "SYSTEM", 
                "Logged out. Welcome to the System Handler. Would you like to LOG IN or CREATE ACCOUNT?" );

        UserList userList = new UserList();
        User currUser = new User();
        System.out.println( STARTUP_MESSAGE );

        // Takes user input for username and password creation
        String userName = userList.promptInput( "username" );
        String userPassword = userList.promptInput( "password" );

        // If account creation is successful (i.e. no blank values) then the user will be retrieved
        if ( userList.accountCreation( userName, userPassword ) ) currUser = userList.retrieveUser( userName );
        formatHelper.welcomeMessage( currUser );

        // User is added to the "userList" object which keeps track of all recorded users
        userList.addUser( currUser );

        // Prints information about user and creates while loop that returns information unless "quit"
        currUser.promptUser();

        // Once logged out of first created user
        while ( true ) {
            System.out.println( LOG_OUT_MESSAGE );
            Scanner input = new Scanner( System.in );
            String response;

            // Handling user input in terminal
            response = input.nextLine().toUpperCase();

            // Handles a user log in
            if ( response.equals( "LOG IN" ) ) {
                // Prompts the user for log-in information
                currUser = userList.logInPrompt();
                // If user is not found/wrong password, returns null and prompts log in again
                if ( currUser != null ) {
                    // Formats account information for the logged-in user
                    formatHelper.menuInformation( currUser );
                    // Records the time the user logged in
                    currUser.getInformation().setBeginningTime();
                    // Prompts user for input
                    currUser.promptUser();
                }
            }
            // Hnadles if the user wants to create an account
            else if ( response.equals ( "CREATE ACCOUNT" ) ) {
                while (true) {
                    userName = userList.promptInput( "username" );
                    userPassword = userList.promptInput( "password" );
                    // If account creation was successful, then account was added to user list
                    if ( userList.accountCreation( userName, userPassword ) ) {
                        currUser = userList.retrieveUser( userName );
                        formatHelper.welcomeMessage( currUser );
                        break;
                    }
                }
                // Prompts user once while loop is broken
                currUser.promptUser();
            }
            else {
                System.out.println( formatHelper.basicMessage( "ERROR", "Incorrect input." ) );
            }
        }
    }
}
