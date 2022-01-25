package game.rest_api;

import game.battle.BattleDBAccess_impl;
import game.battle.BattleFightLogger;
import game.battle.Battle_impl;
import game.card.CardsDBAccess_impl;
import game.card.Monstercard;
import game.card.Spellcard;
import game.deck.DeckDBAccess_impl;
import game.trading.TradingDBAccess_impl;
import game.trading.Trading_impl;
import game.user.UserDBAccess_impl;
import game.user.User_impl;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.SQLException;


public class HttpRequestHandler_Impl implements HttpRequestHandler
{
    @Setter
    private HttpRequest_Impl req;
    @Getter @Setter
    private int count = 1;
    @Getter
    private String authUser;

    // --- DB ACCESS -- //
    @Getter
    UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
    @Getter
    CardsDBAccess_impl CardsDBAccess = new CardsDBAccess_impl();
    @Getter
    DeckDBAccess_impl DeckDBAccess = new DeckDBAccess_impl();
    @Getter
    TradingDBAccess_impl TradingDBAccess = new TradingDBAccess_impl();
    @Getter
    BattleDBAccess_impl BattleDBAccess = new BattleDBAccess_impl();

    public HttpRequestHandler_Impl(HttpRequest_Impl req)
    {
        this.req = req;
    }
    public HttpRequestHandler_Impl() {}

    @Override
    public HttpResponse_Impl handle() throws IOException, SQLException {
        HttpResponse_Impl response = null;
        JsonNode node = null;
        ObjectMapper mapper = new ObjectMapper();
        String requestContent = req.getHttpsContent();
        if(requestContent != null && !requestContent.equals("")) { node = mapper.readTree(requestContent); }

        // check auth
        if(!(req.getMethod().equals("POST") && req.getPath().equals("users"))
                &&
                !(req.getMethod().equals("POST") && req.getPath().equals("sessions")))
        {
            authUser = req.getAuthorizedUser();
        }

        switch(req.getMethod())
        {
            case "POST" -> response = handlePOST(node);
            case "GET" -> response = handleGET();
            case "PUT" -> response = handlePUT(node);
            case "DELETE" -> response = handleDELETE(node);
        }
        return response;
    }

    @Override
    public HttpResponse_Impl handlePOST(JsonNode node) throws SQLException {
        String res;

        switch(req.getPath())
        {
            case "users" -> {
                // --- getting username and password of node to add to db
                // --- if User was created it will get User from db again to check
                // if not null and true- success
                User_impl user = new User_impl(node.get("Username").getValueAsText(), node.get("Password").getValueAsText(), 20, "", "", "");
                if(getUserDBAccess().addUser(user))
                { return new HttpResponse_Impl(200, "User created"); }
                else { return new HttpResponse_Impl(400, "User not created"); }
            }
            case "sessions" -> {
                // --- getting User from db
                res = getUserDBAccess().loginUser(node);
                if(res != null && !res.equals("")) { return new HttpResponse_Impl(200, "User logged in"); }
                else { return new HttpResponse_Impl(400, "User not logged in, wrong password or username"); }
            }
            case "packages" -> {
                // --- if adding packages was a success true
                if(!getCardInfo(node)) { return new HttpResponse_Impl(500, "package not submitted"); }
                else { return new HttpResponse_Impl(200, "package created"); }
            }
            case "transactions" -> {
                // true if authorized
                if(getCardsDBAccess().acquirePackage(req.getAuthorizedUser()))
                { return new HttpResponse_Impl(200, "package bought"); }
                else { return new HttpResponse_Impl(400, "not enough money"); }
            }
            case "tradings" -> {
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                if(req.getSecondLevelPath() != null)
                {
                    String oldOwner = getCardsDBAccess().getOwner(getTradingDBAccess().getID(req.getSecondLevelPath()));
                    // --- trading with self not allowed
                    if(oldOwner.equals(authUser))
                    { return new HttpResponse_Impl(403, "trades with yourself forbidden"); }
                            // card to trade                    // trading id
                    String card_id = getTradingDBAccess().getID(req.getSecondLevelPath());
                                                                            // card from auth User
                    if(getTradingDBAccess().UpdateOwner(oldOwner, authUser, node.getValueAsText())
                            && getTradingDBAccess().UpdateOwner(authUser, oldOwner, card_id)
                            && getTradingDBAccess().deleteTrade(req.getSecondLevelPath(), card_id)
                    )
                    { return new HttpResponse_Impl(200, "trade made"); }
                    else { return new HttpResponse_Impl(400, "no trades made"); }
                }
                else
                {
                    // --- new trade
                    Trading_impl trade = new Trading_impl(node.get("Id").getValueAsText(),
                            node.get("CardToTrade").getValueAsText(),
                            node.get("Type").getValueAsText(), node.get("MinimumDamage").getValueAsInt());
                    if(getTradingDBAccess().createTrade(trade))
                    { return new HttpResponse_Impl(200, "trade created"); }
                    else { return new HttpResponse_Impl(400, "no trade created"); }
                }
            }
            case "battles" -> {
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                getBattleDBAccess().ChangePlayerStatus(authUser, "ready for battle...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // --- get other user to battle with
                String otherUser = getBattleDBAccess().checkAllPlayerStatus(authUser);
                if(otherUser != null && !otherUser.equals(""))
                {
                    Battle_impl Battle = new Battle_impl(authUser, otherUser,
                            getDeckDBAccess().getUserDeckAsCards(authUser), getDeckDBAccess().getUserDeckAsCards(otherUser),
                            getUserDBAccess().getStatsAsArray(authUser), getUserDBAccess().getStatsAsArray(otherUser));
                    boolean success = Battle.BattleFight();
                    if(!success) { new HttpResponse_Impl(400,  "battle could not finish"); }
                    BattleFightLogger logger = BattleFightLogger.getInstance();
                    return new HttpResponse_Impl(200,  logger.getLog().toString());
                }
                return new HttpResponse_Impl(404, "no battle found");
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public HttpResponse_Impl handleGET()
    {
        switch(req.getPath())
        {
            case "cards" -> {
                // --- get cards from db
                String res = getCardsDBAccess().showCards(authUser);
                // if result is not null - User owns cards - prints cards
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(400, "no cards shown"); }
            }
            case "deck" -> {
                // if null or empty = unauthorized to get deck
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                // --- get deck from db
                String res = getDeckDBAccess().getUserDeck(authUser, "json");
                if(res != null && !res.equals("")) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(400, "deck unconfigured"); }
            }
            case "deck?format=plain" -> {
                // if null or empty = unauthorized to get deck
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                // --- get deck from db
                String res = getDeckDBAccess().getUserDeck(authUser, "plain");
                if(res != null && !res.equals("")) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(400, "deck unconfigured"); }
            }
            case "users" -> {
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                if(!authUser.equals(req.getSecondLevelPath()))
                { return new HttpResponse_Impl(403, "forbidden"); }
                String res = getUserDBAccess().getUserWithoutSenInfo(authUser);
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(404, "User not found"); }
            }
            case "stats" -> {
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                String res = getUserDBAccess().getStats(authUser);
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(404, "User not found"); }
            }
            case "score" -> {
                String res = getUserDBAccess().getScore();
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else  { return new HttpResponse_Impl(404, "no users data found"); }
            }
            case "tradings" -> {
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                String res = getTradingDBAccess().getTrades();
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(404, "no trades found"); }
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public HttpResponse_Impl handlePUT(JsonNode node) throws SQLException {
        boolean success;
        switch(req.getPath()) {
            case "deck" -> {
                // if null = not authorized to set deck
                if (authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }

                if(node.size() < 4 || node.size() > 4)
                { return new HttpResponse_Impl(400, "not enough cards to set deck"); }

                for (int i = 0; i < 4; i++) {
                    // --- if card is not owned by auth User
                    if(!getCardsDBAccess().getCard(node.get(i).getValueAsText()).contains(authUser))
                    { return new HttpResponse_Impl(403, "not card owner"); }

                    if (!getDeckDBAccess().setUserDeck(node.get(i).getValueAsText()))
                    { return new HttpResponse_Impl(400, "deck was not set"); }
                }
                return new HttpResponse_Impl(200, "deck set");
            }
            case "users" -> {
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                if(!authUser.equals(req.getSecondLevelPath()))
                { return new HttpResponse_Impl(403, "forbidden"); }
                User_impl user = new User_impl(authUser, "", 0,
                        node.get("Name").getValueAsText(), node.get("Bio").getTextValue(), node.get("Image").getTextValue());
                success = getUserDBAccess().EditUserData(user);
                if(success) { return new HttpResponse_Impl(200, "User data updated"); }
                else { return new HttpResponse_Impl(400, "User data not updated"); }
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public HttpResponse_Impl handleDELETE(JsonNode node) {
        switch(req.getPath())
        {
            case "tradings" -> {
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                if(getTradingDBAccess().deleteTrade(req.getSecondLevelPath(), getTradingDBAccess().getID(req.getSecondLevelPath())))
                { return new HttpResponse_Impl(200, "trade successfully deleted"); }
                else { return new HttpResponse_Impl(400, "trade not deleted"); }
            }
            default -> {
                return new HttpResponse_Impl(400, "no such function");
            }
        }
    }


    @Override
    public boolean getCardInfo(JsonNode node) throws SQLException {

        String category_type, element_type;
        Spellcard spellcard;
        Monstercard monstercard;

        int package_num = getCardsDBAccess().createPackage();
        for(int i = 0; i < 5; i++)
        {
            if(node.get(i).get("Name").getValueAsText().contains("Fire"))
            { element_type = "Fire"; }
            else if(node.get(i).get("Name").getValueAsText().contains("Water"))
            { element_type = "Water"; }
            else
            { element_type = "Regular"; }
            if(node.get(i).get("Name").getValueAsText().contains("Spell"))
            { category_type = "Spell"; }
            else
            { category_type = "Monster"; }

            if(category_type.equals("Spell"))
            {
                spellcard = new Spellcard(node.get(i).get("Id").getValueAsText(), node.get(i).get("Name").getValueAsText(),
                        element_type, Double.parseDouble(node.get(i).get("Damage").getValueAsText()), "", package_num);
                getCardsDBAccess().addCardToPackage(spellcard);
            }
            else
            {
                monstercard = new Monstercard(node.get(i).get("Id").getValueAsText(), node.get(i).get("Name").getValueAsText(),
                        element_type, Double.parseDouble(node.get(i).get("Damage").getValueAsText()), "", package_num);
                getCardsDBAccess().addCardToPackage(monstercard);
            }
        }
        return true;
    }
}