package game.user;

import game.db.DatabaseConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBAccess implements UserDBAccess_Interface
{

    @Override
    public String getUser(int ID) throws SQLException {
        Connection conn = DatabaseConn.getInstance().getConn();
        PreparedStatement sta = conn.prepareStatement(
                "SELECT * FROM users WHERE ID = ?;"
        );
        sta.setString(1, String.valueOf(ID));
        ResultSet res = sta.executeQuery();
        StringBuilder userData = new StringBuilder();

        // get userdata results
        if(res.next()) {
            userData.append(res.getString(1)).append("\n").append(res.getString(2)).append("\n")
                    .append(res.getString(3)).append("\n");
        }
        else return null;

        // close everything before returning data
        res.close();
        sta.close();
        conn.close();

        return userData.toString();
    }

    @Override
        public String addUser(String username, String psw)
    {
        Connection conn = DatabaseConn.getInstance().getConn();
        try {
            PreparedStatement sta = conn.prepareStatement(
                    "INSERT INTO users (username, password, coins) VALUES (?, ?, ?);"
            );
            sta.setString(1, username);
            sta.setString(2, psw);
            sta.setString(3, "0");

            int affectedRows = sta.executeUpdate();

            // couldnt get executed
            if (affectedRows == 0) {
                return null;
            }

            try (ResultSet generatedKeys = sta.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // close
                    sta.close();
                    conn.close();
                    return this.getUser(generatedKeys.getInt(1));
                }
            }
            // close
            sta.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
