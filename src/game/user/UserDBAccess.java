package game.user;

import org.codehaus.jackson.JsonNode;

import java.sql.SQLException;

public interface UserDBAccess {
    String getUser(String user) throws SQLException;
    String getUserWithoutSenInfo(String user) throws SQLException;
    boolean EditUserData(User_impl user) throws SQLException;
    String addUser(User_impl user);
    String loginUser(JsonNode node) throws SQLException;
    String getStats(String user);
    String getScore();
}
