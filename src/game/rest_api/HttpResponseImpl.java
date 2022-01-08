package game.rest_api;
import lombok.Getter;

public class HttpResponseImpl implements HttpResponse
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

    @Override
    public String getPayload() {
        return this.RESPONSE;
    }

    public HttpResponseImpl() {
        this.version = "HTTP/1.1";
        this.statusMessage = "OK";
        this.statusCode = 200;
        this.content_type = "application/json";
        this.host = "127.0.0.1";
        this.content_length = 0;
    }

    public HttpResponseImpl(int code) {
        this();
        this.statusCode = code;

        switch(code)
        {
            case 200 -> this.statusMessage = "OK";
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
        this.RESPONSE = bob.toString();
    }

}
