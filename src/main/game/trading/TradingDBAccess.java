package game.trading;

public interface TradingDBAccess {
    String getTrades();
    boolean createTrade(Trading_impl trade);
    String getID(String trading_id);
    boolean deleteTrade(String id, String card_id);
    boolean UpdateOwner(String NewOwner, String OldOwner, String id);
}
