package game.deck;
import game.card.Cards_impl;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DeckDBAccess {
    String getUserDeck(String username, String format);
    ArrayList<Cards_impl> getUserDeckAsCards(String username);
    boolean setUserDeck(String id) throws SQLException;
}
