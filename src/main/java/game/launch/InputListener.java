package game.launch;

import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import game.objects.ships.Ship;

import java.util.*;

public class InputListener {
    private GameMap playerMap;
    private GameMap aiMap;
    private ArrayList<Ship> playerShips;
    private ArrayList<Ship> aiShips;

    public InputListener(GameMap playerMap, GameMap aiMap) {
        this.playerMap = playerMap;
        this.aiMap = aiMap;
        this.playerShips = playerMap.getShips();
        this.aiShips = aiMap.getShips();
    }

    public boolean fire(String userInput) {
        int y = getY(userInput);
        int x = getX(userInput);
        Element[][] map = aiMap.getCells();
        map[y][x].setCellChecked(true);
        if (map[y][x].getState() == ElementState.SHIP) {
            Ship ship = GameMap.getShip(aiMap, y, x);
            if (ship != null) {
                boolean isShotAlready = false;
                Element elementInShip = ship.getElementByCoordinates(y, x);
                if (elementInShip.getState() != ElementState.INJURED) {
                    ship.markHit(y, x);
                    map[y][x].setSymbol('X');
                } else {
                    isShotAlready = true;
                }
                if (ship.checkState()) {
                    if (!isShotAlready) {
                        System.out.println("Ship has drowned!");
                    } else {
                        System.out.println("Ship has drowned! By the way, you've already shot this cell.");
                    }
                    Element[] shipBody = ship.getBody();
                    for (Element element : shipBody) {
                        ArrayList<Element> surround = element.getSurround();
                        for (Element elements : surround) {
                            map[elements.getY()][elements.getX()].setCellChecked(true);
                        }
                    }
                    waitOneSecond();
                    System.out.println("------------- Enemy map ----------------");
                    aiMap.displayEnemyMap();
                    return true;
                } else {
                    if (!isShotAlready) {
                        System.out.println("It's a shot! Ship is injured.");
                    } else {
                        System.out.println("It's a shot! Ship is injured. By the way, you've already shot this cell.");
                    }
                    waitOneSecond();
                    aiMap.displayEnemyMap();
                    return true;
                }
            } else {
                System.err.println("Somethings gone wrong. No ship found at this coordinates: y = " + y + " x = " + x);
                waitOneSecond();
                return false;
            }
        } else {
            System.out.println("No luck this time. You have missed.");
            waitOneSecond();
            return false;
        }
    }

    public boolean aiFire() {
        return false;
    }

    private int getY(String input) {
        input = input.toUpperCase();
        return input.charAt(input.length() - 2) - 65;
    }

    private int getX(String input) {
        return Character.digit(input.charAt(input.length() - 1), 10);
    }

    private void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
