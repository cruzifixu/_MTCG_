package game.deck;
import game.card.Cards_impl;
import game.card.Monstercard;
import game.card.Spellcard;
import game.db.DatabaseConn_impl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class DeckDBAccess_impl implements DeckDBAccess
{
    @Override
    public String getUserDeck(String username, String format) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM card WHERE ownedby = ? AND in_deck = ?;"
            );
            stmt.setString(1, username);
            stmt.setBoolean(2, true);
            ResultSet res = stmt.executeQuery();

            StringBuilder userData = new StringBuilder();
            while(res.next())
            {
                if(format.equals("json"))
                {
                    userData.append("{\"id\":" + "\"").append(res.getString(1)).append("\",")
                            .append("\"card_name\":" + "\"").append(res.getString(2)).append("\",")
                            .append("\"element_type\":" + "\"").append(res.getString(3)).append("\",")
                            .append("\"damage\":" + "\"").append(res.getString(4)).append("\",")
                            .append("\"username\":" + "\"").append(res.getString(5)).append("\",")
                            .append("\"package_num\":" + "\"").append(res.getInt(6)).append("\"}\n")
                    ;
                }
                else
                {
                    userData.append(res.getString(1)).append(" ")
                            .append(res.getString(2)).append(" ")
                            .append(res.getString(3)).append(" ")
                            .append(res.getInt(4)).append(" ")
                            .append(res.getString(5)).append(" ")
                            .append(res.getInt(6)).append("\n")
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
    public ArrayList<Cards_impl> getUserDeckAsCards(String username) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM card WHERE ownedby = ? AND in_deck = ?;"
            );
            stmt.setString(1, username);
            stmt.setBoolean(2, true);
            ResultSet res = stmt.executeQuery();

            ArrayList<Cards_impl> Deck = new ArrayList<>();

            while(res.next())
            {
                Cards_impl card;
                if(res.getString(2).toUpperCase(Locale.ROOT).contains("MONSTER"))
                {
                    card = new Monstercard(res.getString(1), res.getString(2),
                            res.getString(3), res.getInt(4), res.getString(5), res.getInt(6));
                }
                else
                {
                    card = new Spellcard(res.getString(1), res.getString(2),
                            res.getString(3), res.getInt(4), res.getString(5), res.getInt(6));
                }
                Deck.add(card);
            }

            res.close();
            stmt.close();
            conn.close();
            return Deck;
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
                "UPDATE card SET in_deck = ? WHERE id = ?;"
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
