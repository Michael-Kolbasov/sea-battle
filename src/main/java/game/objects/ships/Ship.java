package game.objects.ships;

public abstract class Ship implements ShipBehavior {
    protected char[] body;
    private int hitCount = 0;
    private boolean dead = false;

    @Override
    public char markHit(int position) {
        hitCount++;
        return body[position] = 'x';
    }

    @Override
    public boolean checkState() {
        return dead = (hitCount == body.length);
    }
}