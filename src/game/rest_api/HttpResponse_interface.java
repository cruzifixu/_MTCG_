package game.rest_api;

import java.io.BufferedWriter;

public interface HttpResponse_interface {
    public void write(BufferedWriter writer, int code);
}
