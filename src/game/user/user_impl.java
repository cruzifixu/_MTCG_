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
    @Getter @Setter
    String nickname;
    @Getter @Setter
    String bio;
    @Getter @Setter
    String image;

    public user_impl(String user, String psw, int coins, String nickname, String bio, String image)
    {
        this.username = user;
        this.password = psw;
        this.coins = coins;
        this.nickname = nickname;
        this.bio = bio;
        this.image = image;
    }
}
