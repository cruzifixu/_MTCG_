package game.battle;

public interface BattleDBAccess {
    boolean ChangePlayerStatus(String user, String status);
    String checkOnePlayerStatus(String user);
    String checkAllPlayerStatus(String user);
}
