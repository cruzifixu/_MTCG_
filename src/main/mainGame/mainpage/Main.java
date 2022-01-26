package mainGame.mainpage;
import game.rest_api.RestServer_impl;

public class Main {

    Main() {}


    public static void main(String[] args) throws Exception
    {
        RestServer_impl res = new RestServer_impl(10001);
        res.run();
    }

}
