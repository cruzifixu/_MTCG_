package game.deck;

import game.card.CardsDBAccess_impl;
import game.card.Cards_impl;
import game.card.Monstercard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckDBAccess_implTest {

    @Test
    void setUserDeck() {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        DeckDBAccess_impl DeckDBAccess = new DeckDBAccess_impl();
        Cards_impl cards = new Monstercard("6cd85277-4590-49d4-b0cf-ba0a921faad2", "FireGoblin", "Fire", 16, "", 24);
        assertDoesNotThrow(()->assertTrue(CardDBAccess.addCardToPackage(cards)));
        assertDoesNotThrow(()->assertTrue(DeckDBAccess.setUserDeck("6cd85277-4590-49d4-b0cf-ba0a921faad2")));
    }
    @Test
    void setUserDeckNotWorking()
    {
        DeckDBAccess_impl DeckDBAccess = new DeckDBAccess_impl();
        assertDoesNotThrow(()->assertFalse(DeckDBAccess.setUserDeck("6cd85277-4590-49d4-b0cf-ba0a921faad3")));
    }
}