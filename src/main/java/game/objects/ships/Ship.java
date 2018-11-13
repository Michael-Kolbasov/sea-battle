package game.objects.ships;
import game.objects.Element;
import game.objects.ElementState;
import game.objects.field.GameMap;
import java.util.Random;

public abstract class Ship implements ShipBehavior {
    private static int resetCount = 0;

    private enum Orientation {
        HORIZONTAL, VERTICAL
    }

    protected Element[] body;
    protected int length;
    private Orientation orientation;
    private boolean orientationLock;
    private int hitCount;

    @Override
    public void markHit(int position) {
        body[position].setSymbol('x');
        hitCount++;
    }

    @Override
    public boolean checkState() {
        return hitCount == body.length;
    }

    @Override
    public void fillBody() {
        for (int i = 0; i < body.length; i++) {
            body[i] = new Element();
            body[i].setSymbol('o');
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
                resetCoordinates();
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
                resetCoordinates();
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