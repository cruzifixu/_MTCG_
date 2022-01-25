package game.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardsDBAccess_implTest {

    @Test
    void addCardToPackage() {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        Cards_impl cards = new Monstercard("6cd85277-4590-49d4-b0cf-ba0a921faad1", "WaterGoblin", "Water", 15, "leeiam", 23);
        assertDoesNotThrow(()->assertTrue(CardDBAccess.addCardToPackage(cards)));
    }
    @Test
    void getCard()
    {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        assertEquals("{\"id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad1\",\"card_name\":\"WaterGoblin\",\"element_type\":\"Water\",\"damage\":\"15\",\"username\":\"null\",\"package_num\":\"23\"}\n",
                CardDBAccess.getCard("6cd85277-4590-49d4-b0cf-ba0a921faad1"));
    }
    @Test
    void getNoCard()
    {
        CardsDBAccess_impl CardDBAccess = new CardsDBAccess_impl();
        assertEquals("", CardDBAccess.getCard("6cd77756-4736-56d4-b0cf-ba0a921faad5"));
    }
}