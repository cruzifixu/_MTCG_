package game;
import java.util.ArrayList;


public class Trading {
    // offer from players
    protected int Offer;
    // player name
    protected String name;

    //one player sets Offer
    protected void setOffer(int offer) {
        this.Offer = offer;
    }
    //get offer player set
    protected int getOffer() {
        return this.Offer;
    }
    // trade cards player wanted to trade
    protected void tradeCards(ArrayList<Cards> deckp1, ArrayList<Cards> deckp2) {

    }
}
