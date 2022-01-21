package game.user;

import org.codehaus.jackson.JsonNode;

import java.sql.SQLException;

public interface UserDBAccess {
    String getUser(String user) throws SQLException;
    String addUser(JsonNode node);
    String loginUser(JsonNode node) throws SQLException;
}
