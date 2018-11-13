package game.objects.ships;
import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import java.util.Random;

public abstract class Ship implements ShipBehavior {
    private static int resetCount = 0;

    public enum Health {
        HEALTHY, INJURED, DEAD
    }

    private enum Orientation {
        HORIZONTAL, VERTICAL
    }

    protected Element[] body;
    protected int length;
    private Orientation orientation;
    private Health health = Health.HEALTHY;
    private boolean orientationLock;
    private int hitCount;

    @Override
    public int hashCode() {
        Element[] elements = getBody();
        int result = 0;
        for (int i = 0; i < elements.length; i++) {
            result += elements[i].hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Ship other = (Ship) obj;
        Element[] thisElements = getBody();
        Element[] otherElements = other.getBody();
        if (thisElements.length != otherElements.length) {
            return false;
        }
        for (int i = 0; i < getBody().length; i++) {
            if (!thisElements[i].equals(otherElements[i])) {
                return false;
            }
        }
        return true;
    }

    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }

    public Element getElementByCoordinates(int y, int x) {
        Element element = new Element(y, x);
        for (Element elementToFind : getBody()) {
            if (elementToFind.equals(element)) {
                return elementToFind;
            }
        }
        return null;
    }

    @Override
    public void markHit(int y, int x) {
        Element elementToMark = new Element(y, x);
        Element[] elements = getBody();
        for (Element element : elements) {
            if (element.equals(elementToMark)) {
                element.setSymbol('X');
                element.setState(ElementState.INJURED);
            }
        }
        hitCount++;
        if (checkState()) {
            setHealth(Health.DEAD);
        } else {
            setHealth(Health.INJURED);
        }
    }

    @Override
    public boolean checkState() {
        return hitCount >= body.length;
    }

    @Override
    public void fillBody() {
        for (int i = 0; i < body.length; i++) {
            body[i] = new Element();
            body[i].setSymbol('O');
            body[i].setState(ElementState.SHIP);
        }
    }

    @Override
    public void setRandomCoordinates() {
        Random rand = new Random();
        int setOrientation = rand.nextInt(2);
        if (!orientationLock) {
            orientation = setOrientation == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        }
        int x = rand.nextInt(10);
        int y = rand.nextInt(10);
        if (orientation == Orientation.VERTICAL) {
            if (y > GameMap.HEIGTH - body.length) {
                if (resetCount == 10) {
                    resetCount = 0;
                    orientation = Orientation.HORIZONTAL;
                    orientationLock = true;
                }
                resetCount++;
                this.resetCoordinates();
                this.setRandomCoordinates();
            } else {
                for (int i = 0; i < body.length; i++) {
                    body[i].setY(y++);
                    body[i].setX(x);
                }
            }
        } else if (orientation == Orientation.HORIZONTAL) {
            if (x > GameMap.WIDTH - body.length) {
                if (resetCount == 10) {
                    resetCount = 0;
                    orientation = Orientation.VERTICAL;
                    orientationLock = true;
                }
                resetCount++;
                this.resetCoordinates();
                this.setRandomCoordinates();
            } else {
                for (int i = 0; i < body.length; i++) {
                    body[i].setY(y);
                    body[i].setX(x++);
                }
            }
        }
    }

    public int getLength() {
        return length;
    }

    public Element[] getBody() {
        return body;
    }

    private void resetCoordinates() {
        for (int i = 0; i < body.length; i++) {
            body[i].setX(0);
            body[i].setY(0);
        }
    }
}