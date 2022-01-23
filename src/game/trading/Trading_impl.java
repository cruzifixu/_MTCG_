package game.trading;

import lombok.Getter;

public class Trading_impl {
    @Getter
    private String id;
    @Getter
    private String card_to_trade;
    @Getter
    private String type;
    @Getter
    private int min_damage;

    public Trading_impl(String id, String card, String type, int min_damage)
    {
        this.id = id;
        this.card_to_trade = card;
        this.type = type;
        this.min_damage = min_damage;
    }
}
