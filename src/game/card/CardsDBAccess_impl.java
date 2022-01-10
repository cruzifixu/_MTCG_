package game.card;

import game.db.databaseConn_impl;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CardsDBAccess_impl implements CardsDBAccess
{
    @Override
    public boolean createPackage(ArrayList<String> oneCard, int package_num)
    {
        Connection conn = databaseConn_impl.getInstance().getConn();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO packages (ownedby) VALUES (?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "");
            int rowsaffected = stmt.executeUpdate();

            if(rowsaffected == 0) { return false; }

            ResultSet generatedKeys = stmt.getGeneratedKeys();

            if(generatedKeys.next())
            {
                int count = 0, cardField = 0;
                while(count < 5)
                {
                    count++;
                    PreparedStatement sta = conn.prepareStatement(
                            "INSERT INTO Cards (id, name, damage, package_num) VALUES (?, ?, ?, ?);");
                    sta.setString(1, oneCard.get(cardField));
                    sta.setString(2, oneCard.get(cardField+1));
                    sta.setDouble(3, Double.parseDouble(oneCard.get(cardField+2)));
                    sta.setInt(4, generatedKeys.getInt(1));

                    int affectedRows = sta.executeUpdate();
                    cardField += 3;
                    // couldn't get executed
                    if (affectedRows == 0) { return false; }


                    sta.close();
                }
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }


}
