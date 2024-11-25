import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class TorpedoGame extends JPanel implements ActionListener, MouseListener {
    //A gomb, ami a visszavonást végzi
    private JButton backspace = new JButton("Vissza");
    //A visszavonás után, hogy ne történjen meg újra a visszavonás/ne jelölhessen egyből a gép
    boolean wasLastRemoved = false;
    //Ne vonhasson vissza a játékos egymás után többször, csak egyszer
    private int clickCount = 0;

    //Menüpontok
    JMenuItem newGame = new JMenuItem("Új játék");
    JMenuItem saveGame = new JMenuItem("Mentés");
    JMenuItem loadGame = new JMenuItem("Betöltés");

    //Menü
    JMenu menu = new JMenu("Menü");

    //Entitások a játékhoz
    private Player player;
    private Computer computer;

    //A frame, amiben a játék fut
    private JFrame frame;

    //A játéktábla
    private Board board;

    //A négyzetrács mérete, az ablak szélessége és magassága
    private static int gridSize = 60;
    private static int frameWidth = 23*gridSize;
    private static int frameHeight = 13*gridSize;
    private static int gridSizeHorizontal = 10;
    private static int gridSizeVertical = 10;

    //A hajók száma és maximális hossza
    private int numberOfShips;
    private int maxLength;
    private int alreadyPlacedShipsNum = 0;

    //A hajók hosszának listája
    private List<Integer> shipLengths = new ArrayList<>();

    //A 0. eleme a 2 hosszú hajók kellő száma, a 1. eleme a 3 hosszú hajók száma, stb.
    private static int[] shipNums = new int[4];

    //A hajók elhelyezéséhez szükséges pontok
    private Point firstPoint = null;
    private Point lastPoint = null;

    /**
     * A hajók elhelyezésének metódusa, a két pont között lévő mezőket igazzá teszi a player.ships[][] tömbben
     * @param first az első kattintás helye
     * @param last az utolsó kattintás helye
     */
    public void drawShip(Point first, Point last) {
        ArrayList<Point> shipCoordinates = new ArrayList<>();
        //player.ships[firstPoint.x][firstPoint.y] = false;
        if (first.x == last.x) {
            // Vertical ship
            for (int y = Math.min(first.y, last.y); y <= Math.max(first.y, last.y); y++) {
                player.ships[first.x][y] = true;
                //System.out.println(first.x + " " + y + " " + player.ships[first.x][y]);
                shipCoordinates.add(new Point(first.x, y));
            }
        } else if (first.y == last.y) {
            // Horizontal ship
            for (int x = Math.min(first.x, last.x); x <= Math.max(first.x, last.x); x++) {
                //System.out.println(x + " " + first.y);
                player.ships[x][first.y] = true;
                shipCoordinates.add(new Point(x, first.y));
            }
        }
        player.shipsList.add(new Ship(shipCoordinates));
        shipLengths.add(shipCoordinates.size());
        board.addOneToShipCounter();
        alreadyPlacedShipsNum++;
    }

    /**
     * A hajó elsüllyedéséhez a hajó minden pontjának igaznak kell lennie a shots[][] tömbben
     * @param ship a hajó, amit vizsgálunk
     * @param e az entitás, amit vizsgálunk
     * @return igaz, ha a hajó elsüllyedt
     */
    public boolean isShipSunk(Ship ship, Entity e) {
        for (Point p : ship.getCoordinates()) {
            if (!e.shots[p.x][p.y]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Az összes hajó elsüllyedését vizsgálja
     * @param e az entitás, amely hajóit vizsgálunk
     * @param e2 a másik entitás, aminek a lövéseit vizsgáljuk
     */
    public void checkAllShips(Entity e, Entity e2) {
        for (Ship ship : e.shipsList) {
            if (!ship.isSunk() && isShipSunk(ship, e2)) {
                numberOfShips--;
                ship.setSunk();
            }
        }
    }

    /**
     * Az összes hajó elsüllyedését vizsgálja
     * @param e az entitás, amely hajóit vizsgáljuk
     * @return igaz, ha az összes hajó elsüllyedt
     */
    public boolean areAllShipsSunk(Entity e) {
        for (Ship ship : e.shipsList) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Azt vizsgálja, hogy nem-e fedik egymást a hajók
     * @param first az első kattintás helye, a hajó első cellája
     * @param last a másik kattintás helye, a hajó utolsó cellája
     * @return igaz, ha a hajók fedik egymást
     */
    public boolean isOverlapping(Point first, Point last) {
        player.ships[first.x][first.y] = false;
        if (first.x == last.x) {
            // Vertical ship
            for (int y = Math.min(first.y, last.y); y <= Math.max(first.y, last.y); y++) {
                //System.out.println(first.x + " " + y);
                if(player.ships[first.x][y]){
                    return true;
                }
            }
        } else if (first.y == last.y) {
            // Horizontal ship
            for (int x = Math.min(first.x, last.x); x <= Math.max(first.x, last.x); x++) {
                //System.out.println(x + " " + first.y);
                if(player.ships[x][first.y]){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A játék konstruktora, a játék elindításához szükséges inicializálásokat végzi, visszaállított játék esetén kell használni
     * @param frame a frame, amiben a játék fut
     * @param player a játékos
     * @param computer a gép
     */
    public TorpedoGame(JFrame frame, Player player, Computer computer) {
        this.frame = frame;
        this.player = player;
        this.computer = computer;
        newGame();
    }

    /**
     * A játék konstruktora, a játék elindításához szükséges inicializálásokat végzi, új játék kezdésekor kell használni
     * @param frame a frame, amiben a játék fut
     */
    public TorpedoGame(JFrame frame){
        this.frame = frame;
        player = new Player(gridSizeHorizontal, gridSizeVertical);
        computer = new Computer(gridSizeHorizontal, gridSizeVertical);

        computer.placeShips(shipNums, gridSizeHorizontal, gridSizeVertical);
        newGame();
    }

    /**
     * A játék elindításához szükséges inicializálásokat végzi
     */
    private void newGame() {
        System.out.println("A játék elindult.");

        //buttonPanel létrehozása a gomboknak
        JPanel buttonPanel = new JPanel(new FlowLayout());
        setLayout(new BorderLayout());
        numberOfShips = 0;
        for(int i = 0; i < 4; i++){
            numberOfShips += shipNums[i];
        }
        board = new Board(gridSize, gridSizeHorizontal, gridSizeVertical, player, computer, numberOfShips, this);
        add(board, BorderLayout.CENTER);

        //ActionListnerek hozzáadása a gombokhoz
        backspace.addActionListener(this);

        //Menü hozzáadása
        newGame.setActionCommand("newGame");
        newGame.addActionListener(this);
        saveGame.setActionCommand("saveGame");
        saveGame.addActionListener(this);
        loadGame.setActionCommand("loadGame");
        loadGame.addActionListener(this);


        menu.add(newGame);
        menu.add(saveGame);
        menu.add(loadGame);

        JMenuBar bar = new JMenuBar();
        bar.add(menu);

        frame.setJMenuBar(bar);
        //A gombok hozzáadása a panelhez
        buttonPanel.add(backspace);
        add(buttonPanel, BorderLayout.SOUTH);

        addMouseListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==newGame) {
            int temp = maxLength;
            TorpedoGame game = new TorpedoGame(frame);
            game.setMaxShipLength(temp);
            pageFunction.pageRefresher(frame, game);
        }
        if(e.getSource()==saveGame) {
            FileHandler gamestate = new FileHandler(player, computer, gridSizeHorizontal, gridSizeVertical, maxLength, shipNums, shipLengths, alreadyPlacedShipsNum);
            FileHandler.saveGame(gamestate);
            System.out.println("Játék mentve.");
            System.exit(0);
        }
        if(e.getSource()==backspace) {
            if(!player.clickedPoints.isEmpty() && !wasLastRemoved){
                Point last = player.clickedPoints.getLast();
                player.clickedPoints.remove(last);
                repaint();
                wasLastRemoved = true;
            }
        }
    }

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
                if(wasLastRemoved){
                    clickCount++;
                }
                if(wasLastRemoved && clickCount == 2){
                    clickCount = 0;
                    wasLastRemoved = false;
                }
                checkAllShips(player, computer);
                checkAllShips(computer, player);
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
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void setGridSizeHorizontal(String gridSizeHorizontal) {
        TorpedoGame.gridSizeHorizontal = Integer.parseInt(gridSizeHorizontal);
    }

    public static void setGridSizeVertical(String gridSizeVertical) {
        TorpedoGame.gridSizeVertical = Integer.parseInt(gridSizeVertical);
    }

    public void setMaxShipLength(int maxShipLength) {
        maxLength = Math.max(maxShipLength, maxLength);
    }

    public int getShipNumByLen(int len){
        //System.out.println(len);
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
        System.out.println("Most beállította a " + (idx+2) + " hosszú hajók számát " + num + "-ra");
    }

    public static boolean between(int value, int min, int max) {
        return value >= min && value < max;
    }

    public static int getFrameWidth(){
        return frameWidth;
    }

    public static int getFrameHeight(){
        return frameHeight;
    }

    public Entity getComputer() {
        return computer;
    }

    public static int getGridSizeHorizontal() {
        return gridSizeHorizontal;
    }

    public static int getGridSizeVertical() {
        return gridSizeVertical;
    }

    public Entity getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public void setShipLengths(List<Integer> shipLengths) {
        this.shipLengths = shipLengths;
    }

    public void setAlreadyPlacedShipsNum(int alreadyPlacedShipsNum) {
        this.alreadyPlacedShipsNum = alreadyPlacedShipsNum;
    }
}