package game.objects.ships;

public interface ShipBehavior {
    void markHit(int y, int x);
    boolean checkState();
    void setRandomCoordinates();
    void fillBody();
}