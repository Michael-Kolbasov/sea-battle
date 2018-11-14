package game.launch;
import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import game.objects.ships.Ship;

import java.io.BufferedReader;
import java.util.*;

public class GameProcess {
    private AppLauncher launcher;
    private GameMap playerMap;
    private GameMap aiMap;
    private boolean victory;

    public GameProcess(GameMap playerMap, GameMap aiMap, AppLauncher launcher) {
        this.playerMap = playerMap;
        this.aiMap = aiMap;
        this.launcher = launcher;
    }

    public boolean fire(String userInput) {
        if (checkOtherInput(userInput)) {
            return true;
        }
        int y = getY(userInput);
        int x = getX(userInput);
        Element[][] map = aiMap.getCells();
        map[y][x].setCellChecked(true);
        if (map[y][x].getState() == ElementState.SHIP) {
            Ship ship = GameMap.getShipFromMap(aiMap, y, x);
            if (ship != null) {
                boolean isShotAlready = false;
                Element elementInShip = ship.getElementByCoordinates(y, x);
                if (elementInShip.getState() != ElementState.CHECKED) {
                    ship.markHit(y, x);
                    map[y][x].setSymbol('X');
                } else {
                    isShotAlready = true;
                }
                if (ship.checkState()) {
                    if (!isShotAlready) {
                        System.out.println("Ship has drowned!");
                        Element[] shipBody = ship.getBody();
                        for (Element element : shipBody) {
                            ArrayList<Element> surround = element.getSurround();
                            for (Element elements : surround) {
                                map[elements.getY()][elements.getX()].setCellChecked(true);
                            }
                        }
                        waitOneSecond();
                        if (checkIsItVictory(aiMap)) {
                            System.out.println("Congratulations! You have won!");
                            for (int i = 0; i < map.length; i++) {
                                for (int j = 0; j < map[i].length; j++) {
                                    map[i][j].setCellChecked(true);
                                }
                            }
                            waitOneSecond();
                            playerMap.displayMap();
                            aiMap.displayEnemyMap();
                            victory = true;
                            return true;
                        } else {
                            aiMap.displayEnemyMap();
                            return true;
                        }
                    } else {
                        System.out.println("Ship has drowned! By the way, you've already shot this cell.");
                        waitOneSecond();
                        return false;
                    }
                } else {
                    if (!isShotAlready) {
                        System.out.println("It's a shot! Ship is injured.");
                    } else {
                        System.out.println("It's a shot! Ship is injured. By the way, you've already shot this cell.");
                        return false;
                    }
                    waitOneSecond();
                    aiMap.displayEnemyMap();
                    return true;
                }
            } else {
                throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                        "y = " + y + ", x = " + x);
            }
        } else {
            System.out.println("No luck this time. You have missed.");
            waitOneSecond();
            aiFire();
            return false;
        }
    }

    public boolean aiFire() {
        Random rand = new Random();
        int y = rand.nextInt(10);
        int x = rand.nextInt(10);
        Element[][] map = playerMap.getCells();
        if (map[y][x].getState() == ElementState.CHECKED) {
            return aiFire();
        } else {
            map[y][x].setCellChecked(true);
            Character yAsCharacter = (char) (y + 65);
            System.out.println("Computer shot cell " + yAsCharacter + x);
            waitOneSecond();
            if (map[y][x].getState() == ElementState.SHIP) {
                Ship ship = GameMap.getShipFromMap(playerMap, y, x);
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
                return true;
            } else {
                map[y][x].setSymbol('•');
                System.out.println("Computer missed");
                waitOneSecond();
                fire(CREATE_SOMETHING_WITHOUT_INPUT)
                return false;
            }
        }
    }

    public GameMap getPlayerMap() {
        return playerMap;
    }

    public GameMap getAiMap() {
        return aiMap;
    }

    public void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isVictory() {
        return victory;
    }

    private boolean checkIsItVictory(GameMap map) {
        return GameMap.countHowManyShipsLeft(map) == 0;
    }

    private boolean checkOtherInput(String userInput) {
        if (userInput.equalsIgnoreCase("ships")) {
            int shipsLeft = GameMap.countHowManyShipsLeft(aiMap);
            if (shipsLeft == 1) {
                System.out.println("There is " + GameMap.countHowManyShipsLeft(aiMap) + " ship left.");
            } else {
                System.out.println("There are " + GameMap.countHowManyShipsLeft(aiMap) + " ships left.");
            }
            aiMap.displayEnemyMap();
            waitOneSecond();
            return true;
        } else if (userInput.equalsIgnoreCase("quit")) {
            closeConnection();
            System.exit(0);
        } else if (userInput.equalsIgnoreCase("start")) {
            playerMap = null;
            aiMap = null;
            launcher = null;
            new AppLauncher().launchRandom();
        } else {

        }
        return false;
    }

    private BufferedReader closeConnection() {
        return Input.getReader();
    }

    private int getY(String input) {
        input = input.toUpperCase();
        return input.charAt(input.length() - 2) - 65;
    }

    private int getX(String input) {
        return Character.digit(input.charAt(input.length() - 1), 10);
    }
}