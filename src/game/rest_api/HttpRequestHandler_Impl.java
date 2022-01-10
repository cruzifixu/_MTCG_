package game.rest_api;

import game.card.CardsDBAccess;
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
        HttpResponse_Impl response = null;
        String requestContent = req.getHttpsContent();
        ObjectMapper mapper= new ObjectMapper();
        JsonNode node=mapper.readTree(requestContent);
        UserDBAccess_impl userDBAccess_impl = new UserDBAccess_impl();
        CardsDBAccess_impl cardDBAccess_impl = new CardsDBAccess_impl();
        String res;

        switch(req.getPath())
        {
            case "/users" -> {
                String user = node.get("Username").getValueAsText();
                String psw = node.get("Password").getValueAsText();
                res = userDBAccess_impl.addUser(user, psw);

                if(res != null) { response = new HttpResponse_Impl(200, "user created"); }
                else { response = new HttpResponse_Impl(400, "user not created"); }
            }
            case "/sessions" -> {
                String user = node.get("Username").getValueAsText();
                String psw = node.get("Password").getValueAsText();
                res = userDBAccess_impl.getUser(user);
                if(res.contains(psw)) { response = new HttpResponse_Impl(200, "user logged in"); }
                else { response = new HttpResponse_Impl(400, "user not logged in, wrong password or username"); }
            }
            case "/packages" -> {
                ArrayList<String> oneCard = new ArrayList<>(3);
                boolean success = traverse(node, oneCard);
                this.count++;
                if(!success) { response = new HttpResponse_Impl(500, "package not submitted"); }
                else response = new HttpResponse_Impl(200, "package created");
            }
            case "/transactions/packages" -> {
                String user = req.getAuthorizedUser();
                boolean success = cardDBAccess_impl.acquirePackage(user);
                if(success) { response = new HttpResponse_Impl(200, "package bought"); }
                else { response = new HttpResponse_Impl(400, "not enough money"); }
            }
            case "/Cards_impl" -> {
                System.out.println("Cards_impl");
                //response = new HttpResponse_Impl(200, "");
            }
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

        return response;
    }

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
            {
                oneCard.add(root.getValueAsText());
            }
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