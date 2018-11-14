package game.launch;
import game.objects.field.GameMap;

public class AppLauncher {
    private GameMap playerMap;
    private GameMap aiMap;

    public AppLauncher() {
        playerMap = new GameMap();
        aiMap = new GameMap();
    }

    public void launchRandom() {
        GameProcess gameProcess = new GameProcess(playerMap, aiMap, this);
        Input input = new Input(gameProcess, this);
        do {
            input.process();
        } while (!gameProcess.isVictory() /*|| player.isVictory() || ai.isVictory()*/);
    }
}