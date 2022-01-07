package game.user;

import java.sql.SQLException;

public interface UserDBAccess_Interface {
    String getUser(String user) throws SQLException;
    String addUser(String username, String psw);
}
