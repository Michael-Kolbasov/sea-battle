package game.launch;
import game.objects.field.GameMap;

public class AppLauncher {

    public void launchRandom() {
        GameMap playerMap = new GameMap();
        GameMap enemyMap = new GameMap();
        Input input = new Input();

        do {
            System.out.println("--------------- My map -----------------");
            playerMap.displayMap();
            System.out.println("------------- Enemy map ----------------");
            enemyMap.displayMap();
            input.process();
        } while (!input.isFinished());
    }
}