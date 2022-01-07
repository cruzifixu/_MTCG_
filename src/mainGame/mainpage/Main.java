package mainGame.mainpage;
import game.*;
import game.rest_api.RestServer;

public class Main {

    Main() {}


    public static void main(String[] args) throws Exception
    {
        Thread restService = new Thread(new RestServer(10001));
        restService.start();
    }

}
