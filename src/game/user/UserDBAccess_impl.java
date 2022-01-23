package game.user;

import game.db.DatabaseConn_impl;
import org.codehaus.jackson.JsonNode;

import java.sql.*;

public class UserDBAccess_impl implements UserDBAccess
{

    @Override
    public String getUser(String user)  {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            PreparedStatement sta = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ?;"
            );
            sta.setString(1, String.valueOf(user));
            ResultSet res = sta.executeQuery();
            StringBuilder userData = new StringBuilder();

            // get userdata results
            if(res.next()) {
                userData.append("{\"id\":\"").append(res.getString(1))
                        .append("\", \"username\":\"").append(res.getString(2))
                        .append("\", \"coins\":\"").append(res.getString(3))
                        .append("\", \"nickname\":\"").append(res.getString(4))
                        .append("\", \"bio\":\"").append(res.getString(5))
                        .append("\", \"image\":\"").append(res.getString(6)).append("\"}");
            }

            // close everything before returning data
            res.close();
            sta.close();
            conn.close();
            return userData.toString();
        } catch(SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public String getUserWithoutSenInfo(String user) {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            PreparedStatement sta = conn.prepareStatement(
                    "SELECT id, username, coins, nickname, bio, image FROM users WHERE username = ?;"
            );
            sta.setString(1, String.valueOf(user));
            ResultSet res = sta.executeQuery();
            StringBuilder userData = new StringBuilder();

            // get userdata results
            if(res.next()) {
                userData.append("{\"id\":\"").append(res.getString(1))
                        .append("\", \"username\":\"").append(res.getString(2))
                        .append("\", \"coins\":\"").append(res.getString(3))
                        .append("\", \"nickname\":\"").append(res.getString(4))
                        .append("\", \"bio\":\"").append(res.getString(5))
                        .append("\", \"image\":\"").append(res.getString(6)).append("\"}");
            }

            // close everything before returning data
            res.close();
            sta.close();
            conn.close();
            return userData.toString();
        } catch(SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean EditUserData(User_impl user) throws SQLException {
        Connection conn = DatabaseConn_impl.getInstance().getConn();
        // ----- PREPARED STATEMENT ----- //
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE users SET nickname = ?, bio = ?, image = ? WHERE username = ?;"
        );

        stmt.setString(1, user.getNickname());
        stmt.setString(2, user.getBio());
        stmt.setString(3, user.getImage());
        stmt.setString(4, user.getUsername());

        int affectedRows = stmt.executeUpdate();

        if(affectedRows == 0) { return false; }

        stmt.close();
        conn.close();

        return true;
    }

    @Override
    public String addUser(User_impl user)
    {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            PreparedStatement sta = conn.prepareStatement(
                    "INSERT INTO users (token, username, password, coins, won, lost) VALUES (?, ?, ?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            sta.setString(1, " ");
            sta.setString(2, user.getUsername());
            sta.setString(3, user.getPassword());
            sta.setInt(4, 20);
            sta.setInt(5, 0);
            sta.setInt(6, 0);

            int affectedRows = sta.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                sta.close();
                conn.close();
                return null;
            }

            sta.close();
            conn.close();
            return this.getUser(user.getUsername());
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public String loginUser(JsonNode node) {
       try {
           Connection conn = DatabaseConn_impl.getInstance().getConn();
           PreparedStatement sta = conn.prepareStatement(
                   "SELECT * FROM users WHERE username = ? AND password = ?;"
           );
           sta.setString(1, node.get("Username").getValueAsText());
           sta.setString(2, node.get("Password").getValueAsText());
           ResultSet res = sta.executeQuery();
           StringBuilder userData = new StringBuilder();

           // get userdata results
           if(res.next()) {
               userData.append("{\"id\":\"").append(res.getString(1))
                       .append("\", \"username\":\"").append(res.getString(2))
                       .append("\", \"coins\":\"").append(res.getString(3))
                       .append("\", \"nickname\":\"").append(res.getString(4))
                       .append("\", \"bio\":\"").append(res.getString(5))
                       .append("\", \"image\":\"").append(res.getString(6)).append("\"}");
           }

           // close everything before returning data
           res.close();
           sta.close();
           conn.close();
           return userData.toString();
       } catch(SQLException e) { e.printStackTrace(); }
       return null;
    }

    @Override
    public String getStats(String user) {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            PreparedStatement sta = conn.prepareStatement(
                    "SELECT won, lost, elo FROM users WHERE username = ?;"
            );
            sta.setString(1, user);
            ResultSet res = sta.executeQuery();

            StringBuilder userData = new StringBuilder();

            // get userdata results
            if(res.next()) {
                userData.append("{\"wins\":\"").append(res.getString(1))
                        .append("\", \"lost\":\"").append(res.getString(2))
                        .append("\", \"elo\":\"").append(res.getString(3)).append("\"}");
            }

            System.out.println("userdata "+ userData);

            // close everything before returning data
            res.close();
            sta.close();
            conn.close();
            return userData.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getScore() {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            PreparedStatement sta = conn.prepareStatement(
                    "SELECT username, elo FROM users;"
            );
            ResultSet res = sta.executeQuery();
            StringBuilder userData = new StringBuilder();

            // get userdata results
            while(res.next()) {
                userData.append("{\"username\":\"").append(res.getString(1))
                        .append("\", \"elo\":\"").append(res.getString(2)).append("\"}\n");
            }

            // close everything before returning data
            res.close();
            sta.close();
            conn.close();
            return userData.toString();
        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }
}
