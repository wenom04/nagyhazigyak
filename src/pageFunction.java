import javax.swing.*;
import java.awt.*;

public class pageFunction {
    /**
     * A játékot megjelenítő ablakot frissíti
     * @param frame A játékot megjelenítő ablak
     * @param game A játékot megjelenítő panel
     */
    public static void pageRefresher(JFrame frame, TorpedoGame game){
        //https://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java
        frame.getContentPane().removeAll();
        frame.add(game, BorderLayout.CENTER);
        frame.setSize(TorpedoGame.getFrameWidth(), TorpedoGame.getFrameHeight());
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }
}