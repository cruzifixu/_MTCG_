package game.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConn implements databaseConn_interface
{
    // ---------- LAZY INITIALIZATION ----------//
    /////////////////// THE ONLY INSTANCE TO USE ///////////////////
    public static DatabaseConn instance = null;

    // URL, User to conenct to db
    private static String dbUrl = "jdbc:postgresql://localhost:5432/swe1user";
    private static String user = "swe1user";
    private static String psw = "swe1pw";

    // PRIVATE Default-Constructor
    private DatabaseConn() {}

    public static DatabaseConn getInstance()
    {
        // ensure only one instance exists
        if(instance == null) instance = new DatabaseConn();
        return instance;
    }

    @Override
    public Connection getConn()
    {
        try
        {
            return DriverManager.getConnection(dbUrl, user, psw);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
