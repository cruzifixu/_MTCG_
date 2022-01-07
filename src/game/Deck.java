package game;
import java.util.ArrayList;

public class Deck {
    protected ArrayList<Cards> chosenDeck;

    // set Deck
    protected void setDeck(ArrayList<Cards> CardsChosen) {
        this.chosenDeck.clear();
        this.chosenDeck = CardsChosen;
    }
    // get chosen Deck
    protected ArrayList<Cards> getChosenDeck() {
        return this.chosenDeck;
    }
    // remove chosen card
    protected void removeFromDeck(String CardToBeRemoved) {
        this.chosenDeck.remove(CardToBeRemoved);
    }
}
