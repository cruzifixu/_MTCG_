package game.deck;

import game.db.databaseConn_impl;
import game.user.user_impl;
import org.codehaus.jackson.JsonNode;

import java.sql.*;

public class DeckDBAccess_impl implements DeckDBAccess
{

    @Override
    public boolean addUserDeck(user_impl user) {
        try {
            Connection conn = databaseConn_impl.getInstance().getConn();
            int count = 1;

            while(count < 5)
            {
                // ----- PREPARED STATEMENT ----- //
                PreparedStatement sta = conn.prepareStatement(
                        "INSERT INTO deck (owner, card_num) VALUES (?, ?);"
                        );
                sta.setString(1, user.getUsername());
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
                if(format.equals("json"))
                {
                    userData.append("{\"card_num\":"+ "\"" +res.getInt(1)+ "\",")
                            .append("\"username\":" + "\"" + res.getString(2)+ "\",")
                            .append("\"id\":" + "\"" + res.getString(3)+ "\",")
                            .append("\"card_name\":" + "\"" + res.getString(4)+ "\",")
                            .append("\"element_type\":"+ "\"" + res.getString(5)+ "\",")
                            .append("\"damage\":" + "\"" + res.getInt(6)+ "\",")
                            .append("\"package_num\":" + "\"" + res.getInt(7)+ "\"}\n")
                    ;
                }
                else
                {
                    userData.append(res.getInt(1) + " ")
                            .append(res.getString(2)+ " ")
                            .append(res.getString(3)+ " ")
                            .append(res.getString(4)+ " ")
                            .append(res.getString(5)+ " ")
                            .append(res.getInt(6)+ " ")
                            .append(res.getInt(7)+ "\n")
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
    public boolean setUserDeck(JsonNode node, int num) throws SQLException {
        Connection conn = databaseConn_impl.getInstance().getConn();
        // ----- PREPARED STATEMENT ----- //
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE deck SET id = ?, name = ?, element_type = ?, damage = ?, package_num = ? WHERE owner = ? AND card_num = ?;"
        );

        stmt.setString(1, node.get("id").getValueAsText());
        stmt.setString(2, node.get("card_name").getValueAsText());
        stmt.setString(3, node.get("element_type").getValueAsText());
        stmt.setInt(4, Integer.parseInt(node.get("damage").getValueAsText()));
        stmt.setInt(5, Integer.parseInt(node.get("package_num").getValueAsText()));
        stmt.setString(6, node.get("username").getValueAsText());
        stmt.setInt(7, num);

        int affectedRows = stmt.executeUpdate();

        if(affectedRows == 0) { return false; }

        stmt.close();
        conn.close();

        return true;
    }
}
