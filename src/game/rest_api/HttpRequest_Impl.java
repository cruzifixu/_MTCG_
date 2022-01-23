package game.rest_api;

import lombok.Getter;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class HttpRequest_Impl implements HttpRequest
{
    @Getter
    HashMap<String, String> headers;
    @Getter
    private Socket socket;
    @Getter
    private  String method;
    private String path;
    @Getter
    private String secondLevelPath;
    private String host;
    private String content;
    private BufferedReader reader;
    private String token;
    @Getter
    private String authorizedUser;

    public HttpRequest_Impl(BufferedReader br) {
        this.reader = br;
    }

    public String getPath() { return this.path; }

    public String getContent()
    {
        return (content != null) ? content : getHttpsContent();
    }

    @Override
    public String readBody(int ContentLength) throws IOException {
        StringBuilder bob = new StringBuilder(10000);
        char[] buffer = new char[1024];
        int TotalLength = 0;
        int length = 0;
        if(ContentLength != 0)
        {
            while(true)
            {
                try {
                    if (!((length = reader.read(buffer)) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bob.append(buffer, 0, length);
                TotalLength += length;
                if(TotalLength >= ContentLength) break;
            }
        }


        //System.out.println(bob.toString());
        return bob.toString();
    }

    @Override
    public int readHttpHeader() throws IOException {
        String line;
        String[] m, l;
        int ContentLength = 0, count = 0;

        while((line = reader.readLine()) != null)
        {
            if(count++ == 0)
            {
                m = line.split(" ");
                l = m[1].split("/");
                this.method = m[0];
                this.path = l[1];
                if(l.length > 2) { this.secondLevelPath = l[2]; }
            }
            if(count++ == 3)
            {
                m = line.split(" ");
                this.host = m[1];
            }
            if(line.isBlank()) break;
            if(line.toLowerCase().startsWith("content-length:"))
            { ContentLength = Integer.parseInt(line.substring(15).trim()); }
            if(line.toLowerCase().startsWith("authorization: "))
            {
                m = line.split(":");
                this.token = m[1];
                this.authorizeRequest();
            }

        }
        return ContentLength;
    }

    @Override
    public String getHttpsContent() {
        try {
            int contentLength = this.readHttpHeader();
            content = readBody(contentLength);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return content;
    }

    @Override
    public void authorizeRequest()
    {
        String[] tokenParts = this.token.split(" ");
        String[] tokenPart = tokenParts[2].split("-");
        this.authorizedUser = tokenPart[0];
    }

}
