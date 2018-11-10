package game.objects.ships;
import game.objects.ships.concrete.FourDeckShip;
import game.objects.ships.concrete.OneDeckShip;
import game.objects.ships.concrete.ThreeDeckShip;
import game.objects.ships.concrete.TwoDeckShip;

public class ShipFactory {
    public Ship createShip(int size) {
        Ship ship = null;
        switch (size) {
            case 1 :
                ship = new OneDeckShip();
                break;
            case 2 :
                ship = new TwoDeckShip();
                break;
            case 3 :
                ship = new ThreeDeckShip();
                break;
            case 4 :
                ship = new FourDeckShip();
                break;
            default :
                System.err.println("No such ship");
                break;
        }
        return ship;
    }
}