package game.rest_api;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public interface Request {

    String getPath();
    String getContent();
    String readBody(int contentlength) throws IOException;
    String getHttpsContent();
    int readHttpHeader() throws IOException;
}
