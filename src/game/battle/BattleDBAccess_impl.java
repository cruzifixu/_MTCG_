package game.battle;

import game.db.DatabaseConn_impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattleDBAccess_impl implements BattleDBAccess{

    @Override
    public boolean ChangePlayerStatus(String user, String status) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE users SET status = ? WHERE username = ?;"
            );
            stmt.setString(1, status);
            stmt.setString(2, user);
            int affectedRows = stmt.executeUpdate();

            if(affectedRows == 0)
            {
                stmt.close();
                conn.close();
            }
            stmt.close();
            conn.close();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public String checkOnePlayerStatus(String user) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT status FROM users WHERE username = ?;"
            );
            stmt.setString(1, user);
            ResultSet res = stmt.executeQuery();

            StringBuilder userData = new StringBuilder();
            if(res.next())
            { userData.append(res.getString(1)); }

            res.close();
            stmt.close();
            conn.close();
            return userData.toString();
        } catch (SQLException e) { e.printStackTrace(); }

        return null;
    }

    @Override
    public String checkAllPlayerStatus(String user)
    {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, username, coins, nickname, status FROM users WHERE status = ?;"
            );
            stmt.setString(1, "waiting for battle...");
            ResultSet res = stmt.executeQuery();

            StringBuilder userData = new StringBuilder();
            while (res.next())
            {
                // --- so user is not added - get first waiting user
                if(!res.getString(1).equals(user))
                {
                    userData.append(res.getString(2)); // Username
                    //userData.append("{\"id\":\"").append(res.getString(1))
                    //        .append("\", \"username\":\"").append(res.getString(2))
                    //        .append("\", \"coins\":\"").append(res.getString(3))
                    //        .append("\", \"nickname\":\"").append(res.getString(4))
                    //        .append("\", \"status\":\"").append(res.getString(5)).append("\"}");
                    break;
                }
            }
            res.close();
            stmt.close();
            conn.close();
            return userData.toString();
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
