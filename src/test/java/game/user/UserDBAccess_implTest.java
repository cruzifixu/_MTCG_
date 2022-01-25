package game.user;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserDBAccess_implTest {
    @Test
    void addUser()
    {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        User_impl user = new User_impl("chezcruz", "PassWorthy", 20, "cheezy", "me playin...", ">.<");
        assertTrue(UserDBAccess.addUser(user));
    }
    @Test
    void EditUserDataTrue()
    {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        User_impl user = new User_impl("chezcruz", "PassWorthy", 20, "cheezier", "me codin...", "#>.<#");
        assertDoesNotThrow(()->assertTrue(UserDBAccess.EditUserData(user)));
    }
    @Test
    void EditUserDataAssertionFailed()
    {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        User_impl userAssert = new User_impl("leeiam", "NotSoPassin", 20, "lees", "me goofin...", "##");
        assertThrows(AssertionFailedError.class, ()->assertTrue(UserDBAccess.EditUserData(userAssert)));
    }
    @Test
    void getStatsBeforeUpdate()
    {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        User_impl user = new User_impl("PlayerStats", "NotSoPassin", 20, "lees", "me goofin...", "##");
        assertTrue(UserDBAccess.addUser(user));
        assertEquals("{\"wins\":\"0\", \"lost\":\"0\", \"elo\":\"100\"}", UserDBAccess.getStats("PlayerStats"));
    }
    @Test
    void UpdateStats()
    {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        ArrayList<Integer> stats = new ArrayList<>();
        stats.add(1);
        stats.add(2);
        stats.add(300);
        assertTrue(UserDBAccess.UpdateStats("PlayerStats", stats));
    }
    @Test
    void getStatsAfterUpdate()
    {
        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
        assertEquals("{\"wins\":\"1\", \"lost\":\"2\", \"elo\":\"300\"}", UserDBAccess.getStats("PlayerStats"));
    }
}