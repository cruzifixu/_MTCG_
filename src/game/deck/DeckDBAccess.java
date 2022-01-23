package game.deck;

import game.user.user_impl;
import org.codehaus.jackson.JsonNode;

import java.sql.SQLException;

public interface DeckDBAccess {
    boolean addUserDeck(user_impl user);
    String getUserDeck(String username, String format);
    boolean setUserDeck(JsonNode node, int num) throws SQLException;
}
