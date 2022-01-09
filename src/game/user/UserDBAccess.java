package game.user;

import game.db.DatabaseConn;

import java.sql.*;

public class UserDBAccess implements UserDBAccess_Interface
{

    @Override
    public String getUser(String user) throws SQLException {
        Connection conn = DatabaseConn.getInstance().getConn();
        PreparedStatement sta = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ?;"
        );
        sta.setString(1, String.valueOf(user));
        ResultSet res = sta.executeQuery();
        StringBuilder userData = new StringBuilder();

        // get userdata results
        if(res.next()) {
            userData.append(res.getString(2)).append("\n").append(res.getString(3)).append("\n")
                    .append(res.getString(4)).append("\n");
        }

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
                    "INSERT INTO users (token, username, password, coins) VALUES (?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            sta.setString(1, " ");
            sta.setString(2, username);
            sta.setString(3, psw);
            sta.setInt(4, 0);

            int affectedRows = sta.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                return null;
            }

            try (ResultSet generatedKeys = sta.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // close
                    sta.close();
                    conn.close();
                    return this.getUser(username);
                }
            }
            // close
            sta.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
