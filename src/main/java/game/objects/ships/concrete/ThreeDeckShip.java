package game.objects.ships.concrete;

import game.objects.ships.Ship;

public class ThreeDeckShip extends Ship {

    public ThreeDeckShip() {
        body = new char[] {'o', 'o', 'o'};
    }
}