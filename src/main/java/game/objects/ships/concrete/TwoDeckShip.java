package game.objects.ships.concrete;

import game.objects.ships.Ship;

public class TwoDeckShip extends Ship {

    public TwoDeckShip() {
        body = new char[] {'o', 'o'};
    }
}