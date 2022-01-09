package game.rest_api;

import lombok.Getter;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class HttpRequestImpl implements Request
{
    @Getter
    HashMap<String, String> headers;
    @Getter
    private Socket socket;
    private String path;
    private String host;
    private String content;
    private BufferedReader reader;

    public HttpRequestImpl(BufferedReader br) {
        this.reader = br;
    }


    public String getPath() { return this.path; }


    public String getContent()
    {
        return (content != null) ? content : getHttpsContent();
    }


    public String readBody(int contentlength) throws IOException {
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

    public int readHttpHeader() throws IOException {
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


    public String getHttpsContent() {

        if(content != null && !content.isBlank()) return content;

        try {
            int contentLength = this.readHttpHeader();
            return content = readBody(contentLength);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }


}
