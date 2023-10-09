import java.util.*;

/**
 * UserList class which records, stores, and instantiates user objects
 * 
 * Bugs: No bugs, but should streamline uppercasing input more effectively
 * 
 * @author David Sanchez (@davchez on GitHub)
 */
public class UserList {

    private Map< String, User > userList;
    private FormatHelper formatHelper = new FormatHelper();

    /**
     * No-arg constructor
     */
    public UserList() {
        // Names for users are kept in upper case
        userList = new HashMap< String, User >();
    }

    /**
     * Getter method that returns a User object if the input String matches
     * the name of a User stored in this UserList
     * @param name String to be matched to User names in UserList
     * @return User if input matches a username; otherwise null
     */
    public User retrieveUser( String name ) {
        if ( doesUserExist( name ) ) return userList.get( name.toUpperCase() );
        return null;
    }

    /**
     * Getter method that determines whether or not a User exists in this UserList
     * @param name is compared to list of User names in userList
     * @return True if User exists in UserList; false otherwise
     */
    public boolean doesUserExist( String name ) {
        if ( userList.containsKey( name.toUpperCase() ) ) return true;
        return false;
    }

    /**
     * Method that accepts user input in the terminal, has general usage throughout
     * SystemHandler package
     * @param message What the user is prompted to input (i.e. "username")
     * @param input Scanner object
     * @return String, the user's input in the terminal
     */
    private String enterInformation( String message, Scanner input ) {
        System.out.println( formatHelper.basicMessage( "SYSTEM", String.format( "Please enter your %s.", message.toLowerCase() ) ) );
        return input.nextLine();
    }

    /**
     * Priavte helper method which sends a key to the InformationHandler of the User
     * object to call upon the Runnable method that executes a security question
     * challenge
     * @param user Security questions will be fetched from this specific user's account
     * @return If user passed security challenge, returns User; otherwise, null
     */
    private User promptSecurityQuestion( User user ) {
        user.executeProcessor( "CHALLENGE SECURITY QUESTION" );
        if ( user.getInformation().getPassedSecurityQuestions() ) { return user; }
        return null;
    }
    
    /**
     * Private helper method which accepts an attempted log in by having username
     * and password as parameters, checks if user exists in this UserList and if
     * the password matches the account if it exists
     * @param name Username of desired User object
     * @param password Password of desired User object
     * @return Boolean, True if log-in is succesful, otherwise False
     */
    private boolean promptPassword( String name, String password ) {
        if ( doesUserExist( name.toUpperCase() ) ) { 
            return retrieveUser( name.toUpperCase() ).getPassword().equals( password ); }
        return false;
    }

    /**
     * Private helper method which is prompted when a user attempts to
     * log into a User account.  Formats messages so that the terminal can
     * notify the user if the log in was succesful or unsuccessful
     * @param name Username of desired User object
     * @param password Password of desired User object
     * @return User if login is successful, otherwise null
     */
    private User attemptLogIn( String name, String password ) {
        if ( promptPassword( name.toUpperCase(), password ) ) { 
            System.out.println( formatHelper.basicMessage( "SYSTEM", "Log in successful." ) );
            return retrieveUser( name.toUpperCase() ); }
        System.out.println( formatHelper.basicMessage( "SYSTEM", "Log in failed." ) );
        return null;
    }

    /**
     * Login primary method which handles all login attempts.  Collects user input
     * and asks for their username and password.  Checks if username exists in UserList
     * and if so, will check if their attempted password matches the User object's password
     * @return User account if login is successful, otherwise null
     */
    public User logInPrompt() {
        Scanner input = new Scanner( System.in );
        String username = enterInformation( "username", input ).toUpperCase(), password = enterInformation ( "password", input );
        User user = attemptLogIn( username.toUpperCase(), password );
        if ( doesUserExist( username.toUpperCase() ) && user == null ) { user = promptSecurityQuestion( retrieveUser( username.toUpperCase() ) ); }
        return user;
    }

    /**
     * Method that prompts the user for any basic information.  Collects user input through terminal
     * and is flexible to have wide utilization range through SystemHandler package for the UserList
     * @param parameter Desired object/input to be wanted by System
     * @return String, user terminal input
     */
    public String promptInput( String parameter ) {
        System.out.println( formatHelper.basicMessage( "SYSTEM", String.format("Please enter your new %s.", parameter ) ) );
        Scanner input = new Scanner( System.in );
        return input.nextLine();
    }

    /**
     * Private helper method that returns whether or not the input entered by a user in the
     * terminal is valid when attempting to create a new name and password.  Rejects null inputs
     * @param name Desired username for new User object
     * @param password Desired password for new User object
     * @return True if all inputs are non-null, otherwise false
     */
    private boolean validInput( String name, String password ) {
        if ( name == null || password == null ) return false;
        else return true;
    }

    /**
     * Method that adds a new User object to the UserList HashMap.  Will NOT be added
     * if User object already has been added or if desired new name and new password
     * are null
     * @param user Desired User to be added
     * @return True if adding User was successful, False if otherwise
     */
    public boolean addUser( User user ) {
        if ( doesUserExist( user.getName() ) ) { return false; }
        else if ( validInput( user.getName(), user.getPassword() ) ) { userList.put( user.getName(), user ); }
        return true;
    }

    /**
     * Method that checks if a User has already been created or if the desired new
     * username and new password are valid inputs.  Will create a new user if
     * neither of these conditions are met
     * @param name Desired name for new User object
     * @param password Desired password for new User password
     * @return True if conditions are met, False if otherwise
     */
    public boolean canCreateAccount( String name, String password ) {
        if ( !validInput( name.toUpperCase(), password ) ) { return false; }
        formatHelper.basicMessage( "SYSTEM", 
            ( doesUserExist( name.toUpperCase() ) ) ? "User already exists." : "Username has not been taken, and account creation was successful." );
        return !doesUserExist( name );
    }

    /**
     * Account creation primary method that handles user creations.  If user neither already exists
     * nor the desired name and password are null, then user will be created
     * @param name Desired username for new User object
     * @param password Desired password for new User object
     * @return True if user account creation was successful, False if otherwise
     */
    public boolean accountCreation( String name, String password ) {
        if ( canCreateAccount( name.toUpperCase(), password ) ) { 
            return addUser( new User( name.toUpperCase(), password ) ); }
        System.out.println( formatHelper.basicMessage( "SYSTEM: USER CREATION PROHIBITED", "Username has already been taken.") );
        return false;
    }
}