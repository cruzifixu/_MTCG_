package game.deck;

import lombok.Getter;

public class Deck_impl implements Deck
{
    @Getter
    protected String card_name;
    @Getter
    protected String CategoryType;
    @Getter
    protected String ElementType;
    @Getter
    protected int damage;
}
