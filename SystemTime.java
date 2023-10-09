import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The SystemTime class, which handles all recording of time-related inquiries (length of log-in,
 * account creation date)
 * 
 * Bugs: None
 * 
 * @author David Sanchez (@davchez on GitHub)
 */
public class SystemTime {
    
    /**
     * Method which returns the current time in month, day, year format with an
     * hour, minute, second format for time
     * @return returns a readable formatted String of the current time
     */
    public String getCurrentTime() {
        LocalDateTime timer = LocalDateTime.now();
        DateTimeFormatter timerFormat = DateTimeFormatter.ofPattern( "MM-dd-yyyy HH:mm:ss" );
        String result = timer.format( timerFormat );
        return result;
    }
}
