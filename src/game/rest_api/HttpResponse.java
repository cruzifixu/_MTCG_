package game.rest_api;
import lombok.Getter;
import java.io.PrintWriter;

public class HttpResponse implements HttpResponse_interface
{
    @Getter
    String version;
    @Getter
    String statusMessage;
    @Getter
    int statusCode;
    @Getter
    String host;
    @Getter
    String content_type;
    @Getter
    int content_length;

    @Getter
    String RESPONSE;

    private static HttpResponse response = null;

    private PrintWriter writer;

    public static HttpResponse getInstance()
    {
        if(response == null) response = new HttpResponse();
        return response;
    }

    private HttpResponse() {
        this.version = "HTTP/1.1";
        this.statusMessage = "OK";
        this.statusCode = 200;
        this.content_type = "application/json";
        this.host = "";
        this.content_length = 0;
    }


    public void setWriter(PrintWriter writer)
    {
        this.writer = writer;
    }

    @Override
    public void write(int code) {
        this.statusCode = code;

        if(this.writer == null) System.out.println("NULL---------------------------------");

        switch(code)
        {
            case 400 -> this.statusMessage = "Bad Request";
            case 401 -> this.statusMessage = "unauthorized";
            case 403 -> this.statusMessage = "forbidden";
            case 404 -> this.statusMessage = "Not found";
            case 500 -> this.statusMessage = "Internal Servererror";
        }
        // bob the builder
        StringBuilder bob = new StringBuilder();
        bob.append(version).append(" ").append(statusCode).append(" ").append(statusMessage).append("\r\n");
        bob.append("Host: ").append(host).append("\r\n");
        bob.append("Content-type: ").append(content_type).append("\r\n");
        bob.append("Content-length: ").append(content_length).append("\r\n");
        //System.out.println(bob);
        this.writer.write(bob.toString());
        this.RESPONSE = bob.toString();
        this.writer.flush();
    }
}
