package game.field;

public class Field {
    private static Field ourInstance = new Field();

    public static Field getInstance() {
        return ourInstance;
    }

    private Field() {
    }
}
