package game.trading;

import game.db.DatabaseConn_impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradingDBAccess_impl implements TradingDBAccess
{
    @Override
    public String getTrades() {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM trade;"
            );
            ResultSet res = stmt.executeQuery();

            StringBuilder userData = new StringBuilder();
            while(res.next())
            {
                userData.append("{\"id\":"+ "\"" +res.getString(1)+ "\",")
                        .append("\"card_to_trade\":" + "\"" + res.getString(2)+ "\",")
                        .append("\"type\":" + "\"" + res.getString(3)+ "\",")
                        .append("\"min_damage\":"+ "\"" + res.getInt(4)+ "\"}\n")
                ;
            }

            res.close();
            stmt.close();
            conn.close();
            if(!userData.isEmpty()) userData.toString();
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean createTrade(Trading_impl trade) {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO trade (id, card_to_trade, type, min_damage) VALUES (?, ?, ?, ?);"
            );
            stmt.setString(1, trade.getId());
            stmt.setString(2, trade.getCard_to_trade());
            stmt.setString(3, trade.getType());
            stmt.setInt(4, trade.getMin_damage());
            int affectedRows = stmt.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                stmt.close();
                conn.close();
                return false;
            }
            stmt.close();

            PreparedStatement stmt2 = conn.prepareStatement(
                    "UPDATE cards SET for_trading = ? WHERE id = ?;"
            );
            stmt2.setBoolean(1, true);
            stmt2.setString(2, trade.getCard_to_trade());

            affectedRows = stmt2.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                stmt2.close();
                conn.close();
                return false;
            }
            stmt2.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getID(String trading_id) {
        try
        {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT card_to_trade FROM trade WHERE id = ?;"
            );
            stmt.setString(1, trading_id);
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

    @Override
    public boolean deleteTrade(String trading_id, String card_id) {
        try {
            Connection conn = DatabaseConn_impl.getInstance().getConn();
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM trade WHERE id = ?;"
            );
            stmt.setString(1, trading_id);
            int affectedRows = stmt.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                stmt.close();
                conn.close();
                return false;
            }
            stmt.close();
            PreparedStatement stmt2 = conn.prepareStatement(
                    "UPDATE cards SET for_trading = ? WHERE id = ?;"
            );
            stmt2.setBoolean(1, false);
            stmt2.setString(2, card_id);

            affectedRows = stmt2.executeUpdate();

            // couldn't get executed
            if (affectedRows == 0) {
                stmt2.close();
                conn.close();
                return false;
            }
            stmt2.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean UpdateOwner(String NewOwner, String OldOwner, String id) {
        Connection conn = DatabaseConn_impl.getInstance().getConn();
        try {
            // ----- PREPARED STATEMENT ----- //
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE cards SET ownedby = ?, for_trading = ? WHERE id = ?;"
            );
            stmt.setString(1, NewOwner);
            stmt.setBoolean(2, false);
            stmt.setString(3, id);

            int affectedRows = stmt.executeUpdate();

            if(affectedRows == 0)
            {
                stmt.close();
                conn.close();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
