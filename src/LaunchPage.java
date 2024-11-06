import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LaunchPage extends JPanel implements ActionListener
{
    Image backgroundImg;

    JButton myButton = new JButton("Új játék");
    JComboBox myComboBox;
    JComboBox myComboBox2;

    JComboBox ship2Num;
    JComboBox ship3Num;
    JComboBox ship4Num;
    JComboBox ship5Num;

    JLabel ship2Label = new JLabel("2 hosszú hajók száma:");
    JLabel ship3Label = new JLabel("3 hosszú hajók száma:");
    JLabel ship4Label = new JLabel("4 hosszú hajók száma:");
    JLabel ship5Label = new JLabel("5 hosszú hajók száma:");

    JLabel horizontalLabel = new JLabel("Vízszintes méret:");
    JLabel verticalLabel = new JLabel("Függőleges méret:");


    public LaunchPage() {

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        backgroundImg = new ImageIcon("./kepek/hatterkep.png").getImage();

        //frame.setContentPane(new JLabel(new ImageIcon(backgroundImg)));
        myButton.setFocusable(false);

        String[] sizes = {"10", "9", "8", "7", "6", "5"};

        String[] bigShipNum = {"1", "2", "3"};
        String[] smallShipNum = {"1", "2", "3", "4", "5"};

        ship2Num = new JComboBox(smallShipNum);
        ship3Num = new JComboBox(smallShipNum);
        ship4Num = new JComboBox(bigShipNum);
        ship5Num = new JComboBox(bigShipNum);

        myComboBox = new JComboBox(sizes);
        myComboBox2 = new JComboBox(sizes);

        //myButton.addActionListener(this);
        //myComboBox.addActionListener(this);
        //myComboBox2.addActionListener(this);

        //JPanel panel = new JPanel(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        add(horizontalLabel, c);
        c.gridx = 1;
        add(myComboBox, c);
        c.gridx = 0;
        c.gridy = 1;
        add(verticalLabel, c);
        c.gridx = 1;
        add(myComboBox2, c);

        c.gridx = 0;
        c.gridy = 2;
        add(ship2Label, c);
        c.gridx = 1;
        add(ship2Num, c);
        c.gridx = 0;
        c.gridy = 3;
        add(ship3Label, c);
        c.gridx = 1;
        add(ship3Num, c);
        c.gridx = 0;
        c.gridy = 4;
        add(ship4Label, c);
        c.gridx = 1;
        add(ship4Num, c);
        c.gridx = 0;
        c.gridy = 5;
        add(ship5Label, c);
        c.gridx = 1;
        add(ship5Num, c);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        add(myButton, c);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s;
        if(e.getSource()==myButton) {

            new TorpedoGame();
        }
        if(e.getSource()==myComboBox) {
            s = myComboBox.getSelectedItem().toString();
            //System.out.println(s);
            TorpedoGame.setGridSizeHorizontal(s);
        }

        if(e.getSource()==myComboBox2) {
            s = myComboBox2.getSelectedItem().toString();
            //System.out.println(s);
            TorpedoGame.setGridSizeVertical(s);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
    }
}

