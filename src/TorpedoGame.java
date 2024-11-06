import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TorpedoGame extends JPanel implements ActionListener {
    private JButton newGame = new JButton("Új játék");
    private JButton saveGame = new JButton("Mentés");
    private JButton loadGame = new JButton("Játék visszaállítása");
    private JButton backspace = new JButton("Vissza");

    private JFrame frame;

    private static int gridSize = 60;
    private static int frameWidth = 23*gridSize;
    private static int frameHeight = 13*gridSize;
    private static int gridSizeHorizontal = 10;
    private static int gridSizeVertical = 10;

    private ArrayList<Point> clickPoints = new ArrayList<>();
    private ArrayList<Point> shipPoints = new ArrayList<>();
    private ArrayList<Point> botPoints = new ArrayList<>();

    private boolean[][] hits = new boolean[10][10];
    private boolean[][] ships = new boolean[10][10];
    private boolean[][] shipsPlayer = new boolean[10][10];
    private boolean[][] hitByBot = new boolean[10][10];

    private int shipCounter = 0;
    private int foundShips = 0;
    private int shipsFoundByBot = 0;

    private Point firstPoint = null;
    private Point lastPoint = null;

    private boolean lastWasRemoved = false;

    private void drawShip(Point first, Point last) {
        if (first.x == last.x) {
            // Vertical ship
            for (int y = Math.min(first.y, last.y); y <= Math.max(first.y, last.y); y++) {
                shipPoints.add(new Point(first.x, y));
                    shipsPlayer[first.x][y] = true;
            }
        } else if (first.y == last.y) {
            // Horizontal ship
            for (int x = Math.min(first.x, last.x); x <= Math.max(first.x, last.x); x++) {
                shipPoints.add(new Point(x, first.y));
                    shipsPlayer[x][first.y] = true;

            }
        }
        shipCounter++;
    }
    /*
    public static void main(String[] args) {
        JFrame frame = new JFrame("Torpedo");
        TorpedoGame game = new TorpedoGame();

        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            if (game.ships[x][y]) {
                i--;
            } else {
                game.ships[x][y] = true;
            }
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(true);
        frame.add(game);
        frame.setVisible(true);
    }
*/
    public TorpedoGame() {
        frame = new JFrame("Torpedo");
        TorpedoGame game = this;
        ImageIcon icon = new ImageIcon("./kepek/torpedoikonrendes.png");
        frame.setIconImage(icon.getImage());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        frame.setLayout(new BorderLayout());

        newGame.addActionListener(this);
        saveGame.addActionListener(this);
        loadGame.addActionListener(this);
        backspace.addActionListener(this);

        buttonPanel.add(newGame);
        buttonPanel.add(saveGame);
        buttonPanel.add(loadGame);
        buttonPanel.add(backspace);
        frame.add(game, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            if (game.ships[x][y]) {
                i--;
            } else {
                game.ships[x][y] = true;
            }
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(true);
        frame.add(game);
        frame.setVisible(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Hova rajzoljuk a köröket
                //a getX() megkapja az egér x koordinátáját, getY() pedig az y koordinátáját
                // kivon az x-ből 12*gridSize-at(hogy csak a jobb oldali négyzetrácson lehessen kattintani)
                // [jelen esetben ez 360, azért csináltam így, hogy meg lehessen változtatni a gridSize-t]
                // elosztja 30-cal, hogy megtudjuk melyik négyzetrácsba kattintottunk
                int gridX = (e.getX() - (gridSizeHorizontal+2)*gridSize) / gridSize;
                //System.out.println(gridX);
                // a getY() ugyanaz, mint az x, csak itt 30-at vonunk le, hogy a felső rész ne legyen benne
                int gridY = (e.getY() - gridSize) / gridSize;
                //System.out.println(gridY);
                int gridShipX = (e.getX() - gridSize) / gridSize;
                //System.out.println(gridShipX);
                int gridShipY = (e.getY() - gridSize) / gridSize;
                //System.out.println(gridShipY);

                //Ha a kattintás a négyzetrácsokon belül van, akkor hozzáadjuk a kattintás helyét a clickPoints listához
                if (shipCounter >= 3 && gridX >= 0 && gridX < gridSizeHorizontal && gridY >= 0 && gridY < gridSizeVertical) {
                    if(!clickPoints.contains(new Point(gridX, gridY))){
                        clickPoints.add(new Point(gridX, gridY));
                        repaint();
                    }
                }

                if (shipCounter < 3 && gridShipX >= 0 && gridShipX < gridSizeHorizontal && gridShipY >= 0 && gridShipY < gridSizeVertical) {
                    if (!shipPoints.contains(new Point(gridShipX, gridShipY))) {
                        shipPoints.add(new Point(gridShipX, gridShipY));
                        shipsPlayer[gridShipX][gridShipY] = true;
                        if (firstPoint==null) {
                            firstPoint = new Point(gridShipX, gridShipY);
                        } else {
                            lastPoint = new Point(gridShipX, gridShipY);
                            if((firstPoint.x == lastPoint.x && Math.abs(firstPoint.y - lastPoint.y) < 5) ||
                                    (firstPoint.y == lastPoint.y && Math.abs(firstPoint.x - lastPoint.x) < 5))
                            {
                                drawShip(firstPoint, lastPoint);
                            }
                            else{
                                System.out.println("Igy nem lehet hajot elhelyezni te kis buzi!");
                                shipsPlayer[firstPoint.x][firstPoint.y] = false;
                                shipsPlayer[lastPoint.x][lastPoint.y] = false;
                                shipPoints.remove(firstPoint);
                                shipPoints.remove(lastPoint);
                            }
                            firstPoint = null;
                            lastPoint = null;
                        }
                        repaint();
                    }
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        setBackground(new Color(52, 61, 235));
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, gridSize/3));

        //A négyzetrácsok kirajzolása
        for (int x = gridSize; x <= gridSizeHorizontal*gridSize; x += gridSize){
            for (int y = gridSize; y <= gridSizeVertical*gridSize; y += gridSize) {
                g.drawRect(x, y, gridSize, gridSize);
            }
        }

        for (int x = (gridSizeHorizontal+2)*gridSize; x <= (2*gridSizeHorizontal+1)*gridSize; x += gridSize) {
            for (int y = gridSize; y <= gridSizeVertical*gridSize; y += gridSize) {
                g.drawRect(x, y, gridSize, gridSize);
            }
        }

        //A baloldali négyzetrács számozása és betűzése
        for (int i = 1; i <= gridSizeVertical; i++) {
            g.drawString(String.valueOf(i), gridSize/3, gridSize + i * gridSize - gridSize/3);
        }

        for (int i = 0; i < gridSizeHorizontal; i++) {
            g.drawString(String.valueOf((char) ('A' + i)), gridSize + i * gridSize + gridSize/3, gridSize-gridSize/3);
        }

        //A jobboldali négyzetrács számozása és betűzése
        for (int i = 1; i <= gridSizeVertical; i++) {
            g.drawString(String.valueOf(i), (gridSizeHorizontal+1)*gridSize+gridSize/3, gridSize + i * gridSize - gridSize/3);
        }

        for (int i = 0; i < gridSizeHorizontal; i++) {
            g.drawString(String.valueOf((char) ('A' + i)), (gridSizeHorizontal+2)*gridSize + i * gridSize + gridSize/3, gridSize-gridSize/3);
        }

        int botRandomX;
        int botRandomY;
        Random rand = new Random();


        for (Point p : clickPoints) {
            // 12*gridSize azért kell, mert a jobboldali négyzetrácsok innen kezdődnek
            // a p egy 0 és 10 közötti szám, amit meg kell szorozni 30-al, mert a négyzetrácsok 30x30-asak
            // a 15 azért kell, mert a kör középpontját kell meghatározni
            int centerX = (gridSizeHorizontal+2)*gridSize + p.x * gridSize + gridSize/2;
            // a y koordinátát is ugyanúgy kell meghatározni, mint az x-et
            // de itt csak 30-at kell hozzáadni
            int centerY = gridSize + p.y * gridSize + gridSize/2;

            if (ships[p.x][p.y] && !hits[p.x][p.y]) {
                foundShips++;
                System.out.println("Found ships: " + foundShips);
                hits[p.x][p.y] = true;
            }

            if (ships[p.x][p.y]) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            // A -gridSize / 6 azért kell, mert a kört, a négyzet bal felső sarkából rajzolja
            g.fillOval(centerX - gridSize / 6, centerY - gridSize / 6, gridSize / 3, gridSize / 3);


        }
        if (shipCounter >= 3 && !lastWasRemoved) {
            botRandomX = rand.nextInt(gridSizeHorizontal);
            botRandomY = rand.nextInt(gridSizeVertical);
            Point botShot = new Point(botRandomX, botRandomY);
            System.out.println(botRandomX + "BotX");
            System.out.println(botRandomY + "BotY");
            System.out.println("/n");


            if(!botPoints.contains(botShot)) {
                botPoints.add(botShot);
                if(shipsPlayer[botRandomX][botRandomY] && !hitByBot[botRandomX][botRandomY]) {
                    shipsFoundByBot++;
                    System.out.println("Ships found by bot: " + shipsFoundByBot);
                    hitByBot[botRandomX][botRandomY] = true;
                }
            }
            lastWasRemoved = false;
        }


        for(Point p : shipPoints) {
            int rectX = p.x * gridSize + gridSize;
            int rectY = p.y * gridSize + gridSize;

            g.setColor(Color.GRAY);
            g.fillRect(rectX, rectY, gridSize, gridSize);
        }
        for(Point p: botPoints) {
            if (shipsPlayer[p.x][p.y]) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            int centerX = gridSize + p.x * gridSize + gridSize/2;
            int centerY = gridSize + p.y * gridSize + gridSize/2;
            // A -gridSize / 6 azért kell, mert a kört, a négyzet bal felső sarkából rajzolja
            g.fillOval(centerX - gridSize / 6, centerY - gridSize / 6, gridSize / 3, gridSize / 3);
        }
        if(foundShips == 10) {
            g.setColor(Color.GREEN);
            g.drawString("You won!", 12*gridSize, 11*gridSize);
        } else if (shipsFoundByBot == 10) {
            g.setColor(Color.RED);
            g.drawString("You lost!", 12*gridSize, 11*gridSize);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==newGame) {
            frame.dispose();
            new TorpedoGame();
        }
        if(e.getSource()==saveGame) {
            //saveGame();
        }
        if(e.getSource()==loadGame) {
            //loadGame();
        }
        if(e.getSource()==backspace) {
            Point last = clickPoints.getLast();
            clickPoints.remove(last);
            lastWasRemoved = true;
            repaint();
        }
    }

    public static void setGridSizeHorizontal(String gridSizeHorizontal) {
        int gridSizeH = Integer.parseInt((String) gridSizeHorizontal);
        TorpedoGame.gridSizeHorizontal = gridSizeH;
    }

    public static void setGridSizeVertical(String gridSizeVertical) {
        int gridSizeV = Integer.parseInt((String) gridSizeVertical);
        TorpedoGame.gridSizeVertical = gridSizeV;
    }


}