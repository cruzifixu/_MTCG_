package game.deck;

import game.db.DatabaseConn_impl;
import game.user.User_impl;

import java.sql.*;

public class DeckDBAccess_impl implements DeckDBAccess
{

    @Override
    public boolean addUserDeck(User_impl user) {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            int count = 1;

            while(count < 5)
            {
                // ----- PREPARED STATEMENT ----- //
                PreparedStatement sta = conn.prepareStatement(
                        "UPDATE cards SET in_deck = ? WHERE id = ?;"
                        );
                sta.setBoolean(1, true);
                sta.setInt(2, count);

                int affectedRows = sta.executeUpdate();

                // couldn't get executed
                if (affectedRows == 0) {
                    sta.close();
                    conn.close();
                    return false;
                }
                sta.close();
                count++;
            }
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getUserDeck(String username, String format) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM cards WHERE ownedby = ? AND in_deck = ?;"
            );
            stmt.setString(1, username);
            stmt.setBoolean(2, true);
            ResultSet res = stmt.executeQuery();

            StringBuilder userData = new StringBuilder();
            while(res.next())
            {
                if(format.equals("json"))
                {
                    userData.append("{\"id\":"+ "\"" +res.getString(1)+ "\",")
                            .append("\"card_name\":" + "\"" + res.getString(2)+ "\",")
                            .append("\"element_type\":" + "\"" + res.getString(3)+ "\",")
                            .append("\"damage\":" + "\"" + res.getString(4)+ "\",")
                            .append("\"username\":"+ "\"" + res.getString(5)+ "\",")
                            .append("\"package_num\":" + "\"" + res.getInt(6)+ "\"}\n")
                    ;
                }
                else
                {
                    userData.append(res.getString(1) + " ")
                            .append(res.getString(2)+ " ")
                            .append(res.getString(3)+ " ")
                            .append(res.getInt(4)+ " ")
                            .append(res.getString(5)+ " ")
                            .append(res.getInt(6)+ "\n")
                    ;
                }

            }

            res.close();
            stmt.close();
            conn.close();
            return userData.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean setUserDeck(String id) throws SQLException {
        Connection conn = DatabaseConn_impl.getInstance().getConn();
        // ----- PREPARED STATEMENT ----- //
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE cards SET in_deck = ? WHERE id = ?;"
        );
        stmt.setBoolean(1, true);
        stmt.setString(2, id);

        int affectedRows = stmt.executeUpdate();

        if(affectedRows == 0)
        {
            stmt.close();
            conn.close();
            return false;
        }

        stmt.close();
        conn.close();

        return true;
    }
}
