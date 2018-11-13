package game.objects;
import java.util.ArrayList;

public class Element {
    private int x;
    private int y;
    private ElementState state;
    private char symbol;

    public Element() {

    }

    public Element(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public boolean checkEmptiness() {
        return state != ElementState.SHIP;
    }

    public ArrayList<Element> getSurround() {
        ArrayList<Element> elements = new ArrayList<>();
        if ((getX() != 0 && getX() != 9) && (getY() != 0 && getY() != 9)) {
            Element e1 = new Element(getY() + 1, getX() + 1);
            Element e2 = new Element(getY() + 1, getX());
            Element e3 = new Element(getY() + 1, getX() - 1);
            Element e4 = new Element(getY(), getX() - 1);
            Element e5 = new Element(getY() - 1, getX() - 1);
            Element e6 = new Element(getY() - 1, getX());
            Element e7 = new Element(getY() - 1, getX() + 1);
            Element e8 = new Element(getY(), getX() + 1);
            elements.add(e1);
            elements.add(e2);
            elements.add(e3);
            elements.add(e4);
            elements.add(e5);
            elements.add(e6);
            elements.add(e7);
            elements.add(e8);
        } else if (y == 0) {
            if (x == 0) {
                Element e1 = new Element(getY(), getX() + 1);
                Element e2 = new Element(getY() + 1, getX() + 1);
                Element e3 = new Element(getY() + 1, getX());
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
            } else if (x == 9) {
                Element e1 = new Element(getY() + 1, getX());
                Element e2 = new Element(getY() + 1, getX() - 1);
                Element e3 = new Element(getY(), getX() - 1);
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
            } else {
                Element e1 = new Element(getY(), getX() - 1);
                Element e2 = new Element(getY() + 1, getX() - 1);
                Element e3 = new Element(getY() + 1, getX());
                Element e4 = new Element(getY() + 1, getX() + 1);
                Element e5 = new Element(getY(), getX() + 1);
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
                elements.add(e4);
                elements.add(e5);
            }
        } else if (y == 9) {
            if (x == 0) {
                Element e1 = new Element(getY() - 1, getX());
                Element e2 = new Element(getY() - 1, getX() + 1);
                Element e3 = new Element(getY(), getX() + 1);
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
            } else if (x == 9) {
                Element e1 = new Element(getY() - 1, getX());
                Element e2 = new Element(getY() - 1, getX() - 1);
                Element e3 = new Element(getY(), getX() - 1);
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
            } else {
                Element e1 = new Element(getY(), getX() - 1);
                Element e2 = new Element(getY() - 1, getX() - 1);
                Element e3 = new Element(getY() - 1, getX());
                Element e4 = new Element(getY() - 1, getX() + 1);
                Element e5 = new Element(getY(), getX() + 1);
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
                elements.add(e4);
                elements.add(e5);
            }
        } else if (y > 0 && y < 9) {
            if (x == 0) {
                Element e1 = new Element(getY() - 1, getX());
                Element e2 = new Element(getY() - 1, getX() + 1);
                Element e3 = new Element(getY(), getX() + 1);
                Element e4 = new Element(getY() + 1, getX() + 1);
                Element e5 = new Element(getY() + 1, getX());
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
                elements.add(e4);
                elements.add(e5);
            } else if (x == 9) {
                Element e1 = new Element(getY() - 1, getX());
                Element e2 = new Element(getY() - 1, getX() - 1);
                Element e3 = new Element(getY(), getX() - 1);
                Element e4 = new Element(getY() + 1, getX() - 1);
                Element e5 = new Element(getY() + 1, getX());
                elements.add(e1);
                elements.add(e2);
                elements.add(e3);
                elements.add(e4);
                elements.add(e5);
            }
        }
        return elements;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ElementState getState() {
        return state;
    }

    public void setState(ElementState state) {
        this.state = state;
    }
}