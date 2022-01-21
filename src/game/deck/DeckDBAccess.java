package game.deck;

import org.codehaus.jackson.JsonNode;

public interface DeckDBAccess {
    boolean addUserDeck(String username);
    String getUserDeck(String username);
    boolean setUserDeck(JsonNode node);
}
