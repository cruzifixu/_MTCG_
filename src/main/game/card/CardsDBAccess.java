package game.card;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CardsDBAccess
{
    int createPackage();
    boolean addCardToPackage(Cards_impl card) throws SQLException;
    boolean checkBalance(String username);
    boolean acquirePackage(String username);
    boolean addTransactionEntry(String username, String package_id);
    String showTransactionHistory(String username);
    String showCards(String user);
    String getCard(String ID);
    String getOwner(String ID);
}
