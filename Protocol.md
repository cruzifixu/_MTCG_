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
 
Einige Stets der Tests sind wie diese, wo einmal auf die Funktionalität als auch auf das erwartete Fehlschlagen des Requests, sowie hier den Player Status, für
den Battle zu ändern

## Card


    @Test
    void addCardToPackage() {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        Cards_impl cards = new Monstercard("6cd85277-4590-49d4-b0cf-ba0a921faad1", "WaterGoblin", "Water", 15, "leeiam", 23);
        assertDoesNotThrow(()->assertTrue(CardDBAccess.addCardToPackage(cards)));
    }
    @Test
    void getCard()
    {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        assertEquals("{\"id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad1\",\"card_name\":\"WaterGoblin\",\"element_type\":\"Water\",\"damage\":\"15\",\"username\":\"null\",\"package_num\":\"23\"}\n",
                CardDBAccess.getCard("6cd85277-4590-49d4-b0cf-ba0a921faad1"));
    }
    @Test
    void getNoCard()
    {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        assertEquals("", CardDBAccess.getCard("6cd77756-4736-56d4-b0cf-ba0a921faad5"));
    }

Die Card Tests testen in diesem Fall, ob eine Karte in die Datenbank eingeführt werden kann als auch, dass es nichts wirft. Nach dem dies erfolgreich ausgeführt wird,
wird diese aus der Datenbank erfolgreich geholt sowie ein Test, dass nichtexistierende Karten nicht gefunden werden können

    @Test
    void setUserDeck() {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        DeckDBAccess_impl DeckDBAccess = new DeckDBAccess_impl();
        Cards_impl cards = new Monstercard("6cd85277-4590-49d4-b0cf-ba0a921faad2", "FireGoblin", "Fire", 16, "", 24);
        assertDoesNotThrow(()->assertTrue(CardDBAccess.addCardToPackage(cards)));
        assertDoesNotThrow(()->assertTrue(DeckDBAccess.setUserDeck("6cd85277-4590-49d4-b0cf-ba0a921faad2")));
    }
    @Test
    void setUserDeckNotWorking()
    {
        DeckDBAccess_impl DeckDBAccess = new DeckDBAccess_impl();
        assertDoesNotThrow(()->assertFalse(DeckDBAccess.setUserDeck("6cd85277-4590-49d4-b0cf-ba0a921faad3")));
    }

Die Deck Klasse beinhaltet hauptsächlich das Setzen und Holen des Decks in der Datenbank und hier wird somit das Setten eines Decks geprüft.
    
    @Test
    void getCardInfo() {
        HttpRequestHandler_Impl handler = new HttpRequestHandler_Impl();
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(()->assertTrue(handler.getCardInfo(mapper.readTree("[{\"Id\":\"67f9048f-99b8-4ae4-b866-d8008d00c53d\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"Name\":\"RegularSpell\", \"Damage\": 50.0}, {\"Id\":\"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"Name\":\"Knight\", \"Damage\": 20.0}, {\"Id\":\"02a9c76e-b17d-427f-9240-2dd49b0d3bfd\", \"Name\":\"RegularSpell\", \"Damage\": 45.0}, {\"Id\":\"2508bf5c-20d7-43b4-8c77-bc677decadef\", \"Name\":\"FireElf\", \"Damage\": 25.0}]"))));
    }
    @Test
    void authorizeRequest()
    {
        HttpRequest_Impl req = new HttpRequest_Impl();
        req.setToken("Authorization: Basic leeiam-mtcgToken");
        req.authorizeRequest();
        assertEquals("leeiam", req.getAuthorizedUser());
    }

Da in der Handler Klasse grundsätzlich nur die verschiedenen Request behandelt werden, wurden die Funktionen, die diese Request brauchen - ohne Datenbankverbindung, sondern
eher weiterverarbeiten von Strings.

    @Test
    void createTrade() {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        Cards_impl cards = new Monstercard("6cd85277-4590-49d4-b0cf-ba0a921faad6", "WaterGoblin", "Water", 15, "", 23);
        assertDoesNotThrow(()->assertTrue(CardDBAccess.addCardToPackage(cards)));
        TradingDBAccess_impl TradingDBAccess = new TradingDBAccess_impl();
        Trading_impl trade = new Trading_impl("6cd85277-4590-49d4-b0cf-ba0a921faad5", "6cd85277-4590-49d4-b0cf-ba0a921faad6", "Water", 15);
        assertTrue(TradingDBAccess.createTrade(trade));
    }
    // aufeinander bauend
    @Test
    void deleteTrade()
    {
        TradingDBAccess_impl TradingDBAccess = new TradingDBAccess_impl();
        assertTrue(TradingDBAccess.deleteTrade("6cd85277-4590-49d4-b0cf-ba0a921faad5", "6cd85277-4590-49d4-b0cf-ba0a921faad6"));
    }
    // aufeinander bauend
    @Test
    void getID()
    {
        TradingDBAccess_impl TradingDBAccess = new TradingDBAccess_impl();
        assertEquals("", TradingDBAccess.getID("6cd85277-4590-49d4-b0cf-ba0a921faad5"));
    }

Die Trading Tests sind hier aufeinanderbauend und müssten auch nacheinander getestet werden, damit die richtige Funktion getestet wird.

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

Die User Klasse bildet die Basis des ganzen und hier werden die ersten ausgeführten Funktionen getestet als auch Statistiken, die für den weiteren Verlauf
wichtig sind.

# time spent

Das eigentliche Projekt habe ich in den letzten 3 Wochen geschrieben, wobei das größte Hinderniss ehrlich gesagt der PrintWriter gestellt hat, womit ich mich am längsten auseinander setzen musste. In den darauffolgenden Tagen habe ich stückchenweise aufbauend zum Curl Script weiter an dem Code gearbeitet und als die Basis mit den User Daten stand, ging der Rest ziemlich flüssig, da viele dtenbankbezogenen Elemente sehr ähnlich aufgebaut waren. Es stellten sich nur mehr noch ein paar Hindernisse, da ich kleinere Fehler bei der Rechtschreibung oder aufgrund eines Tippfehlers gemacht habe, aber sonst lief es ganz okay. 
