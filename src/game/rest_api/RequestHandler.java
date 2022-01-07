package game.rest_api;

import game.user.UserDBAccess;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class RequestHandler implements RequestHandler_Interface
{
    private final Request req;

    public RequestHandler(Request req)
    {
        this.req = req;
    }

    @Override
    public void Handler() throws IOException {
        String requestContent = req.getHttpsContent();
        ObjectMapper mapper= new ObjectMapper();
        JsonNode node=mapper.readTree(requestContent);
        HttpResponse response = new HttpResponse();
        String res;

        if(req.getPath().equals("/users"))
        {
            UserDBAccess dbAccess = new UserDBAccess();
            String user = node.get("Username").getValueAsText();
            String psw = node.get("Password").getValueAsText();
            res = dbAccess.addUser(user, psw);

            if(res.length() > 0)
            {
                if(res.contains("OK"))
                {
                    response.write(req.writer, 200);
                }
            }
        }
    }
}
