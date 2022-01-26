# Design und Lessons Learned


## Persistence
Das erste Design für das Projekt war noch ziemlich zerstreut und nicht Java basiert. Der Fokus lag eher auf Methoden, die ohne jegliche Persistent DB laufen würden.
Also die Information über den User, die Karten und weiteres würden nur als Objekte gespeichert und so genutzt werden, 
was sich gleich auf den ersten Fehler bezieht, nämlich, dass dies nicht persistent ist und die Daten danach endgültig gelöscht werden.

## PrintWriter and Empty Reply
Die meiste Zeit ging in das PrintWriter Problem hinein. Der Writer wurde immerzu neu gesetzt und konnte somit keinen Reply an den Server schicken. Ebenso hab ich den Fehler gemacht
nicht in Betracht gezogen zu haben, den body, also die Reply vorher zu setzen, womit die Reply stets empty war. Dies hat insgesamt 2-3 Tage gekostet.

## Redudant
Im Laufe des Projekts hatte ich die Angewohnheit komplizierter zu denken als es eigentlich ist.
Das beste Beispiel hier wäre das Deck. Die Karten für das Deck in eine eigene Tabelle zu hinterlegen war müsamer als die eigentlich sehr einfache Lösung, nämlich eine weitere Spalte
hinzuzufügen - ein Boolean, ob die Karte im Deck ist oder nicht. Anders hab ich mir sonst die Mühe machen müssen, die wichtigen Informationen einer Karte stets aus
der Kartentabelle zu holen und dieselbe Information in die Deck Tabelle hinzuzufügen - sehr redundant.

## Datenbank
Der Aufbau der Datenbank hat auch einige Stunden gekostet, da man zu Beginn die zu dem Zeitpunkt offensichtlichen Spalten einfügt. Mühsa, wurde es also dann die restlichen
Spalten nachträglich hinzuzufügen um die Funktionalität zu unterstützen. Ich selbst habe keine einfache Methode gefunden, eine Tabelle zu bearbeiten - Spalten zu verschieben.


# unit test design


## Battle

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
 


# time spent

