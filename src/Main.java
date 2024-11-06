import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Torpedo");
        ImageIcon icon = new ImageIcon("./kepek/torpedoikonrendes.png");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(new LaunchPage(), BorderLayout.CENTER);
    }
}

