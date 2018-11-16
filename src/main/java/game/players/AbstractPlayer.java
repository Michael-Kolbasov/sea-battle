package game.players;
import game.launch.GameProcess;
import game.objects.Element;
import game.objects.field.GameMap;
import game.objects.ships.Ship;

import java.util.ArrayList;

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

    public GameMap getMap() {
        return map;
    }

    public boolean getVictory() {
        return victory;
    }

    public boolean getResult() {
        return result;
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

    protected void setResult(boolean result) {
        this.result = result;
    }

    protected void setVictory(boolean victory) {
        this.victory = victory;
    }
}