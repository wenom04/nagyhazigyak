import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TorpedoGame extends JPanel implements ActionListener {
    //A gombok, amiket a játékban használunk
    private final JButton newGame = new JButton("Új játék");
    private final JButton saveGame = new JButton("Mentés");
    private final JButton backspace = new JButton("Vissza");

    //Entitások a játékhoz
    private final Entity player;
    private final Entity computer;

    //A frame, amiben a játék fut
    private final JFrame frame;
    private final Board board;

    //A négyzetrács mérete, az ablak szélessége és magassága
    private static final int gridSize = 60;
    static int frameWidth = 23*gridSize;
    static int frameHeight = 13*gridSize;
    private static int gridSizeHorizontal = 10;
    private static int gridSizeVertical = 10;

    private static int numberOfShips;
    private static int maxLength;

    private ArrayList<Integer> shipLengths = new ArrayList<>();
    //A 0. eleme a 2 hosszú hajók kellő száma, a 1. eleme a 3 hosszú hajók száma, stb.
    private static int[] shipNums = new int[4];

    //A hajók elhelyezéséhez szükséges pontok
    private Point firstPoint = null;
    private Point lastPoint = null;


    private void drawShip(Point first, Point last) {
        player.ships[firstPoint.x][firstPoint.y] = false;
        if (first.x == last.x) {
            // Vertical ship
            for (int y = Math.min(first.y, last.y); y <= Math.max(first.y, last.y); y++) {
                player.ships[first.x][y] = true;
                System.out.println(first.x + " " + y + " " + player.ships[first.x][y]);
            }
            shipLengths.add(Math.abs(first.y - last.y)+1);
        } else if (first.y == last.y) {
            // Horizontal ship
            for (int x = Math.min(first.x, last.x); x <= Math.max(first.x, last.x); x++) {
                System.out.println(x + " " + first.y);
                player.ships[x][first.y] = true;
            }
            shipLengths.add(Math.abs(first.x - last.x)+1);
        }
    }

    private boolean isOverlapping(Point first, Point last) {
        player.ships[firstPoint.x][firstPoint.y] = false;
        if (first.x == last.x) {
            // Vertical ship
            for (int y = Math.min(first.y, last.y); y <= Math.max(first.y, last.y); y++) {
                System.out.println(first.x + " " + y);
                if(player.ships[first.x][y]){
                    return true;
                }
            }
        } else if (first.y == last.y) {
            // Horizontal ship
            for (int x = Math.min(first.x, last.x); x <= Math.max(first.x, last.x); x++) {
                System.out.println(x + " " + first.y);
                if(player.ships[x][first.y]){
                    return true;
                }
            }
        }
        return false;
    }

    public TorpedoGame(JFrame frame) {
        this.frame = frame;

        //Az entitások létrehozása
        player = new Player(gridSizeHorizontal, gridSizeVertical);
        computer = new Computer(gridSizeHorizontal, gridSizeVertical);

        //buttonPanel létrehozása a gomboknak
        JPanel buttonPanel = new JPanel(new FlowLayout());
        setLayout(new BorderLayout());
        numberOfShips = 0;
        for(int i = 0; i < 4; i++){
            numberOfShips += shipNums[i];
        }
        System.out.println("Number of ships: H " + numberOfShips);
        board = new Board(gridSize, gridSizeHorizontal, gridSizeVertical, player, computer, numberOfShips);
        add(board, BorderLayout.CENTER);

        //ActionListnerek hozzáadása a gombokhoz
        newGame.addActionListener(this);
        saveGame.addActionListener(this);
        backspace.addActionListener(this);

        //A gombok hozzáadása a panelhez
        buttonPanel.add(newGame);
        buttonPanel.add(saveGame);
        buttonPanel.add(backspace);
        add(buttonPanel, BorderLayout.SOUTH);

        /*
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
        */
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
                if (shipLengths.size() >= numberOfShips && gridX >= 0 && gridX < gridSizeHorizontal && gridY >= 0 && gridY < gridSizeVertical) {
                    if(!player.clickedPoints.contains(new Point(gridX, gridY))){
                        player.clickedPoints.add(new Point(gridX, gridY));
                        board.repaint();
                    }
                }

                if (shipLengths.size() < numberOfShips && gridShipX >= 0 && gridShipX < gridSizeHorizontal && gridShipY >= 0 && gridShipY < gridSizeVertical) {
                    if (firstPoint==null) {
                        if(!player.ships[gridShipX][gridShipY]) {
                            firstPoint = new Point(gridShipX, gridShipY);
                            player.ships[firstPoint.x][firstPoint.y] = true;
                            repaint();
                        }
                    } else {
                        lastPoint = new Point(gridShipX, gridShipY);
                        //Azt vizsgálom, hogy két hajó nincs-e egymáson, a hajó hossza 1 és maxLength között van-e
                        //és hogy a hajók száma nem haladja-e meg a megadott értéket
                        if (!isOverlapping(firstPoint, lastPoint) && ((firstPoint.x == lastPoint.x && TorpedoGame.between(Math.abs(firstPoint.y - lastPoint.y),1, maxLength) && getShipNumByLen(Math.abs(firstPoint.y - lastPoint.y) + 1) < shipNums[Math.abs(firstPoint.y - lastPoint.y)-1]) ||
                                (firstPoint.y == lastPoint.y && TorpedoGame.between(Math.abs(firstPoint.x - lastPoint.x),1, maxLength) && getShipNumByLen(Math.abs(firstPoint.x - lastPoint.x) + 1) < shipNums[Math.abs(firstPoint.x - lastPoint.x)-1]))) {
                            drawShip(firstPoint, lastPoint);
                            board.addOneToShipCounter();
                            if (shipLengths.size() == numberOfShips) {
                                System.out.println("Minden hajó el lett helyezve.");
                            }
                        }
                        else{
                            System.out.println("Igy nem lehet hajot elhelyezni te kis buzi!");
                            player.ships[firstPoint.x][firstPoint.y] = false;
                        }
                        firstPoint = null;
                        lastPoint = null;
                    }
                    repaint();
                    }
                }

        });
    }

//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//
//        setBackground(new Color(52, 61, 235));
//        g.setColor(Color.WHITE);
//        g.setFont(new Font("Arial", Font.BOLD, gridSize/3));
//
//        drawGrid(g, false);
//        drawGrid(g, true);
//
//
//        int botRandomX;
//        int botRandomY;
//        Random rand = new Random();
//
//
//        for (Point p : player.clickedPoints) {
//            // 12*gridSize azért kell, mert a jobboldali négyzetrácsok innen kezdődnek
//            // a p egy 0 és 10 közötti szám, amit meg kell szorozni 30-al, mert a négyzetrácsok 30x30-asak
//            // a 15 azért kell, mert a kör középpontját kell meghatározni
//            int centerX = (gridSizeHorizontal+2)*gridSize + p.x * gridSize + gridSize/2;
//            // a y koordinátát is ugyanúgy kell meghatározni, mint az x-et
//            // de itt csak 30-at kell hozzáadni
//            int centerY = gridSize + p.y * gridSize + gridSize/2;
//
//            if (computer.ships[p.x][p.y] && !player.shots[p.x][p.y]) {
//                foundShips++;
//                System.out.println("Found ships: " + foundShips);
//                player.shots[p.x][p.y] = true;
//            }
//
//            if (computer.ships[p.x][p.y]) {
//                g.setColor(Color.RED);
//            } else {
//                g.setColor(Color.WHITE);
//            }
//            // A -gridSize / 6 azért kell, mert a kört, a négyzet bal felső sarkából rajzolja
//            g.fillOval(centerX - gridSize / 6, centerY - gridSize / 6, gridSize / 3, gridSize / 3);
//
//
//        }
//        if (shipCounter >= numberOfShips) {
//            Point botShot;
//            do {
//                botRandomX = rand.nextInt(gridSizeHorizontal);
//                botRandomY = rand.nextInt(gridSizeVertical);
//                botShot = new Point(botRandomX, botRandomY);
//            } while (computer.clickedPoints.contains(botShot) && !lastWasRemoved);
//            computer.clickedPoints.add(botShot);
//
//            System.out.println(botRandomX + "BotX");
//            System.out.println(botRandomY + "BotY");
//            System.out.println("/n");
//
//            if (player.ships[botRandomX][botRandomY] && !computer.shots[botRandomX][botRandomY]) {
//                shipsFoundByBot++;
//                System.out.println("Ships found by bot: " + shipsFoundByBot);
//                computer.shots[botRandomX][botRandomY] = true;
//            }
//
//            lastWasRemoved = false;
//        }
//
//
//        for(int i = 0; i < 10; i++) {
//            for(int j = 0; j < 10; j++) {
//                if(player.ships[i][j]) {
//                    g.setColor(Color.GRAY);
//                    g.fillRect(i * gridSize + gridSize, j * gridSize + gridSize, gridSize, gridSize);
//                }
//            }
//        }
//
//        for(Point p: computer.clickedPoints) {
//            if (player.ships[p.x][p.y]) {
//                g.setColor(Color.RED);
//            } else {
//                g.setColor(Color.WHITE);
//            }
//            int centerX = gridSize + p.x * gridSize + gridSize/2;
//            int centerY = gridSize + p.y * gridSize + gridSize/2;
//            // A -gridSize / 6 azért kell, mert a kört, a négyzet bal felső sarkából rajzolja
//            g.fillOval(centerX - gridSize / 6, centerY - gridSize / 6, gridSize / 3, gridSize / 3);
//        }
//        if(foundShips == 10) {
//            g.setColor(Color.GREEN);
//            g.drawString("You won!", 12*gridSize, 11*gridSize);
//        } else if (shipsFoundByBot == 10) {
//            g.setColor(Color.RED);
//            g.drawString("You lost!", 12*gridSize, 11*gridSize);
//
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==newGame) {
            System.out.println(shipLengths);
            TorpedoGame game = new TorpedoGame(frame);
            pageFunction.pageRefresher(frame, game);
        }
        if(e.getSource()==saveGame) {
            //saveGame();
        }
        if(e.getSource()==backspace) {
            if(!player.clickedPoints.isEmpty()) {
                Point last = player.clickedPoints.getLast();
                player.clickedPoints.remove(last);
                repaint();
            }
        }
    }

    public static void setGridSizeHorizontal(String gridSizeHorizontal) {
        TorpedoGame.gridSizeHorizontal = Integer.parseInt(gridSizeHorizontal);
    }

    public static void setGridSizeVertical(String gridSizeVertical) {
        TorpedoGame.gridSizeVertical = Integer.parseInt(gridSizeVertical);
    }

    public static void setMaxShipLength(int maxShipLength) {
        maxLength = Math.max(maxShipLength, maxLength);
    }

    public int getShipNumByLen(int len){
        System.out.println(len);
        int count = 0;
        for (int i : shipLengths){
            if (i == len){
                count++;
            }
        }
        return count;
    }

    public static void setShipLen(int num, int idx){
        shipNums[idx] = num;
    }

    public static boolean between(int value, int min, int max) {
        return value > min && value < max;
    }
//    private void drawGrid(Graphics g, boolean isOnRight){
//        int fromGridX = gridSize;
//        int toGridX = gridSizeHorizontal*gridSize;
//        int stringNumbersX = gridSize/3;
//        int stringLettersX = gridSize;
//
//        if (isOnRight){
//            fromGridX = (gridSizeHorizontal+2)*gridSize;
//            toGridX = (2*gridSizeHorizontal+1)*gridSize;
//            stringNumbersX = (gridSizeHorizontal+1)*gridSize+gridSize/3;
//            stringLettersX = (gridSizeHorizontal+2)*gridSize;
//
//        }
//        for (int x = fromGridX; x <= toGridX; x += gridSize){
//            for (int y = gridSize; y <= gridSizeVertical*gridSize; y += gridSize) {
//                g.drawRect(x, y, gridSize, gridSize);
//            }
//        }
//        for (int i = 1; i <= gridSizeVertical; i++) {
//            g.drawString(String.valueOf(i), stringNumbersX, gridSize + i * gridSize - gridSize/3);
//        }
//
//        for (int i = 0; i < gridSizeHorizontal; i++) {
//            g.drawString(String.valueOf((char) ('A' + i)), stringLettersX + i * gridSize + gridSize/3, gridSize-gridSize/3);
//        }
//
}