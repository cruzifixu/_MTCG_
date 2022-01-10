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

    @Override
    public boolean checkBalance(String username)
    {
        Connection conn = databaseConn_impl.getInstance().getConn();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT coins FROM users WHERE username = ?;"
            );
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();
            if(res.next())
            {
                int coins = res.getInt(1);
                if(coins > 0) { this.acquirePackage(username); }
                else { return false; }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean acquirePackage(String username)
    {
        Connection conn = databaseConn_impl.getInstance().getConn();
        try {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT package_num FROM packages WHERE ownedby = ?;"
        );

            stmt.setString(1, "");
            ResultSet res = stmt.executeQuery();
            if(this.checkBalance(username)) // enough money
            {
                if(res.next()) // next free package
                {
                    PreparedStatement sta = conn.prepareStatement(
                            "UPDATE packages SET ownedby = ? WHERE package_num = ?;"
                    );
                    sta.setString(1, username);
                    sta.setString(2, res.getString(1));

                    int affectedrows = sta.executeUpdate();

                    if(affectedrows == 0) { return false; }

                    sta.close();
                }
                else { return false; }
            }
            else { return false; }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
