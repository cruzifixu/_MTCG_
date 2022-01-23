package game.rest_api;

import game.card.CardsDBAccess_impl;
import game.card.Monstercard;
import game.card.Spellcard;
import game.deck.DeckDBAccess_impl;
import game.user.UserDBAccess_impl;
import game.user.user_impl;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;


public class HttpRequestHandler_Impl implements HttpRequestHandler
{
    @Setter
    private HttpRequest_Impl req;
    @Getter @Setter
    private int count = 1;

    // --- DB ACCESS -- //
    @Getter
    UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();
    @Getter
    CardsDBAccess_impl CardsDBAccess = new CardsDBAccess_impl();
    @Getter
    DeckDBAccess_impl DeckDBAccess = new DeckDBAccess_impl();

    public HttpRequestHandler_Impl(HttpRequest_Impl req)
    {
        this.req = req;
    }

    @Override
    public HttpResponse_Impl handle() throws IOException, SQLException {
        HttpResponse_Impl response = null;
        JsonNode node = null;
        ObjectMapper mapper = new ObjectMapper();
        String requestContent = req.getHttpsContent();
        if(requestContent != null && !requestContent.equals("")) { node = mapper.readTree(requestContent); }

        switch(req.getMethod())
        {
            case "POST" -> response = handlePOST(node);
            case "GET" -> response = handleGET();
            case "PUT" -> response =  handlePUT(node);
            case "DELETE" -> System.out.println("DELETE");
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
                // --- if user was created it will get user from db again to check
                // if not null and true- success
                user_impl user = new user_impl(node.get("Username").getValueAsText(), node.get("Password").getValueAsText(), 20, "", "", "");
                if(getUserDBAccess().addUser(user) != null && getDeckDBAccess().addUserDeck(user))
                { return new HttpResponse_Impl(200, "user created"); }
                else { return new HttpResponse_Impl(400, "user not created"); }
            }
            case "sessions" -> {
                // --- getting user from db
                res = getUserDBAccess().loginUser(node);
                // check if password matches entered password
                System.out.println(res);
                if(res != null && !res.equals("")) { return new HttpResponse_Impl(200, "user logged in"); }
                else { return new HttpResponse_Impl(400, "user not logged in, wrong password or username"); }
            }
            case "packages" -> {
                // --- if adding packages was a success true
                if(!getCardInfo(node)) { return new HttpResponse_Impl(500, "package not submitted"); }
                else { return new HttpResponse_Impl(200, "package created"); }
            }
            case "transactions/packages" -> {
                // --- check for if user is authorized first before they can aquire packages
                // true if authorized
                if(getCardsDBAccess().acquirePackage(req.getAuthorizedUser()))
                { return new HttpResponse_Impl(200, "package bought"); }
                else { return new HttpResponse_Impl(400, "not enough money"); }
            }
        }
        return null;
    }

    @Override
    public HttpResponse_Impl handleGET()
    {
        System.out.println(req.getPath());
        switch(req.getPath())
        {
            case "cards" -> {
                // --- check if user is authorized
                String authUser = req.getAuthorizedUser();
                // if null or empty = unauthorized to get cards
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                // --- get cards from db
                String res = getCardsDBAccess().showCards(authUser);
                // if result is not null - user owns cards - prints cards
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(400, "no cards shown"); }
            }
            case "deck" -> {
                // --- check if user is authorized
                String authUser = req.getAuthorizedUser();
                // if null or empty = unauthorized to get deck
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                // --- get deck from db
                String res = getDeckDBAccess().getUserDeck(authUser, "json");
                if(!res.contains("NULL")) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(400, "deck unconfigured"); }
            }
            case "deck?format=plain" -> {
                // --- check if user is authorized
                String authUser = req.getAuthorizedUser();
                // if null or empty = unauthorized to get deck
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                // --- get deck from db
                String res = getDeckDBAccess().getUserDeck(authUser, "plain");
                if(!res.contains("NULL")) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(400, "deck unconfigured"); }
            }
            case "users" -> {
                String authUser = req.getAuthorizedUser();
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                if(!authUser.equals(req.getSecondLevelPath()))
                { return new HttpResponse_Impl(403, "forbidden"); }
                String res = getUserDBAccess().getUserWithoutSenInfo(authUser);
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(404, "user not found"); }
            }
            case "stats" -> {
                String authUser = req.getAuthorizedUser();
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                String res = getUserDBAccess().getStats(authUser);
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(404, "user not found"); }
            }
            case "score" -> {
                String res = getUserDBAccess().getScore();
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else  { return new HttpResponse_Impl(404, "no users data found"); }
            }
        }

        return null;
    }

    @Override
    public HttpResponse_Impl handlePUT(JsonNode node) throws SQLException, IOException {
        boolean success;
        switch(req.getPath()) {
            case "deck" -> {
                String authUser = req.getAuthorizedUser();
                // if null = not authorized to set deck
                if (authUser == null) {
                    return new HttpResponse_Impl(401, "not authorized");
                }

                System.out.println("size "+node.size());

                if(node.size() < 4)
                { return new HttpResponse_Impl(400, "not enough cards to set deck"); }

                for (int i = 1; i < 5; i++) {
                    // --- if card isnt owned by auth user
                    if(!getCardsDBAccess().getCard(node.get(i-1).getValueAsText()).contains(authUser))
                    { return new HttpResponse_Impl(403, "not card owner"); }
                    ObjectMapper mapper = new ObjectMapper();

                    JsonNode oneCard = mapper.readTree(getCardsDBAccess().getCard(node.get(i-1).getValueAsText()));

                    if (!getDeckDBAccess().setUserDeck(oneCard, i))
                    { return new HttpResponse_Impl(400, "deck was not set"); }
                }
                return new HttpResponse_Impl(200, "deck set");
            }
            case "users" -> {
                String authUser = req.getAuthorizedUser();
                if(authUser == null) { return new HttpResponse_Impl(401, "not authorized"); }
                if(!authUser.equals(req.getSecondLevelPath()))
                { return new HttpResponse_Impl(403, "forbidden"); }
                // public user_impl(String user, String psw, int coins, String nickname, String bio, String image)
                user_impl user = new user_impl(authUser, "", 0,
                        node.get("Name").getValueAsText(), node.get("Bio").getTextValue(), node.get("Image").getTextValue());
                success = getUserDBAccess().EditUserData(user);
                if(success) { return new HttpResponse_Impl(200, "user data updated"); }
                else { return new HttpResponse_Impl(400, "user data not updated"); }
            }
        }

        return null;
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