package game.user;

import game.db.databaseConn_impl;

import java.sql.*;

public class UserDBAccess_impl implements UserDBAccess
{

    @Override
    public String getUser(String user) throws SQLException {
        Connection conn = databaseConn_impl.getInstance().getConn();
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
        try {
            Connection conn = databaseConn_impl.getInstance().getConn();
            PreparedStatement sta = conn.prepareStatement(
                    "INSERT INTO users (token, username, password, coins) VALUES (?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            sta.setString(1, " ");
            sta.setString(2, username);
            sta.setString(3, psw);
            sta.setInt(4, 20);

            int affectedRows = sta.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                sta.close();
                conn.close();
                return null;
            }
            sta.close();
            conn.close();
            return this.getUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
