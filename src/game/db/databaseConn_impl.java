package game.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class databaseConn_impl implements databaseConn
{
    // ---------- LAZY INITIALIZATION ----------//
    /////////////////// THE ONLY INSTANCE TO USE ///////////////////
    public static databaseConn_impl instance = null;

    // URL, User to conenct to db
    private static String dbUrl = "jdbc:postgresql://localhost:5432/swe1user";
    private static String user = "swe1user";
    private static String psw = "swe1pw";

    // PRIVATE Default-Constructor
    private databaseConn_impl() {}

    public static databaseConn_impl getInstance()
    {
        // ensure only one instance exists
        if(instance == null) instance = new databaseConn_impl();
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