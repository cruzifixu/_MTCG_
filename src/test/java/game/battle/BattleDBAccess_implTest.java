package game.battle;

import game.user.UserDBAccess_impl;
import game.user.User_impl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleDBAccess_implTest {

    @Test
    void changePlayerStatusTrue() {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        BattleDBAccess_impl BattleDBAccess = new BattleDBAccess_impl();
        User_impl user = new User_impl("battleTesterOne", "PassWorthy", 20, "TesterOne", "me playin...", ">.<");
        assertTrue(UserDBAccess.addUser(user));
        assertTrue(BattleDBAccess.ChangePlayerStatus("battleTesterOne", "waiting..."));
    }

    @Test
    void changePlayerStatusFalse() {
        BattleDBAccess_impl BattleDBAccess = new BattleDBAccess_impl();
        assertFalse(BattleDBAccess.ChangePlayerStatus("NonExistent", "welp"));
    }

    @Test
    void checkAllPlayerStatusOneFound() {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        BattleDBAccess_impl BattleDBAccess = new BattleDBAccess_impl();
        User_impl user = new User_impl("PlayerCheckUser", "PassWorthy", 20, "PlayerCheck", "ready for battle...", ">.<");
        User_impl usertwo = new User_impl("PlayerCheckUserTwo", "PassWorthy", 20, "PlayerCheck", "ready for battle...", ">.<");
        assertTrue(UserDBAccess.addUser(user));
        assertTrue(UserDBAccess.addUser(usertwo));
        assertTrue(BattleDBAccess.ChangePlayerStatus("PlayerCheckUser", "ready for battle..."));
        assertTrue(BattleDBAccess.ChangePlayerStatus("PlayerCheckUserTwo", "ready for battle..."));
        assertEquals("PlayerCheckUserTwo", BattleDBAccess.checkAllPlayerStatus("PlayerCheckUser"));
    }

    @Test
    void checkAllPlayerStatusOneNoneFound() {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        BattleDBAccess_impl BattleDBAccess = new BattleDBAccess_impl();
        User_impl user = new User_impl("NoPlayerCheckUser", "PassWorthy", 20, "PlayerCheck", "ready for battle...", ">.<");
        assertTrue(UserDBAccess.addUser(user));
        assertTrue(BattleDBAccess.ChangePlayerStatus("NoPlayerCheckUser", "ready for battle..."));
        assertEquals("", BattleDBAccess.checkAllPlayerStatus("NoPlayerCheckUser"));


    }
}