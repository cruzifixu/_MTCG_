package game.battle;

public interface BattleDBAccess {
    boolean ChangePlayerStatus(String user, String status);
    String checkAllPlayerStatus(String user);
}
