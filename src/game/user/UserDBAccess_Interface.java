package game.user;

import java.sql.SQLException;

public interface UserDBAccess_Interface {
    public String getUser(int ID) throws SQLException;
    public String addUser(String username, String psw);
}
