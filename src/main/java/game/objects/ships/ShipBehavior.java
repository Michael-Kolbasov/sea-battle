package game.objects.ships;

public interface ShipBehavior {
    char markHit(int position);
    boolean checkState();
}