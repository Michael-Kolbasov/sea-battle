package game.launch;

import game.objects.field.GameMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private GameProcess gameProcess;
    private AppLauncher launcher;
    private Pattern firePattern = Pattern.compile("fire [a-j]{1,1}[0-9]{1,1}");
    private Matcher fireMatcher;
    private String userInput;
    private GameMap playerMap;
    private GameMap aiMap;

    public Input(GameProcess gameProcess, AppLauncher launcher) {
        this.gameProcess = gameProcess;
        playerMap = gameProcess.getPlayerMap();
        aiMap = gameProcess.getAiMap();
        this.launcher = launcher;
    }

    public void process() {
        gameProcess.getPlayerMap().displayMap();
        gameProcess.getAiMap().displayEnemyMap();
        userInput = getUserInput();
        fireMatcher = firePattern.matcher(userInput);
        if (fireMatcher.matches()) {
            fireMatcher.reset();
            startBattle();
        } else if (userInput.equalsIgnoreCase("ships")) {
            int shipsLeft = GameMap.countHowManyShipsLeft(aiMap);
            if (shipsLeft == 1) {
                System.out.println("There is " + GameMap.countHowManyShipsLeft(aiMap) + " ship left.");
            } else {
                System.out.println("There are " + GameMap.countHowManyShipsLeft(aiMap) + " ships left.");
            }
        } else if (userInput.equalsIgnoreCase("start")) {
            playerMap = null;
            aiMap = null;
            launcher = null;
            new AppLauncher().launchRandom();
        } else if (userInput.equalsIgnoreCase("quit")) {
            closeConnection(reader);
            System.exit(0);
        } else {
            System.out.println("Wrong input");
        }
    }

    public static BufferedReader getReader() {
        return reader;
    }

    private void closeConnection(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startBattle() {
        boolean playerFireResult;
        if (gameProcess.fire(userInput)) {
            do {
                userInput = getUserInput();
                playerFireResult = gameProcess.fire(userInput);
            } while (playerFireResult);
        } else {
            boolean aiFireResult;
            if (gameProcess.aiFire()) {
                do {
                    aiFireResult = gameProcess.aiFire();
                } while (aiFireResult);
            }
        }
    }

    private String getUserInput() {
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInput;
    }
}