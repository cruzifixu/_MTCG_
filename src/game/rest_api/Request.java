package game.rest_api;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request implements Request_Interface
{
    @Getter
    HashMap<String, String> headers;
    @Getter
    private _socket socket;
    private String path;
    private String host;
    private String content;
    @Getter
    private PrintWriter writer;

    public Request(_socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(this.socket.getOutputStream(),true);
    }

    @Override
    public String getPath() { return this.path; }

    @Override
    public String getContent()
    {
        return (content != null) ? content : getHttpsContent();
    }

    @Override
    public String readBody(BufferedReader reader, int contentlength) throws IOException {
        StringBuilder bob = new StringBuilder(10000);
        char[] buffer = new char[1024];
        int TotalLength = 0;
        int length = 0;
        while(true)
        {
            try {
                if (!((length = reader.read(buffer)) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            bob.append(buffer, 0, length);
            TotalLength += length;
            if(TotalLength >= contentlength) break;
        }

        //System.out.println(bob.toString());
        return bob.toString();
    }

    @Override
    public String getHttpsContent()
    {
        // HttpResponse response = HttpResponse.getInstance();
        //response.setWriter(writer);

        if(content != null && !content.isBlank()) return content;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream())))
        {
            //writeGetRequest(this.writer);
            int contentLength = readHttpHeader(reader);
            return content = readBody(reader, contentLength);
        }

        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void writeGetRequest(PrintWriter writer) {
        writer.write("GET / HTTP/1.1\r\n");
        writer.write("Host: " + this.host + "\r\n");
        writer.write("Accept: */*\r\n" );
        writer.write("\r\n" );
        writer.flush();
    }

    @Override
    public int readHttpHeader(BufferedReader reader) throws IOException {
        String line;
        String[] m;
        int contentlength = 0, count = 0;

        while((line = reader.readLine()) != null)
        {
            if(count++ == 0)
            {
                m = line.split(" ");
                this.path = m[1];
            }
            if(count++ == 3)
            {
                m = line.split(" ");
                this.host = m[1];
            }
            if(line.isBlank()) break;
            if(line.toLowerCase().startsWith("POST"))
            if(line.toLowerCase().startsWith("content-length:"))
                contentlength = Integer.parseInt(line.substring(15).trim());

        }
        return contentlength;
    }
}
