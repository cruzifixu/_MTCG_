package game.rest_api;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public interface HttpResponse_interface {
    void write(int code) throws IOException;
}
