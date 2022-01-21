package game.rest_api;

import game.card.CardsDBAccess_impl;
import game.user.UserDBAccess_impl;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface HttpRequestHandler {
    HttpResponse handle() throws IOException, SQLException;
    HttpResponse_Impl handlePOST(JsonNode node)  throws SQLException;
    HttpResponse_Impl handleGET();
    HttpResponse_Impl handlePUT();
    boolean traverse(JsonNode root, ArrayList<String> oneCard);
}
