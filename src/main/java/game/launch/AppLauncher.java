package game.launch;
import game.objects.field.GameMap;

public class AppLauncher {
    private GameMap playerMap;
    private GameMap enemyMap;

    public AppLauncher() {
        playerMap = new GameMap();
        enemyMap = new GameMap();
    }

    public void launchRandom() {
        GameProcess inputListener = new GameProcess(playerMap, enemyMap);
        Input input = new Input(inputListener);
        do {
            System.out.println("--------------- My map -----------------");
            playerMap.displayMap();
            System.out.println("------------- Enemy map ----------------");
            enemyMap.displayEnemyMap();
            input.process();
        } while (!input.isFinished() /*|| player.isVictory() || ai.isVictory()*/);
    }
}