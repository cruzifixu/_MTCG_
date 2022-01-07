package game.rest_api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public interface Request_Interface {

    public String getPath();

    public String getContent();
    public String readBody(BufferedReader reader, int contentlength);
    public String getHttpsContent();
    public void writeGetRequest(BufferedWriter writer) throws IOException;
    public int readHttpHeader(BufferedReader reader) throws IOException;
    public List<String> getContentStringsFromRegex(String pattern, int group);
    public List<String> getContentStringsRegexFromRegex(String pattern);
}
