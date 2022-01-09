package game.rest_api;

import java.io.IOException;
import java.sql.SQLException;

public interface HttpRequestHandler {
    HttpResponse handle() throws IOException, SQLException;

}
