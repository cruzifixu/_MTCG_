package game.card;

public class Spellcard extends Cards_impl
{
    public Spellcard(String id, String name, String element_type, double damage, String owner, int package_num)
    {
        this.id = id;
        this.card_name = name;
        this.element_type = element_type;
        this.damage = damage;
        this.owner = owner;
        this.package_num = package_num;
    }
}
