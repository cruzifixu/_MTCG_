package game;

import java.util.ArrayList;

public class Battles {
    //player one deck cards_impl
    protected ArrayList<Cards> p1cards;
    //player two deck cards_impl
    protected ArrayList<Cards> p2cards;
    // players
    protected String playerOne;
    protected String playerTwo;

    // set player decks
    protected void setDecks(ArrayList<Cards> deckOne, ArrayList<Cards> deckTwo) {
        this.p1cards = deckOne;
        this.p2cards = deckTwo;
    }
    //get player decks
    protected ArrayList<Cards> getDeck(String player) {
        if(player == playerOne) return this.p1cards;
        else return this.p2cards;
    }
    //battle itself
    protected String battle(){

        return "won";
    }
}
