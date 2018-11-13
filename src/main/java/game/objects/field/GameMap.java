package game.objects.field;

import game.objects.ElementState;
import game.objects.ships.Ship;
import game.objects.Element;
import game.objects.ships.ShipFactory;

import java.util.ArrayList;

public class GameMap {
    public final static int WIDTH = 10;
    public final static int HEIGTH = 10;
    private final Element[][] cells;
    private final ArrayList<Ship> ships = new ArrayList<>(10);
    private final ShipFactory factory = new ShipFactory();

    public GameMap() {
        cells = new Element[WIDTH][HEIGTH];
        fillMap();
        createShips();
        putShipsRandomly();
    }

    public static Ship getShip(GameMap map, int y, int x) {
        ArrayList<Ship> ships = map.getShips();
        Element elementToCheck = new Element(y, x);
        for (Ship ship : ships) {
            Element[] elements = ship.getBody();
            for (Element element : elements) {
                if (elementToCheck.equals(element)) {
                    return ship;
                }
            }
        }
        return null;
    }

    public Element[][] getCells() {
        return cells;
    }

    public void displayMap() {
        char coordinateX = 'A';
        int coordinateY = 0;

        System.out.print("  ");
        for (int x = 0; x < WIDTH; x++) {
            System.out.print(coordinateY++ + "   ");
        }
        System.out.println();

        for (int y = 0; y < HEIGTH; y++) {
            System.out.print(coordinateX++ + " ");
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(cells[y][x].getSymbol() + " ");
                if (x != 9) {
                    System.out.print("| ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void displayEnemyMap() {
        char coordinateX = 'A';
        int coordinateY = 0;

        System.out.print("  ");
        for (int x = 0; x < WIDTH; x++) {
            System.out.print(coordinateY++ + "   ");
        }
        System.out.println();

        for (int y = 0; y < HEIGTH; y++) {
            System.out.print(coordinateX++ + " ");
            for (int x = 0; x < WIDTH; x++) {
                if (!(cells[y][x].isCellChecked())) {
                    System.out.print(cells[y][x].getSurrogate() + " ");
                } else {
                    System.out.print(cells[y][x].getSymbol() + " ");
                }
                if (x != 9) {
                    System.out.print("| ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    private void fillMap() {
        for (int y = 0; y < HEIGTH; y++) {
            for (int x = 0; x < WIDTH; x++) {
                cells[y][x] = new Element();
                cells[y][x].setSymbol(' ');
                cells[y][x].setState(ElementState.WATER);
                cells[y][x].setY(y);
                cells[y][x].setX(x);
            }
        }
    }

    private void createShips() {
        ships.add(factory.createShip(4));
        for (int i = 0; i < 2; i++) {
            ships.add(factory.createShip(3));
        }
        for (int i = 0; i < 3; i++) {
            ships.add(factory.createShip(2));
        }
        for (int i = 0; i < 4; i++) {
            ships.add(factory.createShip(1));
        }
    }

    private void markBorder(Element[][] array, int y, int x) {
        Element input = new Element(y, x);
        ArrayList<Element> surround = input.getSurround();
        for (Element element : surround) {
            if (array[element.getY()][element.getX()].getState() != ElementState.SHIP) {
                array[element.getY()][element.getX()].setState(ElementState.BORDER);
            }
        }
    }

    private boolean checkCoordinates(Element[][] array, Ship ship) {
        Element[] shipElements = ship.getBody();
        for (Element element : shipElements) {
            if (!(array[element.getY()][element.getX()].checkEmptiness())) {
                return false;
            }
            ArrayList<Element> surround = element.getSurround();
            for (Element elements : surround) {
                if (!(array[elements.getY()][elements.getX()].checkEmptiness())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void putShipsRandomly() {
        for (Ship ship : ships) {
            do {
                ship.setRandomCoordinates();
            } while (!checkCoordinates(cells, ship));
            for (int i = 0; i < ship.getLength(); i++) {
                Element element = ship.getBody()[i];
                int elementY = element.getY();
                int elementX = element.getX();
                cells[elementY][elementX].setSymbol(element.getSymbol());
                cells[elementY][elementX].setState(element.getState());
                markBorder(cells, elementY, elementX);
            }
        }
    }
}
