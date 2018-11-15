package game.players;

import game.launch.GameProcess;
import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import game.objects.ships.Ship;

import java.util.ArrayList;
import java.util.Random;

public class Computer extends AbstractPlayer {
    private boolean rememberCell;
    private int yToRemember;
    private int xToRemember;
    private ShootDirection direction;
    private ArrayList<ShootDirection> shootDirections;

    private enum ShootDirection {
        UP, DOWN, LEFT, RIGHT;

        private static ShootDirection getRandom() {
            return values()[(int) (Math.random() * values().length)];
        }
    }

    public Computer(GameProcess gameProcess) {
        super(gameProcess);
        shootDirections = refillDirections();
    }

    @Override
    public void displayMap() {
        map.displayEnemyMap();
    }

    @Override
    public void fire() {
        Random rand = new Random();
        GameMap enemyMap = gameProcess.getPlayerMap();
        Element[][] map = enemyMap.getCells();
        if (isCellRemembered()) {
            int y = get_yToRemember();
            int x = get_xToRemember();
            shootDirections = getShootDirections();
            direction = shootDirections.get((int) (Math.random() * shootDirections.size()));

            if (direction == ShootDirection.LEFT) {
                x -= 1;
                if (map[y][x].getState() == ElementState.CHECKED) {
                    shootDirections.remove(ShootDirection.LEFT);
                    fire();
                } else {
                    Character yAsCharacter = (char) (y + 65);
                    System.out.println("Computer shoots cell " + yAsCharacter + x);
                    map[y][x].setCellChecked(true);
                    waitOneSecond();
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
                                    System.out.println("Your ship has drowned.");
                                    setRememberCell(false);
                                    shootDirections = refillDirections();
                                    Element[] shipBody = ship.getBody();
                                    for (Element element : shipBody) {
                                        ArrayList<Element> surround = element.getSurround();
                                        for (Element elements : surround) {
                                            map[elements.getY()][elements.getX()].setCellChecked(true);
                                        }
                                    }
                                    waitOneSecond();
                                    if (checkIsItVictory(enemyMap)) {
                                        System.out.println();
                                        System.out.println("It's over! Computer has won.");
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
                                    System.out.println("Ship has drowned! Oh, it was already drowned, computer just wants to be sure.");
                                    waitOneSecond();
                                    setResult(false);
                                }
                            } else {
                                if (!isShotAlready) {
                                    System.out.println("It's a shot! Ship is injured.");
                                    setRememberCell(true);
                                    set_yToRemember(y);
                                    set_xToRemember(x);
                                    setResult(true);
                                } else {
                                    System.out.println("It's a shot! Ship is injured. Oh, and computer already shot this cell.");
                                    setResult(false);
                                }
                                waitOneSecond();
                            }
                        } else {
                            throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                                    "y = " + y + ", x = " + x);
                        }
                    } else {
                        map[y][x].setSymbol('•');
                        System.out.println("Computer missed");
                        waitOneSecond();
                        setResult(false);
                    }
                }
            } else if (direction == ShootDirection.RIGHT) {
                x += 1;
                if (map[y][x].getState() == ElementState.CHECKED) {
                    shootDirections.remove(ShootDirection.RIGHT);
                    fire();
                } else {
                    Character yAsCharacter = (char) (y + 65);
                    System.out.println("Computer shoots cell " + yAsCharacter + x);
                    map[y][x].setCellChecked(true);
                    waitOneSecond();
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
                                    System.out.println("Your ship has drowned.");
                                    setRememberCell(false);
                                    shootDirections = refillDirections();
                                    Element[] shipBody = ship.getBody();
                                    for (Element element : shipBody) {
                                        ArrayList<Element> surround = element.getSurround();
                                        for (Element elements : surround) {
                                            map[elements.getY()][elements.getX()].setCellChecked(true);
                                        }
                                    }
                                    waitOneSecond();
                                    if (checkIsItVictory(enemyMap)) {
                                        System.out.println();
                                        System.out.println("It's over! Computer has won.");
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
                                    System.out.println("Ship has drowned! Oh, it was already drowned, computer just wants to be sure.");
                                    waitOneSecond();
                                    setResult(false);
                                }
                            } else {
                                if (!isShotAlready) {
                                    System.out.println("It's a shot! Ship is injured.");
                                    setRememberCell(true);
                                    set_yToRemember(y);
                                    set_xToRemember(x);
                                    setResult(true);
                                } else {
                                    System.out.println("It's a shot! Ship is injured. Oh, and computer already shot this cell.");
                                    setResult(false);
                                }
                                waitOneSecond();
                            }
                        } else {
                            throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                                    "y = " + y + ", x = " + x);
                        }
                    } else {
                        map[y][x].setSymbol('•');
                        System.out.println("Computer missed");
                        waitOneSecond();
                        setResult(false);
                    }
                }
            } else if (direction == ShootDirection.UP) {
                y -= 1;
                if (map[y][x].getState() == ElementState.CHECKED) {
                    shootDirections.remove(ShootDirection.UP);
                    fire();
                } else {
                    Character yAsCharacter = (char) (y + 65);
                    System.out.println("Computer shoots cell " + yAsCharacter + x);
                    map[y][x].setCellChecked(true);
                    waitOneSecond();
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
                                    System.out.println("Your ship has drowned.");
                                    setRememberCell(false);
                                    shootDirections = refillDirections();
                                    Element[] shipBody = ship.getBody();
                                    for (Element element : shipBody) {
                                        ArrayList<Element> surround = element.getSurround();
                                        for (Element elements : surround) {
                                            map[elements.getY()][elements.getX()].setCellChecked(true);
                                        }
                                    }
                                    waitOneSecond();
                                    if (checkIsItVictory(enemyMap)) {
                                        System.out.println();
                                        System.out.println("It's over! Computer has won.");
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
                                    System.out.println("Ship has drowned! Oh, it was already drowned, computer just wants to be sure.");
                                    waitOneSecond();
                                    setResult(false);
                                }
                            } else {
                                if (!isShotAlready) {
                                    System.out.println("It's a shot! Ship is injured.");
                                    setRememberCell(true);
                                    set_yToRemember(y);
                                    set_xToRemember(x);
                                    setResult(true);
                                } else {
                                    System.out.println("It's a shot! Ship is injured. Oh, and computer already shot this cell.");
                                    setResult(false);
                                }
                                waitOneSecond();
                            }
                        } else {
                            throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                                    "y = " + y + ", x = " + x);
                        }
                    } else {
                        map[y][x].setSymbol('•');
                        System.out.println("Computer missed");
                        waitOneSecond();
                        setResult(false);
                    }
                }
            } else if (direction == ShootDirection.DOWN) {
                y += 1;
                if (map[y][x].getState() == ElementState.CHECKED) {
                    shootDirections.remove(ShootDirection.DOWN);
                    fire();
                } else {
                    Character yAsCharacter = (char) (y + 65);
                    System.out.println("Computer shoots cell " + yAsCharacter + x);
                    map[y][x].setCellChecked(true);
                    waitOneSecond();
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
                                    System.out.println("Your ship has drowned.");
                                    setRememberCell(false);
                                    shootDirections = refillDirections();
                                    Element[] shipBody = ship.getBody();
                                    for (Element element : shipBody) {
                                        ArrayList<Element> surround = element.getSurround();
                                        for (Element elements : surround) {
                                            map[elements.getY()][elements.getX()].setCellChecked(true);
                                        }
                                    }
                                    waitOneSecond();
                                    if (checkIsItVictory(enemyMap)) {
                                        System.out.println();
                                        System.out.println("It's over! Computer has won.");
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
                                    System.out.println("Ship has drowned! Oh, it was already drowned, computer just wants to be sure.");
                                    waitOneSecond();
                                    setResult(false);
                                }
                            } else {
                                if (!isShotAlready) {
                                    System.out.println("It's a shot! Ship is injured.");
                                    setRememberCell(true);
                                    set_yToRemember(y);
                                    set_xToRemember(x);
                                    setResult(true);
                                } else {
                                    System.out.println("It's a shot! Ship is injured. Oh, and computer already shot this cell.");
                                    setResult(false);
                                }
                                waitOneSecond();
                            }
                        } else {
                            throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                                    "y = " + y + ", x = " + x);
                        }
                    } else {
                        map[y][x].setSymbol('•');
                        System.out.println("Computer missed");
                        waitOneSecond();
                        setResult(false);
                    }
                }
            }
        } else {
            int y = rand.nextInt(10);
            int x = rand.nextInt(10);
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
                        ship.markHit(y, x);
                        map[y][x].setSymbol('X');
                        System.out.println("Computer hits the ship!");
                        if (ship.checkState()) {
                            System.out.println("Your ship has drowned.");
                            Element[] shipBody = ship.getBody();
                            for (Element element : shipBody) {
                                ArrayList<Element> surround = element.getSurround();
                                for (Element elements : surround) {
                                    map[elements.getY()][elements.getX()].setCellChecked(true);
                                }
                            }
                            waitOneSecond();
                            if (checkIsItVictory(enemyMap)) {
                                System.out.println();
                                System.out.println("It's over! Computer has won.");
                                for (int i = 0; i < map.length; i++) {
                                    for (int j = 0; j < map[i].length; j++) {
                                        map[i][j].setCellChecked(true);
                                    }
                                }
                                waitOneSecond();
                                setVictory(true);
                                setResult(false);
                            }
                        } else {
                            setRememberCell(true);
                            set_yToRemember(y);
                            set_xToRemember(x);
                        }
                        waitOneSecond();
                        setResult(true);
                    } else {
                        throw new IllegalArgumentException("Something's gone wrong. No ship found at this coordinates: " +
                                "y = " + y + ", x = " + x);
                    }
                } else {
                    map[y][x].setSymbol('•');
                    System.out.println("Computer missed");
                    waitOneSecond();
                    setResult(false);
                }
            }
        }
    }

    private boolean hitNewCoordinates(int y, int x) {

    }

    private boolean isCellRemembered() {
        return rememberCell;
    }

    private void setRememberCell(boolean rememberCell) {
        this.rememberCell = rememberCell;
    }

    private int get_yToRemember() {
        return yToRemember;
    }

    private int get_xToRemember() {
        return xToRemember;
    }

    private void set_yToRemember(int yToRemember) {
        this.yToRemember = yToRemember;
    }

    private void set_xToRemember(int xToRemember) {
        this.xToRemember = xToRemember;
    }

    private ArrayList<ShootDirection> getShootDirections() {
        return shootDirections;
    }

    private ArrayList<ShootDirection> refillDirections() {
        ArrayList<ShootDirection> directions = new ArrayList<>(4);
        directions.add(ShootDirection.DOWN);
        directions.add(ShootDirection.UP);
        directions.add(ShootDirection.LEFT);
        directions.add(ShootDirection.RIGHT);
        return directions;
    }
}