package game.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConn_impl implements DatabaseConn
{
    // ---------- LAZY INITIALIZATION ----------//
    /////////////////// THE ONLY INSTANCE TO USE ///////////////////
    public static DatabaseConn_impl instance = null;

    // URL, User to conenct to db
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/swe1user";
    private static final String user = "swe1user";
    private static final String psw = "swe1pw";

    // PRIVATE Default-Constructor
    private DatabaseConn_impl() {}

    public static DatabaseConn_impl getInstance()
    {
        // ensure only one instance exists
        if(instance == null) instance = new DatabaseConn_impl();
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