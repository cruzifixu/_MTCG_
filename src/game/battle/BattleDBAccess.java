package game.battle;

public interface BattleDBAccess {
    boolean ChangePlayerStatus(String user, String status);
    String checkPlayerStatus(String user);
}
