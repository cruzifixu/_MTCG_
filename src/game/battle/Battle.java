package game.battle;

import game.card.Cards_impl;
import lombok.Getter;

import java.util.ArrayList;

public interface Battle {
    boolean Battlefight();
    void checkRules(Cards_impl card1, Cards_impl card2);
    void checkEffectiveness(Cards_impl card1, Cards_impl card2);
    void checkDamage(Cards_impl card1, Cards_impl card2);
    double calculateELO(ArrayList<Integer> user1, ArrayList<Integer> user2, double endStatus);
}
