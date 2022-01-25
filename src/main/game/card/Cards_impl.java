package game.card;

import lombok.Getter;
import lombok.Setter;

public class Cards_impl {
    @Getter
    protected String id;
    @Getter
    protected String card_name;
    @Getter
    protected String element_type;
    @Getter @Setter
    protected double damage;
    @Getter
    protected String owner;
    @Getter
    protected int package_num;
}
