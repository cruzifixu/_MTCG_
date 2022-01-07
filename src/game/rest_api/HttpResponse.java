package game.rest_api;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.BufferedWriter;

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
    int content_length;

    HttpResponse()
    {
        this.version = "HTTP/1.1";
        this.statusMessage = "OK";
        this.statusCode = 200;
        this.host = "";
        this.content_length = 0;
    }

    @Override
    public void write(BufferedWriter writer, int code)
    {
        // hello ok
    }
}
