package game.user;

import lombok.Getter;
import lombok.Setter;

public class User_impl implements User {
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

    public User_impl(String user, String psw, int coins, String nickname, String bio, String image)
    {
        this.username = user;
        this.password = psw;
        this.coins = coins;
        this.nickname = nickname;
        this.bio = bio;
        this.image = image;
    }
}
