package game.battle;

import game.card.Cards_impl;
import game.user.UserDBAccess_impl;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Random;

public class Battle_impl implements Battle
{
    @Getter @Setter
    String user1;
    @Getter @Setter
    String user2;
    @Getter
    int loseCountUser1 = 0;
    @Getter
    int loseCountUser2 = 0;
    @Getter
    ArrayList<Cards_impl> user1card;
    @Getter
    ArrayList<Cards_impl> user2card;
    @Getter
    ArrayList<Integer> user1stats; // wins - 0, lost - 1, elo - 2
    @Getter
    ArrayList<Integer> user2stats; // wins - 0, lost - 1, elo - 2
    @Getter
    BattleDBAccess_impl battleDBAccessImpl = new BattleDBAccess_impl();
    @Getter
    BattleFightLogger logger = BattleFightLogger.getInstance();

    public Battle_impl(String user1, String user2, ArrayList<Cards_impl> user1card, ArrayList<Cards_impl> user2card, ArrayList<Integer> user1stats, ArrayList<Integer> user2stats)
    {
        this.user1 = user1;
        this.user2 = user2;
        this.user1card = user1card;
        this.user2card = user2card;
        this.user1stats = user1stats;
        this.user2stats = user2stats;

        // change status of players -> in battle
        getBattleDBAccessImpl().ChangePlayerStatus(user1, "in battle...");
        getBattleDBAccessImpl().ChangePlayerStatus(user2, "in battle...");
    }

    @Override
    public boolean BattleFight()
    {
        int rounds = 0;
        do
        {
            logger.newLogEntry("Round " + rounds + "\n");

            Random random = new Random();
            Cards_impl card1 = this.user1card.get(random.nextInt(user1card.size()));
            Cards_impl card2 = this.user2card.get(random.nextInt(user2card.size()));

            // check specialties of cards -> rules on moodle
            checkRules(card1, card2);
            checkRules(card2, card1);
            // check Effectiveness -> rules on moodle
            checkEffectiveness(card1, card2);
            checkEffectiveness(card2, card1);

            // monster with higher damage wins
            // winner gets loser card
            // elo++
            // wins++--
            // lose++--
            checkDamage(card1, card2);

            // game ends if one loses all cards
            if(user1card.isEmpty() || user2card.isEmpty())
            {
                if(user1card.isEmpty()) { logger.newLogEntry(user1 + " won!\n"); }
                else { logger.newLogEntry(user2 + " won!\n"); }
                break;
            }
        } while(rounds++ < 100);

        UserDBAccess_impl UserDBAccess = new UserDBAccess_impl();

        // update stats of users
        // db update status back to menu/ idle
        return getBattleDBAccessImpl().ChangePlayerStatus(user1, "idle") && getBattleDBAccessImpl().ChangePlayerStatus(user2, "idle")
                && UserDBAccess.UpdateStats(user1, user1stats) && UserDBAccess.UpdateStats(user2, user2stats);
    }

    @Override
    public void checkRules(Cards_impl card1, Cards_impl card2) {
        if(card1.getCard_name().contains("Goblin") && card2.getCard_name().contains("Dragon"))
        {
            card1.setDamage(0);
            logger.newLogEntry("Goblins are too afraid of Dragons to attack.\n");
        }
        else if(card1.getCard_name().contains("Wizard") && card2.getCard_name().contains("Ork"))
        {
            card2.setDamage(0);
            logger.newLogEntry("Wizard can control Orcs so they are not able to damage them.\n");
        }
        else if(card1.getCard_name().contains("Knights") && card2.getCard_name().contains("WaterSpell"))
        {
            card2.setDamage(0);
            logger.newLogEntry("The armor of Knights is so heavy that WaterSpells make them drown them instantly.\n");
        }
        else if(card1.getCard_name().contains("Kraken") && card2.getCard_name().contains("Spell"))
        {
            card2.setDamage(0);
            logger.newLogEntry("The Kraken is immune against spells.\n");
        }
        else if(card1.getCard_name().contains("FireElves") && card2.getCard_name().contains("Dragon"))
        {
            card2.setDamage(0);
            logger.newLogEntry("The FireElves know Dragons since they were little and can evade their attacks.\n");
        }
    }

    @Override
    public void checkEffectiveness(Cards_impl card1, Cards_impl card2) {
        // --------------- DIFFERENT ELEMENTS --------------- //
        if      ((card1.getCard_name().contains("Water") && card2.getCard_name().contains("Fire")) ||
                (card1.getCard_name().contains("Fire") && card2.getCard_name().contains("Regular")) ||
                (card1.getCard_name().contains("Regular") && card2.getCard_name().contains("Water"))
        )
        {
            card1.setDamage(card1.getDamage()*2); // double damage
            card2.setDamage(card2.getDamage()/2); // half damage
            logger.newLogEntry(card1.getCard_name() + " is not effective against " + card2.getCard_name() + "\n");
        }
        // --------------- SAME ELEMENT --------------- //
        else if((card1.getCard_name().contains("Fire") && card2.getCard_name().contains("Fire")) ||
                (card1.getCard_name().contains("Water") && card2.getCard_name().contains("Water")) ||
                (card1.getCard_name().contains("Regular") && card2.getCard_name().contains("Regular"))
        )
        { logger.newLogEntry(card1.getCard_name() + " has no effect on " + card2.getCard_name() + "\n"); }

    }

    @Override
    public void checkDamage(Cards_impl card1, Cards_impl card2)
    {
        Random random = new Random();
        double oldDam = 0;
        // ---------- SPECIAL CASE ---------- //
        // --- IF PLAYER LOST TWO TIMES IN A ROW THEY GET A CHANCE TO DRAW ANOTHER CARD
        // --- DAMAGE TO OTHER CARD WILL BE ADDED TO DAMAGE TO FIRST CARD
        if(card1.getDamage() < card2.getDamage())
        {
            if(loseCountUser2 == 0)
            { loseCountUser1++; }
            else { loseCountUser2 = 0; }

            // get second card of user 1
            if(loseCountUser1 == 2)
            {
                Cards_impl card3 = this.user1card.get(random.nextInt(user1card.size()));
                while(card3.getCard_name().equals(card1.getCard_name()))
                { card3 = this.user1card.get(random.nextInt(user1card.size())); }

                // save old damage so comparison is correct
                oldDam = card2.getDamage();

                checkEffectiveness(card3, card2);

                // set Damage to old Damage
                card2.setDamage(oldDam);

                // if damage to second card bigger than of card one - set damage to card1 to damage to second card
                if(card3.getDamage() > card1.getDamage())
                {card1.setDamage(card3.getDamage()); }

                loseCountUser1 = 0;
            }
        }
        else
        {
            if(loseCountUser1 == 0)
            { loseCountUser2++; }
            else { loseCountUser1 = 0; }

            // get second card of user 1
            if(loseCountUser2 == 2)
            {
                Cards_impl card3 = this.user2card.get(random.nextInt(user2card.size()));
                while(card3.getCard_name().equals(card1.getCard_name()))
                { card3 = this.user2card.get(random.nextInt(user2card.size())); }

                // save old damage so comparison is correct
                oldDam = card1.getDamage();

                checkEffectiveness(card3, card1);

                // set Damage to old Damage
                card1.setDamage(oldDam);

                // if damage to second card bigger than of card one - set damage to card1 to damage to second card
                if(card3.getDamage() > card2.getDamage())
                {card2.setDamage(card3.getDamage()); }

                loseCountUser2 = 0;
            }
        }

        // card 1 loses
        if(card1.getDamage() < card2.getDamage())
        {
                          // add element at index of card1
            user2card.add(user1card.get(user1card.indexOf(card1)));
            // remove from user1 card
            user1card.remove(card1);
            user1stats.set(1, user1stats.get(1)+1); // ++ lose
            user2stats.set(0, user2stats.get(0)+1); // ++ win
            user1stats.set(2, (int) calculateELO(user1stats, user2stats, 0)); // lose
            user2stats.set(2, (int) calculateELO(user2stats, user1stats, 1)); // win

            logger.newLogEntry(card1.getCard_name() + " won against " + card2.getCard_name() + "\n");
        }
        // card 2 loses
        else if(card1.getDamage() > card2.getDamage())
        {
            // add element at index of card2
            user1card.add(user2card.get(user2card.indexOf(card2)));
            // remove from user2 card
            user2card.remove(card2);
            user2stats.set(1, user2stats.get(1)+1); // ++ lose
            user1stats.set(0, user1stats.get(0)+1); // ++ win
            user1stats.set(2, (int) calculateELO(user1stats, user2stats, 1)); // win
            user2stats.set(2, (int) calculateELO(user2stats, user1stats, 0)); // lose

            logger.newLogEntry(card2.getCard_name() + " won against " + card1.getCard_name() + "\n");
        }
        else // draw
        {
            user1stats.set(2, (int) calculateELO(user1stats, user2stats, 0.5)); // win
            user2stats.set(2, (int) calculateELO(user2stats, user1stats, 0.5)); // lose#

            logger.newLogEntry(card1.getCard_name() + " and " + card2.getCard_name() + " had a draw\n");
        }
    }

    @Override
    public double calculateELO(ArrayList<Integer> user1, ArrayList<Integer> user2, double endStatus) {
        int k;
        // get elo at index 3
        int estimated = 1/(1+10^((user2.get(2) - user1.get(2))/400));
        if(user1.get(2) < 2400) { k = 20; }
        else { k = 10; }
        return user1.get(2) + k * (endStatus - estimated);
    }
}
