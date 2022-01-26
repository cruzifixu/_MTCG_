package game.trading;

import game.card.CardsDBAccess_impl;
import game.card.Cards_impl;
import game.card.Monstercard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradingDBAccess_implTest {
    @Test
    void createTrade() {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        Cards_impl cards = new Monstercard("6cd85277-4590-49d4-b0cf-ba0a921faad6", "WaterGoblin", "Water", 15, "", 23);
        assertDoesNotThrow(()->assertTrue(CardDBAccess.addCardToPackage(cards)));
        TradingDBAccess_impl TradingDBAccess = new TradingDBAccess_impl();
        Trading_impl trade = new Trading_impl("6cd85277-4590-49d4-b0cf-ba0a921faad5", "6cd85277-4590-49d4-b0cf-ba0a921faad6", "Water", 15);
        assertTrue(TradingDBAccess.createTrade(trade));
    }
    // aufeinander bauend
    @Test
    void deleteTrade()
    {
        TradingDBAccess_impl TradingDBAccess = new TradingDBAccess_impl();
        assertTrue(TradingDBAccess.deleteTrade("6cd85277-4590-49d4-b0cf-ba0a921faad5", "6cd85277-4590-49d4-b0cf-ba0a921faad6"));
    }
    // aufeinander bauend
    @Test
    void getID()
    {
        TradingDBAccess_impl TradingDBAccess = new TradingDBAccess_impl();
        assertEquals("", TradingDBAccess.getID("6cd85277-4590-49d4-b0cf-ba0a921faad5"));
    }
}