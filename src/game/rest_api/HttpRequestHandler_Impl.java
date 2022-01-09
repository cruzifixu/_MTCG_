package game.rest_api;

import game.user.UserDBAccess_impl;
import lombok.Setter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.SQLException;

public class HttpRequestHandler_Impl implements HttpRequestHandler
{
    @Setter
    private HttpRequest_Impl req;

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
        UserDBAccess_impl dbAccess = new UserDBAccess_impl();
        String res;

        switch(req.getPath())
        {
            case "/users" -> {
                String user = node.get("Username").getValueAsText();
                String psw = node.get("Password").getValueAsText();
                res = dbAccess.addUser(user, psw);

                if(res != null)
                {
                    response = new HttpResponse_Impl(200);
                }
                else
                {
                    response = new HttpResponse_Impl(400);
                }
            }
            case "/sessions" -> {
                String user = node.get("Username").getValueAsText();
                String psw = node.get("Password").getValueAsText();
                res = dbAccess.getUser(user);
                if(res.contains(psw)) { response = new HttpResponse_Impl(200); }
                else { response = new HttpResponse_Impl(400); }
            }
            case "/packages" -> {
                System.out.println("pack");
                response = new HttpResponse_Impl(200);
            }
            case "/transactions/packages" -> {
                System.out.println("transaction");
                response = new HttpResponse_Impl(200);
            }
            case "/cards_impl" -> {
                System.out.println("cards_impl");
                response = new HttpResponse_Impl(200);
            }
            case "/deck" -> {
                System.out.println("deck");
                response = new HttpResponse_Impl(200);
            }
            case "/tradings" -> {
                System.out.println("tradings");
                response = new HttpResponse_Impl(200);
            }
            case "/stats" -> {
                System.out.println("stats");
                response = new HttpResponse_Impl(200);
            }
            case "/score" -> {
                System.out.println("score");
                response = new HttpResponse_Impl(200);
            }
        }

        return response;
    }
}