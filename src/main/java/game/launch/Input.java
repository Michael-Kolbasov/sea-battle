package game.launch;

import game.objects.field.GameMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    /*private boolean finish;*/
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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

    /*public void process() {
        try {
            gameProcess.getPlayerMap().displayMap();
            gameProcess.getAiMap().displayEnemyMap();
            String userInput = reader.readLine().toLowerCase();
            Matcher fireMatcher = firePattern.matcher(userInput);
            if (fireMatcher.find()) {
                fireMatcher.reset();
                boolean playerFireResult = gameProcess.fire(userInput);
                while (playerFireResult) {
                    userInput = reader.readLine().toLowerCase();
                    if (!(userInput.equalsIgnoreCase("quit"))) {
                        playerFireResult = gameProcess.fire(userInput);
                    } else if (userInput.equalsIgnoreCase("quit")) {
                        finish = true;
                        return;
                    }
                }
                boolean aiFireResult = gameProcess.aiFire();
                while (aiFireResult) {
                    aiFireResult = gameProcess.aiFire();
                }
            } else if (userInput.equalsIgnoreCase("quit")) {
                reader.close();
                finish = true;
            } else if (userInput.equalsIgnoreCase("ships")) {
                System.out.println("Enemy still has: " + GameMap.countHowManyShipsLeft(gameProcess.getAiMap()) + " ships");
                gameProcess.waitOneSecond();
            }
            if (!isFinished()) {

            } else {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void process() {
        gameProcess.getPlayerMap().displayMap();
        gameProcess.getAiMap().displayEnemyMap();

        userInput = getUserInput();
        fireMatcher = firePattern.matcher(userInput);

        if (fireMatcher.matches()) {
            fireMatcher.reset();
            startBattle();

        } else if (userInput.equalsIgnoreCase("ships")) {
            System.out.println("There are" + GameMap.countHowManyShipsLeft(aiMap) + " ships left.");
        } else if (userInput.equalsIgnoreCase("start")) {
            playerMap = null;
            aiMap = null;
            launcher = null;
            new AppLauncher().launchRandom();
        } else if (userInput.equalsIgnoreCase("quit")) {
            System.exit(0);
        } else {
            System.out.println("Wrong input");
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

    /*public boolean isFinished() {
        return finish;
    }*/
}