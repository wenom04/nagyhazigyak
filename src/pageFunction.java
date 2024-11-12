import javax.swing.*;
import java.awt.*;

public class pageFunction {
    public static void pageRefresher(JFrame frame, TorpedoGame game){
        //https://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java
        frame.getContentPane().removeAll();
        frame.add(game, BorderLayout.CENTER);
        frame.setSize(game.frameWidth, game.frameHeight);
        frame.revalidate();
        frame.repaint();
    }
}
