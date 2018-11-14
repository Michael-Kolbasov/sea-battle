package game.players;

import game.launch.GameProcess;
import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import game.objects.ships.Ship;

import java.util.Random;

public class Computer extends AbstractPlayer {

    public Computer(GameProcess gameProcess) {
        super(gameProcess);
    }

    @Override
    public void displayMap() {
        map.displayEnemyMap();
    }

    @Override
    public void fire() {
        Random rand = new Random();
        int y = rand.nextInt(10);
        int x = rand.nextInt(10);
        GameMap enemyMap = gameProcess.getPlayerMap();
        Element[][] map = enemyMap.getCells();
        if (map[y][x].getState() == ElementState.CHECKED) {
            fire();
        } else {
            map[y][x].setCellChecked(true);
            Character yAsCharacter = (char) (y + 65);
            System.out.println("Computer shoots cell " + yAsCharacter + x);
            waitOneSecond();
            if (map[y][x].getState() == ElementState.SHIP) {
                Ship ship = GameMap.getShipFromMap(enemyMap, y, x);
                if (ship != null) {
                    Element elementInShip = ship.getElementByCoordinates(y, x);
                    ship.markHit(y, x);
                    map[y][x].setSymbol('X');
                } else {
                    throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                            "y = " + y + ", x = " + x);
                }
                System.out.println("Computer hits the ship!");
                waitOneSecond();
                setResult(true);
            } else {
                map[y][x].setSymbol('•');
                System.out.println("Computer missed");
                waitOneSecond();
                setResult(false);
            }
        }
    }
}
