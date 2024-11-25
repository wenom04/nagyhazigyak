import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private int gridSize;
    private int gridSizeHorizontal;
    private int gridSizeVertical;
    private Entity player;
    private Entity computer;
    private int numberOfShips;
    private TorpedoGame game;
    int shipCounter = 0;

    public Board(int gridSize, int gridSizeHorizontal, int gridSizeVertical, Entity player, Entity computer, int numberOfShips, TorpedoGame game) {
        this.gridSize = gridSize;
        this.gridSizeHorizontal = gridSizeHorizontal;
        this.gridSizeVertical = gridSizeVertical;
        this.player = player;
        this.computer = computer;
        this.numberOfShips = numberOfShips;
        this.game = game;
        //System.out.println("Number of ships: L" + numberOfShips);
    }

    /**
     * Kirajzolja a négyzetrácsokat
     * @param g a grafikus felület
     * @param isOnRight a négyzetrács jobb oldalon van-e, ez azért kell, mert arrébb kell rajzolni a betűket és számokat
     */
    private void drawGrid(Graphics g, boolean isOnRight){
        int fromGridX = gridSize;
        int toGridX = gridSizeHorizontal*gridSize;
        int stringNumbersX = gridSize/3;
        int stringLettersX = gridSize;

        if (isOnRight){
            fromGridX = (gridSizeHorizontal+2)*gridSize;
            toGridX = (2*gridSizeHorizontal+1)*gridSize;
            stringNumbersX = (gridSizeHorizontal+1)*gridSize+gridSize/3;
            stringLettersX = (gridSizeHorizontal+2)*gridSize;

        }
        for (int x = fromGridX; x <= toGridX; x += gridSize){
            for (int y = gridSize; y <= gridSizeVertical*gridSize; y += gridSize) {
                g.drawRect(x, y, gridSize, gridSize);
            }
        }
        for (int i = 1; i <= gridSizeVertical; i++) {
            g.drawString(String.valueOf(i), stringNumbersX, gridSize + i * gridSize - gridSize/3);
        }

        for (int i = 0; i < gridSizeHorizontal; i++) {
            g.drawString(String.valueOf((char) ('A' + i)), stringLettersX + i * gridSize + gridSize/3, gridSize-gridSize/3);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        setBackground(new Color(52, 61, 235));
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, gridSize/3));

        drawGrid(g, false);
        drawGrid(g, true);


        //int botRandomX;
        //int botRandomY;
        //Random rand = new Random();


        for (Point p : player.clickedPoints) {
            // 12*gridSize azért kell, mert a jobboldali négyzetrácsok innen kezdődnek
            // a p egy 0 és 10 közötti szám, amit meg kell szorozni 30-al, mert a négyzetrácsok 30x30-asak
            // a 15 azért kell, mert a kör középpontját kell meghatározni
            int centerX = (gridSizeHorizontal+2)*gridSize + p.x * gridSize + gridSize/2;
            // a y koordinátát is ugyanúgy kell meghatározni, mint az x-et
            // de itt csak 30-at kell hozzáadni
            int centerY = gridSize + p.y * gridSize + gridSize/2;

//            if (computer.ships[p.x][p.y] && !player.shots[p.x][p.y]) {
////                foundShips++;
////                System.out.println("Found ships: " + foundShips);
//                player.shots[p.x][p.y] = true;
//            }
//            else{
//                player.misses[p.x][p.y] = true;
//            }

            player.shots[p.x][p.y] = true;

            if (computer.ships[p.x][p.y]) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            // A -gridSize / 6 azért kell, mert a kört, a négyzet bal felső sarkából rajzolja
            g.fillOval(centerX - gridSize / 6, centerY - gridSize / 6, gridSize / 3, gridSize / 3);


        }
        //Point lastHit = new Point();
        //boolean wasHit = false;

        if (shipCounter == numberOfShips) {
            if(!game.wasLastRemoved) {
                computer.randomShot(player, gridSizeHorizontal, gridSizeVertical);
                System.out.println("Lőttem");
                System.out.println(numberOfShips);
                System.out.println(shipCounter);
            }
            game.checkAllShips(computer, player);
            game.checkAllShips(player, computer);
        }


        for(int i = 0; i < gridSizeHorizontal; i++) {
            for(int j = 0; j < gridSizeVertical; j++) {
                if(player.ships[i][j]) {
                    g.setColor(Color.GRAY);
                    g.fillRect(i * gridSize + gridSize, j * gridSize + gridSize, gridSize, gridSize);
                    g.setColor(Color.WHITE);
                    g.drawRect(i * gridSize + gridSize, j * gridSize + gridSize, gridSize, gridSize);
                }
                if(computer.ships[i][j]) {
                    g.setColor(Color.GRAY);
                    g.fillRect(i * gridSize + (gridSizeHorizontal+2) * gridSize, j * gridSize + gridSize, gridSize, gridSize);
                    g.setColor(Color.WHITE);
                    g.drawRect(i * gridSize + (gridSizeHorizontal+2) * gridSize, j * gridSize + gridSize, gridSize, gridSize);
                }
            }
        }

        for(Point p: computer.clickedPoints) {
            if (player.ships[p.x][p.y]) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            int centerX = gridSize + p.x * gridSize + gridSize/2;
            int centerY = gridSize + p.y * gridSize + gridSize/2;
            // A -gridSize / 6 azért kell, mert a kört, a négyzet bal felső sarkából rajzolja
            g.fillOval(centerX - gridSize / 6, centerY - gridSize / 6, gridSize / 3, gridSize / 3);
        }
        if(game.areAllShipsSunk(game.getComputer()) && !player.clickedPoints.isEmpty() && !computer.shipsList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nyertél!", "Game over", JOptionPane.OK_OPTION);
            System.exit(0);
        } else if (game.areAllShipsSunk(game.getPlayer()) && !player.clickedPoints.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sajnos vesztettél!", "Game over", JOptionPane.OK_OPTION);
            System.exit(0);
        }
    }

    public void addOneToShipCounter() {
        shipCounter++;
    }
}
