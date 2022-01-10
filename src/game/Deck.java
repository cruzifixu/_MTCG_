package game;
import game.card.Cards_impl;

import java.util.ArrayList;

public class Deck {
    protected ArrayList<Cards_impl> chosenDeck;

    // set Deck
    protected void setDeck(ArrayList<Cards_impl> CardsChosen) {
        this.chosenDeck.clear();
        this.chosenDeck = CardsChosen;
    }
    // get chosen Deck
    protected ArrayList<Cards_impl> getChosenDeck() {
        return this.chosenDeck;
    }
    // remove chosen card
    protected void removeFromDeck(String CardToBeRemoved) {
        this.chosenDeck.remove(CardToBeRemoved);
    }
}
