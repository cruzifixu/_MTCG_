package game.rest_api;

import game.user.UserDBAccess;
import lombok.Setter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class HttpRequestHandlerImpl implements RequestHandler
{
    @Setter
    private HttpRequestImpl req;

    public HttpRequestHandlerImpl(HttpRequestImpl req)
    {
        this.req = req;
    }

    @Override
    public HttpResponse handle(HttpRequestImpl req) throws IOException {
        HttpResponse response = null;
        String requestContent = req.getHttpsContent();
        ObjectMapper mapper= new ObjectMapper();
        JsonNode node=mapper.readTree(requestContent);
        String res;

        if(req.getPath().equals("/users"))
        {
            UserDBAccess dbAccess = new UserDBAccess();
            String user = node.get("Username").getValueAsText();
            String psw = node.get("Password").getValueAsText();
            res = dbAccess.addUser(user, psw);

            if(res != null)
            {
                response = new HttpResponseImpl(200);
            }
            else
            {
                response = new HttpResponseImpl(400);
            }
        }
        return response;
    }
}