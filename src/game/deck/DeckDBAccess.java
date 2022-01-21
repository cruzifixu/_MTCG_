package game.deck;

import org.codehaus.jackson.JsonNode;

import java.sql.SQLException;

public interface DeckDBAccess {
    boolean addUserDeck(JsonNode node);
    String getUserDeck(String username);
    boolean setUserDeck(String id, String username, int num) throws SQLException;
}
