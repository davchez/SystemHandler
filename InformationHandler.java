import java.util.*;

/**
 * InformationHandler class which processes user input, contains all user information, and utilizes
 * a "Processor" subclass which contains lambda expressions for execution based on user input
 * 
 * Bugs: Security questions can repeat multiple times when prompted by a failed log-in attempt
 * 
 * @author David Sanchez (@davchez on GitHub)
 */
public class InformationHandler {

    /**
     * Processor subclass which contains lambda expressions which are executed based off of key
     * terminal user inputs.  Meant for flexibility and easy expansion if more 
     * capabilities are added to the SystemHandler package
     */
    class Processor {

        // Executable/runnable methods
        private final Runnable ADD_SECURITY_QUESTIONS = () -> { addSecurityQuestions(); };
        private final Runnable CHALLENGE_SECURITY_QUESTION = () -> { challengeSecurityQuestion(); };
        private final Runnable GET_TIME_ELAPSED = () -> { promptTimeElapsed(); };
        private final Runnable SET_BEGINNING_TIME = () -> { setBeginningTime(); };

        // Sorting into arrays so that the processor can initialize them into a HashMap
        private final String[] keyArray = 
            new String[]{ "ADD SECURITY QUESTIONS", "CHALLENGE SECURITY QUESTION", "GET TIME ELAPSED", "SET BEGINNING TIME" };
        private final Runnable[] runnableArray = 
            new Runnable[]{ ADD_SECURITY_QUESTIONS, CHALLENGE_SECURITY_QUESTION, GET_TIME_ELAPSED, SET_BEGINNING_TIME };

        // Necessary object for lambda expressions executions
        private Map< String, Runnable > executableProcessor;

        private final int TOTAL_NUMBER_OF_ITEMS = keyArray.length;

        /**
         * No-arg constructor method
         */
        public Processor() {
            this.executableProcessor = new HashMap<>();
            initializeProcessor();
        }

        /**
         * Getter method which returns the list of processor keys
         * @return String array of procesor keys
         */
        public String[] getKeys() {
            return this.keyArray;
        }

        /**
         * Void method which initializes the processor by adding final key Strings as
         * keys to the executableProcessor HashMap, with runnable methods as values
         */
        public void initializeProcessor() {
            for (int i = 0; i < TOTAL_NUMBER_OF_ITEMS; i++) {
                this.executableProcessor.put( keyArray[i], runnableArray[i] );
            }
        }

        /**
         * Returns executable processor so that other class files can execute the processor capabilities
         * @return Map object called executableProcessor
         */
        public Map < String, Runnable > getExecutableProcessor() {
            System.out.println( formatHelper.basicMessage( "SYSTEM ALERT", "PROCESSOR IS EXECUTING A LAMBDA EXPRESSION!" ) );
            return this.executableProcessor;
        }
    }

    private final String ADD_SQ_PROCESSOR_KEY = "ADD SECURITY QUESTIONS";
    private final String NO_SQ_ERROR = "No security questions found for this account. Login failed.";
    private final String[] POSSIBLE_SECURITY_QUESTIONS = 
        new String[]{ "what is your father's middle name?", "what year was your mother born?", 
                      "what was the name of your high school best friend?", 
                      "what city were you born in?", "what was the name of your favorite teacher?" };
    private final String[] KEY_CHECKLIST = new String[]{ ADD_SQ_PROCESSOR_KEY };
    private final Integer MAX_NUM_SECURITY_QUESTIONS = 3;    
    private final int MAX_SQ_ATTEMPTS = 3;

    // Checklist of incomplete and complete items which requires action from the user
    private Map< String, Boolean > checklist;

    // Security question HashMap where the keys are questions and the values are answers
    private Map< String, String > securityQuestions;

    // Checks if a log-in attempt successfully passed a security question challenge
    private boolean passedSecurityQuestions;

    // Processes user terminal input
    private Processor processor;

    // Used to track time in a readable format
    private SystemTime clock;

    // Formats basic messages
    private FormatHelper formatHelper = new FormatHelper();

    // Number of items incomplete in an account and necessary for the user to complete
    private int numberIncomplete = 0;

    // Time of account creation in MM:DD:YYYY HH:mm:ss readable format
    private String timeCreated;

    // Time of successful account login
    private long beginningTime;

    /**
     * No-arg constructor
     */
    public InformationHandler() {
        this.checklist = new HashMap< String, Boolean >();
        this.securityQuestions = new HashMap< String, String >();
        this.processor = new Processor();
        this.passedSecurityQuestions = false;
        this.clock = new SystemTime();
        this.timeCreated = clock.getCurrentTime();
        this.beginningTime = System.currentTimeMillis();
        initializeChecklist();
    }

    /**
     * Getter method that returns the incomplete/complete account items checklist
     * @return HashMap object called checklist
     */
    public Map< String, Boolean > getChecklist() {
        return this.checklist;
    }

    /**
     * Getter method that returns the information handler Processor object
     * @return non-static Processor object
     */
    public Processor getProcessor() {
        return this.processor;
    }

    /**
     * Counts the number of incomplete actions/items that require a user action
     * @return Integer number of incomplete items in the checklist
     */
    public int getNumOfIncompleteItems() {
        this.numberIncomplete = 0;
        for ( boolean value : checklist.values() ) {
            if ( value == false ) this.numberIncomplete++;
        }
        return this.numberIncomplete;
    }

    /**
     * Void method that sets the boolean value of passedSecurityQuestions
     * @param pass Boolean on whether or not the user passed the
     * security question prompt
     */
    private void setPassedSecurityQuestions( boolean pass ) {
        this.passedSecurityQuestions = pass;
    }

    /**
     * Getter method that returns whether or not the user passed 
     * the security question prompts
     * @return True if user passed security questions, False if otherwise
     */
    public boolean getPassedSecurityQuestions() {
        return this.passedSecurityQuestions;
    }

    /**
     * Unused boolean method which returns whether or not a specific
     * action or item in a user's checklist has been completed or not.  
     * Meant for further expansion when SystemHandler becomes more
     * sophisticated
     * @param key String value which corresponds to the incomplete/complete item/action
     * @return True if item is complete, false if item requires a user action
     */
    public boolean getChecklistItem( String key ) {
        return this.checklist.get( key );
    }

    /**
     * Void methoid which initializes the value and keys of the user's checklist
     */
    public void initializeChecklist() {
        for ( String key : KEY_CHECKLIST ) this.checklist.put( key, false );
    }

    /**
     * Void method which accepts String inputs and executes a processor Runnable method
     * if the input matches a processor key.  Otherwise, nothing happens
     * @param key String value which the processor attempts to match to its String keys
     */
    public void executeProcessor( String key ) {
        getProcessor().getExecutableProcessor().get( key ).run();
    }

    /**
     * Returns a string value that contains a basic message that tells the user
     * how many incomplete items that requires a user action on their account
     * @return Formatted string that alerts the user to how many incomplete items
     * their account has
     */
    public String notificationNotice() {
        return formatHelper.basicMessage( "NOTIFS", 
            String.format( "You have %s incomplete item(s).", String.valueOf( getNumOfIncompleteItems() ) ) );
    }

    /**
     * Private helper method that checks if an account created its necessary security
     * questions in the event of a failed login
     * @return True if security question creation is complete, false if otherwise
     */
    private boolean checkSecurityQuestionsComplete() {
        if ( this.securityQuestions.size() < MAX_NUM_SECURITY_QUESTIONS ) { return false; }
        return true;
    }

    /**
     * Private helper method that assists the security question creation process.  Ensures
     * that there are no duplicate security questions in a user's account (or an accidental
     * overwrite of an already-existing security question in the HashMap)
     * @param key String security question to be checked if it already exists in user's
     * security question list
     * @return True if security question does not exist in user account, false if otherwise
     */
    private boolean checkIfSecurityQuestionPresent( String key ) {
        return this.securityQuestions.containsKey( key );
    }

    /**
     * Private helper method which selects a random security question from the private variable
     * String array list POSSIBLE_SECURITY_QUESTIONS
     * @return String question obtained from random selection on POSSIBLE_SECURITY_QUESTIONS
     */
    private String generateRandomSecurityQuestion() {
        return POSSIBLE_SECURITY_QUESTIONS[ ( new Random() ).nextInt( POSSIBLE_SECURITY_QUESTIONS.length ) ].toLowerCase();
    }

    /**
     * Private helper method that handles security question and answer creation, ensuring that
     * there are no duplicates or overwrites of already-existing questions during creation.  Puts
     * security question string as key and answer string as value in the securityQuestions
     * HashMap
     */
    private void createSecurityQuestion() {
        String question = generateRandomSecurityQuestion();
        if ( checkIfSecurityQuestionPresent( question ) ) { return; }
        System.out.println( formatHelper.basicMessage( "SYSTEM", "Please answer the question: " + question ) );
        Scanner input = new Scanner( System.in );
        this.securityQuestions.put( question, input.nextLine().toLowerCase() );
    }

    /**
     * Security question creation primary method which can be called to initiate security question creation.  
     * Will not execute if security question creation has already been completed for the specific account
     * it is called on
     */
    public void addSecurityQuestions() {
        while ( !checkSecurityQuestionsComplete() ) createSecurityQuestion();
        this.checklist.put( ADD_SQ_PROCESSOR_KEY, true );
        System.out.println( formatHelper.basicMessage( "SYSTEM", "Security Question creation complete." ) );
    }

    /**
     * Private helper method which selects a random security question from a user's account.  Currently
     * not finished and results in duplicate selections 
     * @return String object, security question of which was randomly selected from the user's account
     */
    private String selectSecurityQuestion() {
        // BUG: For future, create a stack so that the questions don't repeat
        return (String)this.securityQuestions.keySet().toArray()[ ( new Random() ).nextInt( MAX_NUM_SECURITY_QUESTIONS ) ];
    }
    
    /**
     * Private helper method which displays the security question in the terminal.  Will return
     * null if security question creation is incomplete for the specific account, otherwise
     * will call another method
     * @return String, random selection of a security question or null
     */
    private String displaySecurityQuestion() {
        if ( !checkSecurityQuestionsComplete() ) { 
            return null; }
        return selectSecurityQuestion();
    }

    /**
     * Private helper method which checks whether or not the user entered the right answer
     * to the displayed security question
     * @param question String, security question to be answered
     * @return True if user entered the correct answer, false if otherwise
     */
    private boolean answerSecurityQuestion( String question ) {
        Scanner input = new Scanner( System.in );
        System.out.println( formatHelper.basicMessage( "SECURITY CHALLENGE", "Please answer the following question: " + question ) );
        String answer = input.nextLine().toLowerCase();
        return ( this.securityQuestions.get( question ) ).equals( answer );
    }

    /**
     * Priavte helper method which checks how many times the user attempted
     * a security question after a failed log-in attempt
     * @return True if user answers security question correctly, false if otherwise
     */
    private boolean attemptSecurityQuestion() {
        int attempts = 0;
        while ( attempts < MAX_SQ_ATTEMPTS ) {
            if ( answerSecurityQuestion( displaySecurityQuestion() ) ) { return true; }
            attempts++; }
        return false;
    }

    /**
     * Security question challenge primary method which prompts the user to answer an account's security questions.
     * Will only accept 3 attempts- if failed, the system will close the log-in attempt and return
     * the user to the main SystemHandler menu.  Otherwise will allow a log-in if successful
     */
    public void challengeSecurityQuestion() {
        boolean securityQuestionsExist = displaySecurityQuestion() != null;
        if ( !securityQuestionsExist ) { System.out.println( formatHelper.basicMessage( "ACCOUNT ERROR", NO_SQ_ERROR ) ); }
        else {
        boolean pass = attemptSecurityQuestion();
        setPassedSecurityQuestions( pass ); }
    }

    /**
     * Setter method that sets the time of account creation on a specific account
     */
    public void setTimeCreated() {
        this.timeCreated = clock.getCurrentTime();
    }

    /**
     * Geter method that returns the time an account was created
     * @return
     */
    public String getTimeCreated() {
        return this.timeCreated;
    }

    /**
     * Private helper and getter method that calculates how log a user has been
     * logged into a specific account, and converts it into a readable format
     * @return Readable formatted string for how long a user has been logged into a
     * specific account
     */
    private String getElapsedTime() {
        return String.valueOf( ( System.currentTimeMillis() - this.beginningTime ) / 1000 );
    }

    /**
     * Setter method that sets the time a user logged into a specific account
     */
    public void setBeginningTime() {
        this.beginningTime = System.currentTimeMillis();
    }
    
    /**
     * Method that allows the user to check how much time has elapsed since they logged into
     * a specific account.  Requires future expansion to be a more effective method
     */
    public void promptTimeElapsed() {
        String message = formatHelper.basicMessage( 
            "CLOCK", String.format( "%s seconds since most recent login.", getElapsedTime() ) );
        System.out.println( message );
    }
}