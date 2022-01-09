package game.user;

import lombok.Getter;
import lombok.Setter;

public class user_impl implements user{
    @Getter @Setter
    String username;
    @Getter @Setter
    String password;
    @Getter @Setter
    int coins;

    user_impl(String user, String psw, int coins)
    {
        this.username = user;
        this.password = psw;
        this.coins = coins;
    }
}
