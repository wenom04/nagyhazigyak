import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Computer extends Entity{
    public Computer(int gridSizeHorizontal, int gridSizeVertical) {
        super(gridSizeHorizontal, gridSizeVertical);
    }

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

    @Override
    public Point randomShot(Entity p, int gridSizeHorizontal, int gridSizeVertical, boolean wasHit) {
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
            this.shots[botRandomX][botRandomY] = true;
            wasHit = true;
            return botShot;
        }
        return null;
    }

    @Override
    public Point targetedShot(Entity player, Point p, int gridSizeHorizontal, int gridSizeVertical) {
        ArrayList<Point> possibleShots = new ArrayList<>();
        if(p.x - 1 >= 0) {
            possibleShots.add(new Point(p.x - 1, p.y));
        }
        if(p.x + 1 < gridSizeHorizontal) {
            possibleShots.add(new Point(p.x + 1, p.y));
        }
        if(p.y - 1 >= 0) {
            possibleShots.add(new Point(p.x, p.y - 1));
        }
        if(p.y + 1 < gridSizeVertical) {
            possibleShots.add(new Point(p.x, p.y + 1));
        }

        Collections.shuffle(possibleShots);

        for(Point point : possibleShots) {
            if(!this.clickedPoints.contains(point)) {
                this.clickedPoints.add(point);
                if(player.ships[point.x][point.y]) {
                    this.shots[point.x][point.y] = true;
                    return new Point(point.x, point.y);
                }
                else{
                    return p;
                }
            }
        }
        return null;
    }
}
