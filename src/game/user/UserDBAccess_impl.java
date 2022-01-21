package game.user;

import game.db.databaseConn_impl;
import game.deck.DeckDBAccess_impl;
import org.codehaus.jackson.JsonNode;

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
    public String addUser(JsonNode node)
    {
        try {
            Connection conn = databaseConn_impl.getInstance().getConn();
            PreparedStatement sta = conn.prepareStatement(
                    "INSERT INTO users (token, username, password, coins) VALUES (?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            sta.setString(1, " ");
            sta.setString(2, node.get("Username").getValueAsText());
            sta.setString(3, node.get("Password").getValueAsText());
            sta.setInt(4, 20);

            int affectedRows = sta.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                sta.close();
                conn.close();
                return null;
            }

            DeckDBAccess_impl deckDBAccess_impl = new DeckDBAccess_impl();
            deckDBAccess_impl.addUserDeck(node.get("Username").getValueAsText());

            sta.close();
            conn.close();
            return this.getUser(node.get("Username").getValueAsText());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String loginUser(JsonNode node) throws SQLException {
        Connection conn = databaseConn_impl.getInstance().getConn();
        PreparedStatement sta = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?;"
        );
        sta.setString(1, node.get("Username").getValueAsText());
        sta.setString(2, node.get("Password").getValueAsText());
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

}
