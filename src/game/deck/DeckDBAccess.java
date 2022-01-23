package game.deck;

import game.card.Cards_impl;
import game.user.user_impl;
import org.codehaus.jackson.JsonNode;

import java.sql.SQLException;

public interface DeckDBAccess {
    boolean addUserDeck(user_impl user);
    String getUserDeck(String username, String format);
    boolean setUserDeck(String id) throws SQLException;
}
