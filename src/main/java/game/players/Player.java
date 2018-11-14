package game.players;
import game.launch.AppLauncher;
import game.launch.GameProcess;
import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import game.objects.ships.Ship;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player extends AbstractPlayer {
    private Pattern firePattern = Pattern.compile("fire ([a-j][0-9]){1,1}");
    private Matcher fireMatcher;
    private String userInput;

    public Player(GameProcess gameProcess) {
        super(gameProcess);
    }

    @Override
    public void fire() {
        userInput = getUserInput();
        if (checkOtherInput(userInput)) {
            userInput = getUserInput();
        }
        int y = getY(userInput);
        int x = getX(userInput);
        GameMap enemyMap = gameProcess.getEnemyMap();
        Element[][] map = enemyMap.getCells();
        map[y][x].setCellChecked(true);
        if (map[y][x].getState() == ElementState.SHIP) {
            Ship ship = GameMap.getShipFromMap(enemyMap, y, x);
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
                        if (checkIsItVictory(enemyMap)) {
                            System.out.println("Congratulations! You have won!");
                            for (int i = 0; i < map.length; i++) {
                                for (int j = 0; j < map[i].length; j++) {
                                    map[i][j].setCellChecked(true);
                                }
                            }
                            waitOneSecond();
                            setVictory(true);
                            setResult(false);
                        } else {
                            setResult(true);
                        }
                    } else {
                        System.out.println("Ship has drowned! By the way, you've already shot this cell.");
                        waitOneSecond();
                        setResult(false);
                    }
                } else {
                    if (!isShotAlready) {
                        System.out.println("It's a shot! Ship is injured.");
                        setResult(true);
                    } else {
                        System.out.println("It's a shot! Ship is injured. By the way, you've already shot this cell.");
                        setResult(false);
                    }
                    waitOneSecond();
                }
            } else {
                throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                        "y = " + y + ", x = " + x);
            }
        } else {
            System.out.println("No luck this time. You have missed.");
            waitOneSecond();
            setResult(false);
        }
    }

    private String getUserInput() {
        String userInput = "";
        try {
            userInput = GameProcess.getReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInput;
    }

    private boolean checkOtherInput(String userInput) {
        fireMatcher = firePattern.matcher(userInput);
        if (fireMatcher.matches()) {
            fireMatcher.reset();
            return false;
        } else if (userInput.equalsIgnoreCase("ships")) {
            GameMap enemyMap = gameProcess.getEnemyMap();
            int shipsLeft = GameMap.countHowManyShipsLeft(enemyMap);
            if (shipsLeft == 1) {
                System.out.println("There is " + GameMap.countHowManyShipsLeft(enemyMap) + " ship left.");
            } else {
                System.out.println("There are " + GameMap.countHowManyShipsLeft(enemyMap) + " ships left.");
            }
            enemyMap.displayEnemyMap();
            waitOneSecond();
            return true;
        } else if (userInput.equalsIgnoreCase("quit")) {
            GameProcess.closeConnection();
            System.exit(0);
        } else if (userInput.equalsIgnoreCase("menu")) {
            new AppLauncher().launchRandom();
        } else {
            System.out.println("Wrong input");
            checkOtherInput(getUserInput());
        }
        return false;
    }
}