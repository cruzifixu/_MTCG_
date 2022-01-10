package game.card;

import java.util.ArrayList;

public interface CardsDBAccess
{
    boolean createPackage(ArrayList<String> oneCard, int count);
    boolean checkBalance(String username);
    boolean acquirePackage(String username);

}
