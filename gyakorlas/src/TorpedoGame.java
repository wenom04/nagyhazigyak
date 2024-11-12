import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TorpedoGame extends JPanel {
    private static int gridSize = 60;
    private static int frameWidth = 23*gridSize;
    private static int frameHeight = 13*gridSize;
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

    public TorpedoGame() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Hova rajzoljuk a köröket
                //a getX() megkapja az egér x koordinátáját, getY() pedig az y koordinátáját
                // kivon az x-ből 12*gridSize-at(hogy csak a jobb oldali négyzetrácson lehessen kattintani)
                // [jelen esetben ez 360, azért csináltam így, hogy meg lehessen változtatni a gridSize-t]
                // elosztja 30-cal, hogy megtudjuk melyik négyzetrácsba kattintottunk
                int gridX = (e.getX() - 12*gridSize) / gridSize;
                // a getY() ugyanaz, mint az x, csak itt 30-at vonunk le, hogy a felső rész ne legyen benne
                int gridY = (e.getY() - gridSize) / gridSize;

                int gridShipX = (e.getX() - gridSize) / gridSize;
                int gridShipY = (e.getY() - gridSize) / gridSize;

                //Ha a kattintás a négyzetrácsokon belül van, akkor hozzáadjuk a kattintás helyét a clickPoints listához
                if (shipCounter >= 10 && gridX >= 0 && gridX < 10 && gridY >= 0 && gridY < 10) {
                    if(!clickPoints.contains(new Point(gridX, gridY))){
                        clickPoints.add(new Point(gridX, gridY));
                        repaint();
                    }

                }

                if (shipCounter < 10 && gridShipX >= 0 && gridShipX < 10 && gridShipY >= 0 && gridShipY < 10) {
                    if (!shipPoints.contains(new Point(gridShipX, gridShipY))) {
                        shipPoints.add(new Point(gridShipX, gridShipY));
                        shipsPlayer[gridShipX][gridShipY] = true;
                        shipCounter++;
                        if (shipCounter == 10) {
                            System.out.println("Ships placed!");
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
        for (int x = gridSize; x <= 10*gridSize; x += gridSize){
            for (int y = gridSize; y <= 10*gridSize; y += gridSize) {
                g.drawRect(x, y, gridSize, gridSize);
            }
        }

        for (int x = 12*gridSize; x <= 21*gridSize; x += gridSize) {
            for (int y = gridSize; y <= 10*gridSize; y += gridSize) {
                g.drawRect(x, y, gridSize, gridSize);
            }
        }

        //A baloldali négyzetrács számozása és betűzése
        for (int i = 1; i <= 10; i++) {
            g.drawString(String.valueOf(i), gridSize/3, gridSize + i * gridSize - gridSize/3);
        }

        for (int i = 0; i < 10; i++) {
            g.drawString(String.valueOf((char) ('A' + i)), gridSize + i * gridSize + gridSize/3, gridSize-gridSize/3);
        }

        //A jobboldali négyzetrács számozása és betűzése
        for (int i = 1; i <= 10; i++) {
            g.drawString(String.valueOf(i), 11*gridSize+gridSize/3, gridSize + i * gridSize - gridSize/3);
        }

        for (int i = 0; i < 10; i++) {
            g.drawString(String.valueOf((char) ('A' + i)), 12*gridSize + i * gridSize + gridSize/3, gridSize-gridSize/3);
        }

        int botRandomX;
        int botRandomY;
        Random rand = new Random();


        for (Point p : clickPoints) {
            // 12*gridSize azért kell, mert a jobboldali négyzetrácsok innen kezdődnek
            // a p egy 0 és 10 közötti szám, amit meg kell szorozni 30-al, mert a négyzetrácsok 30x30-asak
            // a 15 azért kell, mert a kör középpontját kell meghatározni
            int centerX = 12*gridSize + p.x * gridSize + gridSize/2;
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
        if (shipCounter >= 10) {
            botRandomX = rand.nextInt(10);
            botRandomY = rand.nextInt(10);
            Point botShot = new Point(botRandomX, botRandomY);

            if(!botPoints.contains(botShot)) {
                botPoints.add(botShot);
                if(shipsPlayer[botRandomX][botRandomY] && !hitByBot[botRandomX][botRandomY]) {
                    shipsFoundByBot++;
                    System.out.println("Ships found by bot: " + shipsFoundByBot);
                    hitByBot[botRandomX][botRandomY] = true;
                }
            }

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
}