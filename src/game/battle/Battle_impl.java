package game.battle;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Random;

public class Battle_impl implements Battle
{
    @Getter
    ArrayList<String> user1card;
    @Getter
    ArrayList<String> user2card;


    @Override
    public boolean Battlefight()
    {
        Random random = new Random();
        String card1 = user1card.get(random.nextInt(5));
        String card2 = user2card.get(random.nextInt(5));


        return true;
    }
}
