import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Board extends JPanel {
    private int gridSize;
    private int gridSizeHorizontal;
    private int gridSizeVertical;
    private Entity player;
    private Entity computer;
    private int numberOfShips;
    int foundShips = 0;
    int shipsFoundByBot = 0;
    int shipCounter = 0;

    public Board(int gridSize, int gridSizeHorizontal, int gridSizeVertical, Entity player, Entity computer, int numberOfShips) {
        this.gridSize = gridSize;
        this.gridSizeHorizontal = gridSizeHorizontal;
        this.gridSizeVertical = gridSizeVertical;
        this.player = player;
        this.computer = computer;
        this.numberOfShips = numberOfShips;
        System.out.println("Number of ships: L" + numberOfShips);
    }

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


        int botRandomX;
        int botRandomY;
        Random rand = new Random();


        for (Point p : player.clickedPoints) {
            // 12*gridSize azért kell, mert a jobboldali négyzetrácsok innen kezdődnek
            // a p egy 0 és 10 közötti szám, amit meg kell szorozni 30-al, mert a négyzetrácsok 30x30-asak
            // a 15 azért kell, mert a kör középpontját kell meghatározni
            int centerX = (gridSizeHorizontal+2)*gridSize + p.x * gridSize + gridSize/2;
            // a y koordinátát is ugyanúgy kell meghatározni, mint az x-et
            // de itt csak 30-at kell hozzáadni
            int centerY = gridSize + p.y * gridSize + gridSize/2;

            if (computer.ships[p.x][p.y] && !player.shots[p.x][p.y]) {
                foundShips++;
                System.out.println("Found ships: " + foundShips);
                player.shots[p.x][p.y] = true;
            }

            if (computer.ships[p.x][p.y]) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            // A -gridSize / 6 azért kell, mert a kört, a négyzet bal felső sarkából rajzolja
            g.fillOval(centerX - gridSize / 6, centerY - gridSize / 6, gridSize / 3, gridSize / 3);


        }
        if (shipCounter == numberOfShips) {
            Point botShot;
            do {
                botRandomX = rand.nextInt(gridSizeHorizontal);
                botRandomY = rand.nextInt(gridSizeVertical);
                botShot = new Point(botRandomX, botRandomY);
            } while (computer.clickedPoints.contains(botShot));
            computer.clickedPoints.add(botShot);

            if (player.ships[botRandomX][botRandomY] && !computer.shots[botRandomX][botRandomY]) {
                shipsFoundByBot++;
                System.out.println("Ships found by bot: " + shipsFoundByBot);
                computer.shots[botRandomX][botRandomY] = true;
            }
        }


        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(player.ships[i][j]) {
                    g.setColor(Color.GRAY);
                    g.fillRect(i * gridSize + gridSize, j * gridSize + gridSize, gridSize, gridSize);
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
        if(foundShips == 10) {
            g.setColor(Color.GREEN);
            g.drawString("You won!", 12*gridSize, 11*gridSize);
        } else if (shipsFoundByBot == 10) {
            g.setColor(Color.RED);
            g.drawString("You lost!", 12*gridSize, 11*gridSize);

        }
    }

    public void addOneToShipCounter() {
        shipCounter++;
    }
}
