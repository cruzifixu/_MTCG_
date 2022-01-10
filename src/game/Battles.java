package game;

import game.card.Cards_impl;

import java.util.ArrayList;

public class Battles {
    //player one deck Cards_impl
    protected ArrayList<Cards_impl> p1cards;
    //player two deck Cards_impl
    protected ArrayList<Cards_impl> p2cards;
    // players
    protected String playerOne;
    protected String playerTwo;

    // set player decks
    protected void setDecks(ArrayList<Cards_impl> deckOne, ArrayList<Cards_impl> deckTwo) {
        this.p1cards = deckOne;
        this.p2cards = deckTwo;
    }
    //get player decks
    protected ArrayList<Cards_impl> getDeck(String player) {
        if(player == playerOne) return this.p1cards;
        else return this.p2cards;
    }
    //battle itself
    protected String battle(){

        return "won";
    }
}
