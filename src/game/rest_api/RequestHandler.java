package game.rest_api;

import java.io.IOException;

public interface RequestHandler {
    HttpResponse handle(HttpRequestImpl req) throws IOException;

}
