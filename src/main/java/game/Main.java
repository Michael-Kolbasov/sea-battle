package game;
import game.launch.AppLauncher;

public class Main {

    public static void main(String[] args) {
        new Main().showInfo();
        new AppLauncher().launchRandom();
    }

    private void showInfo() {
        System.out.println("Welcome to the Sea Battle!");
        System.out.println("Commands possible at any time:");
        System.out.println("'Start' - to start new game");
        System.out.println("'Quit' - to quit application");
        System.out.println("Commands possible at the game:");
        System.out.println("'Fire A1' - to shoot the cell");
        System.out.println("'Ships' - to show enemy's ships that are alive");
        System.out.println();
    }
}