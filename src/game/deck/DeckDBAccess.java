package game.deck;

import game.user.User_impl;

import java.sql.SQLException;

public interface DeckDBAccess {
    boolean addUserDeck(User_impl user);
    String getUserDeck(String username, String format);
    boolean setUserDeck(String id) throws SQLException;
}
