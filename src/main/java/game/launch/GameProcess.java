package game.launch;
import game.objects.field.GameMap;
import game.players.AbstractPlayer;
import game.players.Computer;
import game.players.Human;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameProcess {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private String userInput;
    private boolean victory;
    private AbstractPlayer player;
    private AbstractPlayer enemy;

    public GameProcess() {
        player = new Human(this);
        enemy = new Computer(this);
    }

    public static BufferedReader getReader() {
        return reader;
    }

    public static void closeConnection() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process() {
        System.out.println("Enter 'Start' to start the game.");
        userInput = getUserInput();
        if (userInput.equalsIgnoreCase("start")) {
            startBattle();
        } else if (userInput.equalsIgnoreCase("quit")) {
            closeConnection();
            System.exit(0);
        } else {
            System.out.println("Wrong input");
            process();
        }
    }

    public GameMap getEnemyMap() {
        return enemy.getMap();
    }

    public GameMap getPlayerMap() {
        return player.getMap();
    }

    private void startBattle() {
        while (!getVictory()) {
            do {
                player.displayMap();
                enemy.displayMap();
                System.out.println("Your time to shoot");
                player.fire();
                if (checkVictory()) {
                    setVictory(true);
                    break;
                }
            } while (player.getResult());
            do {
                System.out.println("Enemy shoots");
                enemy.fire();
                if (checkVictory()) {
                    setVictory(true);
                    break;
                }
            } while (enemy.getResult());
        }
        player.displayMap();
        enemy.displayMap();
    }

    private String getUserInput() {
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInput;
    }

    private boolean checkVictory() {
        return player.getVictory() || enemy.getVictory();
    }

    private boolean getVictory() {
        return victory;
    }

    private void setVictory(boolean victory) {
        this.victory = victory;
    }
}