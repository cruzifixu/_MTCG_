package game.trading;

import lombok.Getter;

public class Trading_impl {
    @Getter
    private final String id;
    @Getter
    private final String card_to_trade;
    @Getter
    private final String type;
    @Getter
    private final int min_damage;

    public Trading_impl(String id, String card, String type, int min_damage)
    {
        this.id = id;
        this.card_to_trade = card;
        this.type = type;
        this.min_damage = min_damage;
    }
}
