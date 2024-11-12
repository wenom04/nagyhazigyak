import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LaunchPage extends JPanel implements ActionListener {
    boolean visible = true;
    Image backgroundImg;

    JButton myButton = new JButton("Új játék");
    private JButton loadGame = new JButton("Játék visszaállítása");
    JComboBox myComboBox;
    JComboBox myComboBox2;

    JComboBox ship2Num;
    JComboBox ship3Num;
    JComboBox ship4Num;
    JComboBox ship5Num;

    JFrame frame;

    JLabel ship2Label = new JLabel("2 hosszú hajók száma:");
    JLabel ship3Label = new JLabel("3 hosszú hajók száma:");
    JLabel ship4Label = new JLabel("4 hosszú hajók száma:");
    JLabel ship5Label = new JLabel("5 hosszú hajók száma:");
    JLabel filler = new JLabel(" ");

    JLabel horizontalLabel = new JLabel("Vízszintes méret:");
    JLabel verticalLabel = new JLabel("Függőleges méret:");

    private int shipNum = 0;
    private int maxLength;


    public LaunchPage(JFrame frame) {
        this.frame = frame;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        backgroundImg = new ImageIcon("./kepek/hatterkep.png").getImage();

        //frame.setContentPane(new JLabel(new ImageIcon(backgroundImg)));
        myButton.setFocusable(false);

        String[] sizes = {"10", "9", "8", "7", "6", "5"};

        String[] bigShipNum = {"0", "1", "2", "3"};
        String[] smallShipNum = {"0", "1", "2", "3", "4", "5"};

        ship2Num = new JComboBox(smallShipNum);
        ship3Num = new JComboBox(smallShipNum);
        ship4Num = new JComboBox(bigShipNum);
        ship5Num = new JComboBox(bigShipNum);
        
        ship2Label.setForeground(Color.WHITE);
        ship3Label.setForeground(Color.WHITE);
        ship4Label.setForeground(Color.WHITE);
        ship5Label.setForeground(Color.WHITE);
        horizontalLabel.setForeground(Color.WHITE);
        verticalLabel.setForeground(Color.WHITE);

        myComboBox = new JComboBox(sizes);
        myComboBox2 = new JComboBox(sizes);

        myButton.addActionListener(this);
        loadGame.addActionListener(this);
        myComboBox.addActionListener(this);
        myComboBox2.addActionListener(this);
        ship2Num.addActionListener(this);
        ship3Num.addActionListener(this);
        ship4Num.addActionListener(this);
        ship5Num.addActionListener(this);

        c.gridx = 0;
        c.gridy = 0;
        //c.insets = new Insets(5, 5, 5, 5);
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

        c.gridx=0;
        c.gridy = 7;
        c.gridwidth = 2;
        add(myButton, c);

        c.gridy = 8;
        add(filler, c);

        c.gridy = 9;
        add(loadGame, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s;
        if(e.getSource()==myButton) {
            this.pageRefresh(frame);
        }
        if(e.getSource()==myComboBox) {
            s = myComboBox.getSelectedItem().toString();
            TorpedoGame.setGridSizeHorizontal(s);
        }
        if(e.getSource()==myComboBox2) {
            s = myComboBox2.getSelectedItem().toString();
            TorpedoGame.setGridSizeVertical(s);
        }

        if(e.getSource()==ship2Num) {
            s = ship2Num.getSelectedItem().toString();
            if(Integer.parseInt(s) > 0) {
                maxLength = Math.max(maxLength, 2);
            }
            TorpedoGame.setShipLen(Integer.parseInt(s),0);
        }
        if(e.getSource()==ship3Num) {
            s = ship3Num.getSelectedItem().toString();
            if(Integer.parseInt(s) > 0) {
                maxLength = Math.max(maxLength, 3);
            }
            TorpedoGame.setShipLen(Integer.parseInt(s),1);
        }
        if(e.getSource()==ship4Num) {
            s = ship4Num.getSelectedItem().toString();
            if(Integer.parseInt(s) > 0) {
                maxLength = Math.max(maxLength, 4);
            }
            TorpedoGame.setShipLen(Integer.parseInt(s),2);
        }
        if(e.getSource()==ship5Num) {
            s = ship5Num.getSelectedItem().toString();
            if(Integer.parseInt(s) > 0) {
                maxLength = Math.max(maxLength, 5);
            }
            TorpedoGame.setShipLen(Integer.parseInt(s),3);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
    }

    public boolean isVisible() {
        return visible;
    }

    public int getShipNum(){
        return shipNum;
    }

    public int getMaxLength(){
        return maxLength;
    }

    public void pageRefresh(JFrame frame){
        TorpedoGame.setMaxShipLength(this.getMaxLength());
        TorpedoGame game = new TorpedoGame(frame);
        pageFunction.pageRefresher(frame, game);
    }
}

