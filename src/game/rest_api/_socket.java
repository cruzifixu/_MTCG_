package game.rest_api;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class _socket implements socket_interface
{
    @Getter
    Socket socket;

    public _socket(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        return socket.getOutputStream();
    }
}
