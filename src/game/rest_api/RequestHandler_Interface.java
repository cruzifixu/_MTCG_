package game.rest_api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public interface RequestHandler_Interface {
    void Handler(Request req) throws IOException;
    void setWriter(PrintWriter writer);
}
