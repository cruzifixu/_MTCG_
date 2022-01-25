package game.rest_api;
import java.io.IOException;

public interface HttpRequest {

    String getPath();
    String getContent();
    String readBody(int contentlength) throws IOException;
    String getHttpsContent();
    int readHttpHeader() throws IOException;
    void authorizeRequest();
}
