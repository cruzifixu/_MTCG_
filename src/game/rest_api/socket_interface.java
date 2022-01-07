package game.rest_api;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface socket_interface {
    //Socket getSocket();
    InputStream getInputStream() throws IOException;
    OutputStream getOutputStream() throws IOException;
}
