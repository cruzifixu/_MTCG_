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
                    "UPDATE users SET bio = ? WHERE username = ?;"
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String checkPlayerStatus(String user) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT bio FROM users WHERE username = ?;"
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
