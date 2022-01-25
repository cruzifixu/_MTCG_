package game.battle;
import lombok.Getter;
import java.sql.Timestamp;
import java.time.Instant;

public class BattleFightLogger
{
    @Getter
    private StringBuilder log = new StringBuilder();
    // ---------- LAZY INITIALIZATION ----------//
    /////////////////// THE ONLY INSTANCE TO USE ///////////////////
    public static BattleFightLogger instance = null;

    // PRIVATE Default-Constructor
    private BattleFightLogger() {}

    public static BattleFightLogger getInstance()
    {
        // ensure only one instance exists
        if(instance == null) instance = new BattleFightLogger();
        return instance;
    }
    public void newLogEntry(String entry) { this.log.append(Timestamp.from(Instant.now()) + ": " + entry); }
}
