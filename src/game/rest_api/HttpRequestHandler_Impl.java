package game.rest_api;

import game.card.CardsDBAccess_impl;
import game.user.UserDBAccess_impl;
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

    public HttpRequestHandler_Impl(HttpRequest_Impl req)
    {
        this.req = req;
    }

    @Override
    public HttpResponse_Impl handle() throws IOException, SQLException {
        UserDBAccess_impl userDBAccess_impl = new UserDBAccess_impl();
        CardsDBAccess_impl cardDBAccess_impl = new CardsDBAccess_impl();
        HttpResponse_Impl response = null;
        JsonNode node = null;
        ObjectMapper mapper = new ObjectMapper();
        String requestContent = req.getHttpsContent();
        if(requestContent != null && !requestContent.equals("")) { node = mapper.readTree(requestContent); }

        switch(req.getMethod())
        {
            case "POST" -> response = handlePOST(node, userDBAccess_impl, cardDBAccess_impl);
            case "GET" -> response = handleGET(cardDBAccess_impl);
            case "PUT" -> System.out.println("PUT");
            case "DELETE" -> System.out.println("DELETE");
        }

        /*
        switch(req.getPath())
        {
            case "/deck" -> {
                System.out.println("deck");
                //response = new HttpResponse_Impl(200);
            }
            case "/tradings" -> {
                System.out.println("tradings");
                //response = new HttpResponse_Impl(200);
            }
            case "/stats" -> {
                System.out.println("stats");
                //response = new HttpResponse_Impl(200);
            }
            case "/score" -> {
                System.out.println("score");
                //response = new HttpResponse_Impl(200);
            }
        }

         */
        return response;
    }

    @Override
    public HttpResponse_Impl handlePOST(JsonNode node, UserDBAccess_impl userDBAccess_impl, CardsDBAccess_impl cardsDBAccess_impl) throws SQLException {
        String res;

        switch(req.getPath())
        {
            case "/users" -> {
                // --- getting username and password of node to add to db
                // --- if user was created it will get user from db again to check
                // if not null - success
                if(userDBAccess_impl.addUser(node) != null)
                { return new HttpResponse_Impl(200, "user created"); }
                else { return new HttpResponse_Impl(400, "user not created"); }
            }
            case "/sessions" -> {
                // --- getting user from db
                res = userDBAccess_impl.loginUser(node);
                // check if password matches entered password
                System.out.println(res);
                if(res != null && res != "") { return new HttpResponse_Impl(200, "user logged in"); }
                else { return new HttpResponse_Impl(400, "user not logged in, wrong password or username"); }
            }
            case "/packages" -> {
                ArrayList<String> oneCard = new ArrayList<>(3);
                // --- if adding packages was a success true
                if(!traverse(node, oneCard)) { return new HttpResponse_Impl(500, "package not submitted"); }
                else {
                    this.count++; // --- count packages
                    return new HttpResponse_Impl(200, "package created");
                }
            }
            case "/transactions/packages" -> {
                // --- check for if user is authorized first before they can aquire packages
                // true if authorized
                if(cardsDBAccess_impl.acquirePackage(req.getAuthorizedUser()))
                { return new HttpResponse_Impl(200, "package bought"); }
                else { return new HttpResponse_Impl(400, "not enough money"); }
            }
        }
        return null;
    }

    @Override
    public HttpResponse_Impl handleGET(CardsDBAccess_impl cardDBAccess_impl)
    {
        switch(req.getPath())
        {
            case "/cards" -> {
                // --- check if user is authorized
                String authUser = req.getAuthorizedUser();
                // if null or empty = unauthorized to get cards
                if(authUser == null)
                { return new HttpResponse_Impl(401, "not authorized"); }
                // --- get cards from db
                String res = cardDBAccess_impl.showCards(authUser);
                // if result is not null - user owns cards - prints cards
                if(res != null) { return new HttpResponse_Impl(200, res); }
                else { return new HttpResponse_Impl(400, "no cards shown"); }
            }
            case "/deck" -> {

            }
        }

        return null;
    }

    @Override
    public boolean traverse(JsonNode root, ArrayList<String> oneCard)
    {
        if(root.isObject()){
            Iterator<String> fieldNames = root.getFieldNames();
            // iterate through all field names
            while(fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = root.get(fieldName);
                traverse(fieldValue, oneCard);
            }
        } else if(root.isArray()){
            ArrayNode arrayNode = (ArrayNode) root;
            // iterate through array
            for(int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                traverse(arrayElement, oneCard);
            }
        } else { // single value field
            if(oneCard.size() < 15)
            { oneCard.add(root.getValueAsText()); }
            if(oneCard.size() == 15)
            {
                CardsDBAccess_impl cardDBAccess_impl = new CardsDBAccess_impl();
                boolean success = cardDBAccess_impl.createPackage(oneCard, this.count);
                if(!success) return false;
                oneCard.clear();
            }
        }
        return true;
    }
}