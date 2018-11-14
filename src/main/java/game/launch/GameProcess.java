package game.launch;
import game.objects.field.GameMap;
import game.players.AbstractPlayer;
import game.players.Computer;
import game.players.Player;

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
        player = new Player(this);
        enemy = new Computer(this);
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
                System.out.println();
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

    public GameMap getEnemyMap() {
        return enemy.getMap();
    }

    public GameMap getPlayerMap() {
        return player.getMap();
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
}