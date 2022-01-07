package game.rest_api;

import java.io.IOException;
import java.net.ServerSocket;

public class RestServer implements Runnable, RestServer_interface
{
    private static ServerSocket listener = null;
    private int port;

    public RestServer(int port)
    {
        this.port = port;
    }

    public void listen()
    {

        System.out.println("Server has been started");
        try
        {
            this.listener = new ServerSocket(port);
            System.out.println("listening from: " + this.listener.getLocalPort());
            while(true)
            {
                _socket socket = new _socket(this.listener.accept());
                System.out.println("New connection from: " + this.listener.getLocalPort());
                // create Thread - runs outside of thread
                Thread thread = new Thread(() -> {
                    Request req = null;
                    try {
                        req = new Request(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    RequestHandler handler = new RequestHandler(req);
                    try {
                        handler.Handler();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    // override
    public void close()
    {
        System.out.println("Closing on: " + listener.getLocalPort());
        try
        {
            listener.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void run()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        listen();
    }
}
