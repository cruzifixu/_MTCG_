package game.rest_api;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RestServer implements Runnable, RestServer_interface
{
    private static ServerSocket listener = null;
    private final int port;

    public RestServer(int port)
    {
        this.port = port;
    }

    public void listen()
    {

        System.out.println("Server has been started");
        try
        {
            listener = new ServerSocket(port);
            System.out.println("listening from: " + listener.getLocalPort());
            while(true)
            {
                Socket socket = listener.accept();
                System.out.println("New connection from: " + listener.getLocalPort());
                // create Thread - runs outside of thread
                Thread thread = new Thread(() -> {
                    try {
                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        OutputStream os = socket.getOutputStream();
                        PrintWriter pw = new PrintWriter(os);
                        HttpRequestImpl req = new HttpRequestImpl(br);
                        HttpRequestHandlerImpl handler = new HttpRequestHandlerImpl(req);
                        HttpResponse response = handler.handle(req);
                        pw.println(response.getPayload());
                        pw.flush();
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
