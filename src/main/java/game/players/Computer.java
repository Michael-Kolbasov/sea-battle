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
    private int initial_yToRemember;
    private int initial_xToRemember;
    private boolean huntStarted;
    private int huntingX;
    private int huntingY;
    private ShootDirection direction;
    private ArrayList<ShootDirection> shootDirections;

    private enum ShootDirection {
        UP {
            public ShootDirection opposite() {
                return ShootDirection.DOWN;
            }
        },
        DOWN {
            public ShootDirection opposite() {
                return ShootDirection.UP;
            }
        },
        LEFT {
            public ShootDirection opposite() {
                return ShootDirection.RIGHT;
            }
        },
        RIGHT {
            public ShootDirection opposite() {
                return ShootDirection.LEFT;
            }
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
        GameMap enemyMap = gameProcess.getPlayerMap();
        Element[][] map = enemyMap.getCells();
        if (isCellRemembered()) {
            int y = get_yToRemember();
            int x = get_xToRemember();
            shootDirections = getShootDirections();
            direction = getRandomDirection();
            setNewCoordinates(y, x, direction, enemyMap, map);
        } else {
            Random rand = new Random();
            int y = rand.nextInt(10);
            int x = rand.nextInt(10);
            performHit(y, x, null, enemyMap, map);
        }
    }

    private void setNewCoordinates(int y, int x, ShootDirection direction, GameMap enemyMap, Element[][] map) {
        if (direction == ShootDirection.LEFT) {
            if (x == 0) {
                shootDirections.remove(ShootDirection.LEFT);
                fire();
            } else {
                x -= 1;
                performHit(y, x, direction, enemyMap, map);
            }
        } else if (direction == ShootDirection.RIGHT) {
            if (x == 9) {
                shootDirections.remove(ShootDirection.RIGHT);
                fire();
            } else {
                x += 1;
                performHit(y, x, direction, enemyMap, map);
            }
        } else if (direction == ShootDirection.UP) {
            if (y == 0) {
                shootDirections.remove(ShootDirection.UP);
                fire();
            } else {
                y -= 1;
                performHit(y, x, direction, enemyMap, map);
            }
        } else if (direction == ShootDirection.DOWN) {
            if (y == 9) {
                shootDirections.remove(ShootDirection.DOWN);
                fire();
            } else {
                y += 1;
                performHit(y, x, direction, enemyMap, map);
            }
        }
    }

    /*
     shootDirections.remove(direction.opposite());
     */

    private void performHit(int y, int x, ShootDirection direction, GameMap enemyMap, Element[][] map) {
        if (map[y][x].getState() == ElementState.CHECKED) {
            if (direction != null) {
                shootDirections.remove(direction);
                fire ();
            } else {
                fire();
            }
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
                            setRememberCell(false);
                            shootDirections = refillDirections();
                            waitOneSecond();
                            setResult(false);
                        }
                    } else {
                        if (!isShotAlready) {
                            System.out.println("It's a shot! Ship is injured.");
                            setRememberCell(true);
                            setInitial_yToRemember(y);
                            setInitial_xToRemember(x);
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

    private boolean isCellRemembered() {
        return rememberCell;
    }

    private void setRememberCell(boolean rememberCell) {
        this.rememberCell = rememberCell;
    }

    private int get_yToRemember() {
        return initial_yToRemember;
    }

    private int get_xToRemember() {
        return initial_xToRemember;
    }

    private void setInitial_yToRemember(int yToRemember) {
        this.initial_yToRemember = yToRemember;
    }

    private void setInitial_xToRemember(int xToRemember) {
        this.initial_xToRemember = xToRemember;
    }

    private ArrayList<ShootDirection> getShootDirections() {
        return shootDirections;
    }

    private ShootDirection getRandomDirection() {
        return shootDirections.get((int) (Math.random() * shootDirections.size()));
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