package game.launch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    private boolean finish;
    private GameProcess gameProcess;
    private Pattern firePattern = Pattern.compile("[fire ][a-j][0-9]");
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public Input(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
    }

    public void process() {
        try {
            String userInput = reader.readLine().toLowerCase();
            Matcher fireMatcher = firePattern.matcher(userInput);
            if (fireMatcher.find()) {
                fireMatcher.reset();
                boolean playerFireResult = gameProcess.fire(userInput);
                while (playerFireResult) {
                    userInput = reader.readLine().toLowerCase();
                    if (!(userInput.equalsIgnoreCase("quit"))) {
                        playerFireResult = gameProcess.fire(userInput);
                    } else {
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

            }
            if (!isFinished()) {

            } else {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFinished() {
        return finish;
    }
}