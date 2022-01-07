package game.rest_api;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public interface Request_Interface {

    String getPath();

    String getContent();
    String readBody(BufferedReader reader, int contentlength) throws IOException;
    String getHttpsContent();
    void writeGetRequest(PrintWriter writer) throws IOException;
    int readHttpHeader(BufferedReader reader) throws IOException;
}
