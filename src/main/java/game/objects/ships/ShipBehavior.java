package game.objects.ships;

public interface ShipBehavior {
    void markHit(int position);
    boolean checkState();
    void setRandomCoordinates();
    void fillBody();
}