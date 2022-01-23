package game.card;

import lombok.Getter;

public class Cards_impl implements Cards
{
    @Getter
    protected String id;
    @Getter
    protected String card_name;
    @Getter
    protected String element_type;
    @Getter
    protected double damage;
    @Getter
    protected String owner;
    @Getter
    protected int package_num;

}
