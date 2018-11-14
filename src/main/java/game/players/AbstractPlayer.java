package game.players;
import game.launch.GameProcess;
import game.objects.field.GameMap;

public abstract class AbstractPlayer {
    protected GameMap map;
    protected GameProcess gameProcess;
    protected boolean result;
    private boolean victory;

    public AbstractPlayer(GameProcess gameProcess) {
        map = new GameMap();
        this.gameProcess = gameProcess;
    }

    public abstract void fire();

    public void displayMap() {
        map.displayMap();
    }

    protected boolean checkIsItVictory(GameMap map) {
        return GameMap.countHowManyShipsLeft(map) == 0;
    }

    protected void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean getResult() {
        return result;
    }

    protected void setResult(boolean result) {
        this.result = result;
    }

    protected void setVictory(boolean victory) {
        this.victory = victory;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean getVictory() {
        return victory;
    }

    protected int getY(String input) {
        input = input.toUpperCase();
        return input.charAt(input.length() - 2) - 65;
    }

    protected int getX(String input) {
        return Character.digit(input.charAt(input.length() - 1), 10);
    }
}
