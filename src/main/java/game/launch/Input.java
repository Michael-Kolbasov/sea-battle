package game.launch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
    private boolean finish;

    public boolean isFinished() {
        return finish;
    }

    public void process() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));) {
            String userInput = reader.readLine();
            finish = userInput.equalsIgnoreCase("quit");

            //TODO: coordinates input and executor

            if (!isFinished()) {
                process();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}