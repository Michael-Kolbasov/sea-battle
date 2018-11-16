package game.players;
import game.launch.GameProcess;
import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import game.objects.ships.Ship;
import java.util.ArrayList;
import java.util.Random;

public class Computer extends AbstractPlayer {
    private boolean firstHit;
    private boolean directionLock;
    private int firstHitY;
    private int firstHitX;
    private int updatedY;
    private int updatedX;
    private ShootDirection direction;
    private ArrayList<ShootDirection> listOfDirections;

    private enum ShootDirection {
        UP, DOWN, LEFT, RIGHT;

        public ShootDirection revert() {
            if (this == UP) {
                return DOWN;
            } else if (this == DOWN) {
                return UP;
            } else if (this == LEFT) {
                return RIGHT;
            } else if (this == RIGHT) {
                return LEFT;
            } else {
                return null;
            }
        }
    }

    public Computer(GameProcess gameProcess) {
        super(gameProcess);
        listOfDirections = refillDirections();
    }

    @Override
    public void fire() {
        GameMap enemyMap = gameProcess.getPlayerMap();
        Element[][] cells = enemyMap.getCells();
        if (isFirstHit()) {
            if (isDirectionLocked()) {
                setUpNewCoordinates(getUpdatedY(), getUpdatedX(), direction, enemyMap, cells);
                performHit(getUpdatedY(), getUpdatedX(), direction, enemyMap, cells);
            } else {
                performUpdatedHit(getUpdatedY(), getUpdatedX(), enemyMap, cells);
            }
        } else {
            Random rand = new Random();
            int y = rand.nextInt(10);
            int x = rand.nextInt(10);
            performHit(y, x, direction, enemyMap, cells);
        }
    }

    private void performHit(int y, int x, ShootDirection direction, GameMap enemyMap, Element[][] cells) {
        if (cells[y][x].isCellChecked()) {
            fire();
        } else {
            if (direction == null) {
                Character yAsCharacter = (char) (y + 65);
                System.out.println("Computer shoots cell " + yAsCharacter + x);
                if (cells[y][x].getState() == ElementState.SHIP) {
                    madeShot(y, x, enemyMap, cells);
                } else {
                    madeMiss(y, x, cells);
                }
            } else {
                setUpNewCoordinates(getUpdatedY(), getUpdatedX(), direction, enemyMap, cells);
                performUpdatedHit(getUpdatedY(), getUpdatedX(), enemyMap, cells);
            }
        }
    }

    private void setUpNewCoordinates(int y, int x, ShootDirection direction, GameMap enemyMap, Element[][] cells) {
        if (direction == null) {
            direction = getRandomDirection();
        }
        if (direction == ShootDirection.LEFT) {
            if (x == 0) {
                listOfDirections.remove(ShootDirection.LEFT);
                direction = getRandomDirection();
                setUpNewCoordinates(y, x, direction, enemyMap, cells);
            } else {
                setUpdatedX(x - 1);
            }
        } else if (direction == ShootDirection.RIGHT) {
            if (x == 9) {
                listOfDirections.remove(ShootDirection.RIGHT);
                direction = getRandomDirection();
                setUpNewCoordinates(y, x, direction, enemyMap, cells);
            } else {
                setUpdatedX(x + 1);
            }
        } else if (direction == ShootDirection.UP) {
            if (y == 0) {
                listOfDirections.remove(ShootDirection.UP);
                direction = getRandomDirection();
                setUpNewCoordinates(y, x, direction, enemyMap, cells);
            } else {
                setUpdatedY(y - 1);
            }
        } else if (direction == ShootDirection.DOWN) {
            if (y == 9) {
                listOfDirections.remove(ShootDirection.DOWN);
                direction = getRandomDirection();
                setUpNewCoordinates(y, x, direction, enemyMap, cells);
            } else {
                setUpdatedY(y + 1);
            }
        }
    }

    private void performUpdatedHit(int y, int x, GameMap enemyMap, Element[][] cells) {
        if (cells[y][x].isCellChecked()) {
            setUpNewCoordinates(y, x, direction, enemyMap, cells);
            performUpdatedHit(getUpdatedY(), getUpdatedX(), enemyMap, cells);
        } else {
            Character yAsCharacter = (char) (getUpdatedY() + 65);
            System.out.println("Computer shoots cell " + yAsCharacter + x);
            cells[y][x].setCellChecked(true);
            waitOneSecond();
            if (cells[y][x].getState() == ElementState.SHIP) {
                madeShot(y, x, enemyMap, cells);
            } else {
                madeMiss(y, x, cells);
            }
        }
    }

    private void madeShot(int y, int x, GameMap enemyMap, Element[][] cells) {
        Ship ship = GameMap.getShipFromMap(enemyMap, y, x);
        if (ship == null) {
            madeMiss(y, x, cells);
            this.direction = direction.revert();
            setUpdatedY(getFirstHitY());
            setUpdatedX(getFirstHitX());
        } else {
            ship.markHit(y, x);
            cells[y][x].setCellChecked(true);
            cells[y][x].setSymbol('X');
            if (ship.checkIsDead()) {
                this.direction = null;
                setFirstHit(false);
                setDirectionLock(false);
                listOfDirections = refillDirections();
                System.out.println("Your ship has drowned.");
                markSurroundChecked(ship, cells);
                waitOneSecond();
                if (checkIsItVictory(enemyMap)) {
                    System.out.println();
                    System.out.println("It's over! Computer has won.");
                    markMapChecked(cells);
                    waitOneSecond();
                    setVictory(true);
                    setResult(false);
                } else {
                    setResult(true);
                }
            } else {
                System.out.println("Computer hits the ship!");
                setResult(true);
                if (!isFirstHit()) {
                    startHunt(y, x);
                    setUpNewCoordinates(y, x, direction, enemyMap, cells);
                } else {
                    setDirectionLock(true);
                }
            }
        }
    }

    private void startHunt(int y, int x) {
        setFirstHit(true);
        setFirstHitX(x);
        setFirstHitY(y);
        setUpdatedX(x);
        setUpdatedY(y);
    }

    private void madeMiss(int y, int x, Element[][] cells) {
        cells[y][x].setCellChecked(true);
        cells[y][x].setSymbol('•');
        System.out.println("Computer missed");
        if (isFirstHit()) {
            setUpdatedY(getFirstHitY());
            setUpdatedX(getFirstHitX());
        }
        if (isDirectionLocked()) {
            this.direction = direction.revert();
        }
        waitOneSecond();
        setResult(false);
    }

    private void markMapChecked(Element[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].setCellChecked(true);
            }
        }
    }

    private void markSurroundChecked(Ship ship, Element[][] cells) {
        Element[] shipBody = ship.getBody();
        for (Element element : shipBody) {
            ArrayList<Element> surround = element.getSurround();
            for (Element elements : surround) {
                cells[elements.getY()][elements.getX()].setCellChecked(true);
            }
        }
    }

    private ArrayList<ShootDirection> refillDirections() {
        ArrayList<ShootDirection> directions = new ArrayList<>(4);
        directions.add(ShootDirection.DOWN);
        directions.add(ShootDirection.UP);
        directions.add(ShootDirection.LEFT);
        directions.add(ShootDirection.RIGHT);
        return directions;
    }

    private ShootDirection getRandomDirection() {
        return listOfDirections.get((int) (Math.random() * listOfDirections.size() - 1));
    }

    private boolean isFirstHit() {
        return firstHit;
    }

    private void setFirstHit(boolean firstHit) {
        this.firstHit = firstHit;
    }

    private int getFirstHitY() {
        return firstHitY;
    }

    private int getFirstHitX() {
        return firstHitX;
    }

    private void setFirstHitY(int firstHitY) {
        this.firstHitY = firstHitY;
    }

    private void setFirstHitX(int firstHitX) {
        this.firstHitX = firstHitX;
    }

    private int getUpdatedY() {
        return updatedY;
    }

    private void setUpdatedY(int updatedY) {
        this.updatedY = updatedY;
    }

    private int getUpdatedX() {
        return updatedX;
    }

    private void setUpdatedX(int updatedX) {
        this.updatedX = updatedX;
    }

    private boolean isDirectionLocked() {
        return directionLock;
    }

    private void setDirectionLock(boolean directionLock) {
        this.directionLock = directionLock;
    }
}