package game.card;

import game.db.databaseConn_impl;
import java.sql.*;
import java.util.ArrayList;

public class CardsDBAccess_impl implements CardsDBAccess
{
    @Override
    public boolean createPackage(ArrayList<String> oneCard, int package_num)
    {
        try {
            Connection conn = databaseConn_impl.getInstance().getConn();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO packages (ownedby) VALUES (?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "");
            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected == 0) {
                stmt.close();
                conn.close();
                return false;
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();

            if(generatedKeys.next())
            {
                int count = 0, cardField = 0;
                while(count < 5)
                {
                    count++;
                    PreparedStatement sta = conn.prepareStatement(
                            "INSERT INTO Cards (id, name, damage, package_num) VALUES (?, ?, ?, ?);"
                    );
                    sta.setString(1, oneCard.get(cardField));
                    sta.setString(2, oneCard.get(cardField+1));
                    sta.setDouble(3, Double.parseDouble(oneCard.get(cardField+2)));
                    sta.setInt(4, generatedKeys.getInt(1));

                    int affectedRows = sta.executeUpdate();
                    cardField += 3;
                    // couldn't get executed
                    if (affectedRows == 0) {
                        PreparedStatement stmt2 = conn.prepareStatement(
                                "DELETE FROM packages WHERE package_num = ?;"
                        );
                        stmt2.setInt(1, generatedKeys.getInt(1));
                        stmt2.executeUpdate();

                        generatedKeys.close();
                        stmt2.close();
                        sta.close();
                        conn.close();
                        return false;
                    }
                    sta.close();
                }
            }
            generatedKeys.close();
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
        try {
            Connection conn = databaseConn_impl.getInstance().getConn();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT coins FROM users WHERE username = ?;"
            );
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();
            if(res.next())
            {
                int coins = res.getInt(1);
                if(coins <= 0)
                {
                    res.close();
                    stmt.close();
                    conn.close();
                    return false;
                }
            }
            res.close();
            stmt.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean acquirePackage(String username)
    {
        try
        {
            Connection conn = databaseConn_impl.getInstance().getConn();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT package_num FROM packages WHERE ownedby = ?;"
            );
            stmt.setString(1, "");
            ResultSet res = stmt.executeQuery();
            if(this.checkBalance(username)) // enough money
            {
                if(res.next()) // next free package
                {
                    // ----- PREPARED STATEMENT ----- //
                    PreparedStatement sta = conn.prepareStatement(
                            "UPDATE packages SET ownedby = ? WHERE package_num = ?;"
                    );
                    sta.setString(1, username);
                    sta.setInt(2, res.getInt(1));

                    // ----- PREPARED STATEMENT ----- //
                    PreparedStatement sta2 = conn.prepareStatement(
                            "UPDATE cards SET ownedby = ? WHERE package_num = ?;"
                    );
                    sta2.setString(1, username);
                    sta2.setInt(2, res.getInt(1));

                    int affectedRows = sta.executeUpdate(), affectedRows2 = sta2.executeUpdate();

                    if(affectedRows == 0)
                    {
                        // ----- PREPARED STATEMENT ----- //
                        // reverse Stmt so no one owns this
                        PreparedStatement reverseStmt = conn.prepareStatement(
                                "UPDATE packages SET ownedby = ? WHERE package_num = ?;"
                        );
                        reverseStmt.setString(1, "");
                        reverseStmt.setInt(2, res.getInt(1));
                        reverseStmt.executeUpdate();

                        // close
                        sta.close();
                        res.close();
                        stmt.close();
                        return false;
                    }
                    if(affectedRows2 == 0)
                    {
                        // ----- PREPARED STATEMENT ----- //
                        // reverse Stmt so no one owns this
                        PreparedStatement reverseStmt = conn.prepareStatement(
                                "UPDATE packages SET ownedby = ? WHERE package_num = ?;"
                        );
                        reverseStmt.setString(1, "");
                        reverseStmt.setInt(2, res.getInt(1));
                        reverseStmt.executeUpdate();

                        // close
                        sta.close();
                        sta2.close();
                        stmt.close();
                        return false;
                    }
                    // close
                    sta2.close();
                    sta.close();

                    // ----- PREPARED STATEMENT ----- //
                    PreparedStatement sta3 = conn.prepareStatement(
                            "SELECT coins FROM users WHERE username = ?;"
                    );
                    sta3.setString(1, username);
                    ResultSet res2 = sta3.executeQuery();
                    if(res2.next())
                    {
                        int coins = res2.getInt(1);
                        int NewCoins = coins - 5;
                        // ----- PREPARED STATEMENT ----- //
                        PreparedStatement sta4 = conn.prepareStatement(
                                "UPDATE users SET coins = ? WHERE username = ?;"
                        );
                        sta4.setInt(1, NewCoins);
                        sta4.setString(2, username);
                        sta4.executeUpdate();
                        sta4.close();
                        sta3.close();
                    }
                    sta3.close();
                }
                else
                {
                    res.close();
                    stmt.close();
                    conn.close();
                    return false;
                }
                res.close();
                stmt.close();
            }
            else
            {
                res.close();
                stmt.close();
                conn.close();
                return false;
            }
            res.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean showCards(String user)
    {
        try
        {
          Connection conn = databaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
          PreparedStatement stmt = conn.prepareStatement(
                  "SELECT * FROM cards WHERE ownedby = ?;"
          );
          stmt.setString(1, user);
          ResultSet res = stmt.executeQuery();
          if(res.next())
          {

          }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
