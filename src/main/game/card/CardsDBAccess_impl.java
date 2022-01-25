package game.card;

import game.db.DatabaseConn_impl;
import java.sql.*;

public class CardsDBAccess_impl implements CardsDBAccess
{
    @Override
    public int createPackage()
    {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO packages (ownedby) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "");
            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected == 0) {
                stmt.close();
                conn.close();
                return 0;
            }

            int num = 0;

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next())
            { num = generatedKeys.getInt(1); }

            generatedKeys.close();
            stmt.close();
            conn.close();

            return num;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean addCardToPackage(Cards_impl card) throws SQLException {
        Connection conn = DatabaseConn_impl.getInstance().getConn();
        // ----- PREPARED STATEMENT ----- //
        PreparedStatement sta = conn.prepareStatement(
                "INSERT INTO cards (id, name,element_type, damage, package_num) VALUES (?, ?, ?, ?, ?);"
        );
        sta.setString(1, card.getId());                     // set id
        sta.setString(2, card.getCard_name());              // set card name
        sta.setString(3, card.getElement_type());           // set element type
        sta.setDouble(4, card.getDamage());                 // set damage
        sta.setInt(5, card.getPackage_num());               // set package num

        int affectedRows = sta.executeUpdate();
        // couldn't get executed
        if (affectedRows == 0) {
            PreparedStatement stmt2 = conn.prepareStatement(
                    "DELETE FROM packages WHERE package_num = ?;"
            );
            stmt2.setInt(1, card.getPackage_num());
            stmt2.executeUpdate();

            stmt2.close();
            sta.close();
            conn.close();
            return false;
        }

        sta.close();
        conn.close();
        return true;
    }

    @Override
    public boolean checkBalance(String username)
    {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT coins FROM users WHERE username = ?;"
            );
            stmt.setString(1, username);    // set username
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
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
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

    @Override
    public String showCards(String user)
    {
        try
        {
          Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
          PreparedStatement stmt = conn.prepareStatement(
                  "SELECT * FROM cards WHERE ownedby = ?;"
          );
          stmt.setString(1, user);
          ResultSet res = stmt.executeQuery();

          StringBuilder userData = new StringBuilder();
          while(res.next())
          {
              userData.append("{\"id\":" + "\"").append(res.getString(1)).append("\",")
                      .append("\"card_name\":" + "\"").append(res.getString(2)).append("\",")
                      .append("\"element_type\":" + "\"").append(res.getString(3)).append("\",")
                      .append("\"damage\":" + "\"").append(res.getInt(4)).append("\",")
                      .append("\"username\":" + "\"").append(res.getString(5)).append("\",")
                      .append("\"package_num\":" + "\"").append(res.getInt(6)).append("\"}\n")
              ;
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
    public String getCard(String ID) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM cards WHERE id = ?;"
            );
            stmt.setString(1, ID);
            ResultSet res = stmt.executeQuery();

            StringBuilder userData = new StringBuilder();
            if(res.next())
            {
                userData.append("{\"id\":" + "\"").append(res.getString(1)).append("\",")
                        .append("\"card_name\":" + "\"").append(res.getString(2)).append("\",")
                        .append("\"element_type\":" + "\"").append(res.getString(3)).append("\",")
                        .append("\"damage\":" + "\"").append(res.getInt(4)).append("\",")
                        .append("\"username\":" + "\"").append(res.getString(5)).append("\",")
                        .append("\"package_num\":" + "\"").append(res.getInt(6)).append("\"}\n")
                ;
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
    public String getOwner(String ID) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ownedby FROM cards WHERE id = ?;"
            );
            stmt.setString(1, ID);
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
