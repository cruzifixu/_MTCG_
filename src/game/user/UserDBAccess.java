package game.user;

import java.sql.SQLException;

public interface UserDBAccess {
    String getUser(String user) throws SQLException;
    String addUser(String username, String psw);
}
