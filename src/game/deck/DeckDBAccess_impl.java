package game.deck;

import game.db.databaseConn_impl;
import org.codehaus.jackson.JsonNode;

import java.sql.*;

public class DeckDBAccess_impl implements DeckDBAccess
{

    @Override
    public boolean addUserDeck(JsonNode node) {
        try {
            Connection conn = databaseConn_impl.getInstance().getConn();
            int count = 1;

            while(count < 5)
            {
                // ----- PREPARED STATEMENT ----- //
                PreparedStatement sta = conn.prepareStatement(
                        "INSERT INTO deck (owner, card_num) VALUES (?, ?);"
                        );
                sta.setString(1, node.get("Username").getValueAsText());
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
    public String getUserDeck(String username) {
        try
        {
            Connection conn = databaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM deck WHERE owner = ?;"
            );
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();

            StringBuilder userData = new StringBuilder();
            while(res.next())
            {
                userData.append(res.getString(1)).append("\n") // owner
                        .append(res.getString(2)).append("\n") // card_name
                        .append(res.getString(3)).append("\n") // category_type
                        .append(res.getString(4)).append("\n") // element_type
                        .append(res.getString(5)).append("\n"); // damage
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
    public boolean setUserDeck(String id, String username, int num) throws SQLException {
        Connection conn = databaseConn_impl.getInstance().getConn();
        // ----- PREPARED STATEMENT ----- //
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE deck SET id = ? WHERE owner = ? AND card_num = ?;"
        );
        stmt.setString(1, id);
        stmt.setString(2, username);
        stmt.setInt(3, num);

        int affectedRows = stmt.executeUpdate();

        if(affectedRows == 0) { return false; }

        stmt.close();
        conn.close();

        return true;
    }
}
