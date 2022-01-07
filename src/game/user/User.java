package game.user;
import lombok.Getter;

public class User implements user_Interface
{
    @Getter
    int ID;
    @Getter
    String username;
    @Getter
    String password;
    @Getter
    int coins;

    @Override
    public void ok()
    {

    }

    /*
    @Override
    public boolean buyPackages(){
        return true;
    }

    @Override
    public void setCoins(int coins, String user) {
        int c = getCoins(user) + coins; // add coins to curr points
        Connection conn = DatabaseConn.getInstance().getConn();
        try {
            PreparedStatement sta = conn.prepareStatement(
                    "UPDATE user SET coins = ?;"
            );
            sta.setString(1, Integer.toString(c));
            sta.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCoins(String user){
        int coins = 0;
        Connection conn = DatabaseConn.getInstance().getConn();
        try {
            PreparedStatement sta = conn.prepareStatement(
                    "SELECT coins FROM user WHERE username = ?;"
            );
            sta.setString(1, user);
            ResultSet res = sta.executeQuery();
            coins = res.getInt(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coins;
    }

     */
}
