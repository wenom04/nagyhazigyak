import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Computer extends Entity {
    private Point lastHit;

    public Computer(int gridSizeHorizontal, int gridSizeVertical) {
        super(gridSizeHorizontal, gridSizeVertical);
    }

    /**
     * A hajók elhelyezése a játéktéren
     * @param shipNums a hajók száma a hosszuk szerint
     * @param gridSizeHorizontal a játéktér szélessége
     * @param gridSizeVertical a játéktér magassága
     */
    @Override
    public void placeShips(int[] shipNums, int gridSizeHorizontal, int gridSizeVertical) {
        Random rand = new Random();
        for (int sizeIndex = 0; sizeIndex < shipNums.length; sizeIndex++) {
            int shipSize = sizeIndex + 2; // 2, 3, 4, 5 hosszúságú hajók
            int count = shipNums[sizeIndex];

            for (int i = 0; i < count; i++) {
                boolean placed = false;
                while (!placed) {
                    int x = rand.nextInt(gridSizeHorizontal);
                    int y = rand.nextInt(gridSizeVertical);
                    boolean horizontal = rand.nextBoolean();

                    if (canPlaceShip(x, y, shipSize, horizontal, gridSizeHorizontal, gridSizeVertical)) {
                        placeShip(x, y, shipSize, horizontal);
                        placed = true;
                    }
                }
            }
        }
    }

    /**
     * Ellenőrzi, hogy el lehet-e helyezni a hajót a megadott helyen
     * @param x a hajó első cellájának x koordinátája
     * @param y a hajó első cellájának y koordinátája
     * @param size a hajó hossza
     * @param horizontal a hajó vízszintes-e
     * @param gridSizeHorizontal a játéktér szélessége
     * @param gridSizeVertical a játéktér magassága
     * @return true, ha el lehet helyezni a hajót, false egyébként
     */
    private boolean canPlaceShip(int x, int y, int size, boolean horizontal, int gridSizeHorizontal, int gridSizeVertical) {
        if (horizontal) {
            if (x + size > gridSizeHorizontal){
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (ships[x + i][y]) {
                    return false;
                }
            }
        } else {
            if (y + size > gridSizeVertical){
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (ships[x][y + i]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Elhelyezi a hajót a megadott helyen
     * @param x a hajó első cellájának x koordinátája
     * @param y a hajó első cellájának y koordinátája
     * @param size a hajó hossza
     * @param horizontal a hajó vízszintes-e
     */
    private void placeShip(int x, int y, int size, boolean horizontal) {
        ArrayList<Point> coordinates = new ArrayList<>();
        if (horizontal) {
            for (int i = 0; i < size; i++) {
                ships[x + i][y] = true;
                coordinates.add(new Point(x + i, y));
            }
        } else {
            for (int i = 0; i < size; i++) {
                ships[x][y + i] = true;
                coordinates.add(new Point(x, y + i));
            }
        }
        shipsList.add(new Ship(coordinates));
    }

    /**
     * Véletlenszerű lövés
     * @param p a játékos
     * @param gridSizeHorizontal a játéktér szélessége
     * @param gridSizeVertical a játéktér magassága
     * @return a lövés koordinátái
     */
    @Override
    public Point randomShot(Entity p, int gridSizeHorizontal, int gridSizeVertical) {
        if(lastHit == null) {
            Point botShot;
            int botRandomX;
            int botRandomY;
            Random rand = new Random();
            do {
                botRandomX = rand.nextInt(gridSizeHorizontal);
                botRandomY = rand.nextInt(gridSizeVertical);
                botShot = new Point(botRandomX, botRandomY);
            } while (this.clickedPoints.contains(botShot));
            this.clickedPoints.add(botShot);
            if (p.ships[botRandomX][botRandomY] && !this.shots[botRandomX][botRandomY]) {
                lastHit = botShot;
            }
            this.shots[botRandomX][botRandomY] = true;
            return botShot;
        }
        else{
            return targetedShot(p,gridSizeHorizontal, gridSizeVertical);
        }
    }

    /**
     * Célozott lövés
     * @param player a játékos
     * @param gridSizeHorizontal a játéktér szélessége
     * @param gridSizeVertical a játéktér magassága
     * @return a lövés koordinátái
     */
    @Override
    public Point targetedShot(Entity player, int gridSizeHorizontal, int gridSizeVertical) {
        ArrayList<Point> possibleShots = new ArrayList<>();
        if(lastHit.x - 1 >= 0 && !this.shots[lastHit.x - 1][lastHit.y]) {
            possibleShots.add(new Point(lastHit.x - 1, lastHit.y));
        }
        if(lastHit.x + 1 < gridSizeHorizontal && !this.shots[lastHit.x + 1][lastHit.y]) {
            possibleShots.add(new Point(lastHit.x + 1, lastHit.y));
        }
        if(lastHit.y - 1 >= 0 && !this.shots[lastHit.x][lastHit.y - 1]) {
            possibleShots.add(new Point(lastHit.x, lastHit.y - 1));
        }
        if(lastHit.y + 1 < gridSizeVertical && !this.shots[lastHit.x][lastHit.y + 1]) {
            possibleShots.add(new Point(lastHit.x, lastHit.y + 1));
        }

        Collections.shuffle(possibleShots);
        this.clickedPoints.add(possibleShots.get(0));
        lastHit = null;
        return possibleShots.get(0);
    }
}
