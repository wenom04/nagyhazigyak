import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Torpedo");
        frame.setVisible(true);
        ImageIcon icon = new ImageIcon("./kepek/torpedoikonrendes.png");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        LaunchPage launchPage = new LaunchPage(frame);
        frame.add(launchPage, BorderLayout.CENTER);
        frame.setSize(600, 600);

    }
}


