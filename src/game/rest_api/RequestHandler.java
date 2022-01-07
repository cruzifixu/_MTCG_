package game.rest_api;

import game.user.UserDBAccess;
import lombok.Setter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

public class RequestHandler implements RequestHandler_Interface
{
    @Setter
    private Request req;
    private PrintWriter writer;

    public RequestHandler(Request req, _socket socket)
    {
        this.req = req;
        try {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setWriter(PrintWriter writer)
    {
        this.writer = writer;
    }

    @Override
    public void Handler(Request req) throws IOException {
        HttpResponse response = HttpResponse.getInstance();
        response.setWriter(req.getWriter());
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
                if(res.contains("OK"))
                {
                    response.write(200);
                }
            }
            else
            {
                response.write(400);
            }
        }
    }
}