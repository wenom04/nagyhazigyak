import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Entity implements Serializable {
    boolean[][] ships;
    boolean[][] shots;
    ArrayList<Point> clickedPoints = new ArrayList<>();
    ArrayList<Ship> shipsList = new ArrayList<>();

    public Entity(int gridSizeHorizontal, int gridSizeVertical) {
        ships = new boolean[gridSizeHorizontal][gridSizeVertical];
        shots = new boolean[gridSizeHorizontal][gridSizeVertical];
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].length; j++) {
                ships[i][j] = false;
                shots[i][j] = false;
            }
        }
    }

    public void placeShips(int[] shipNums, int gridSizeHorizontal, int gridSizeVertical) {}

    public Point randomShot(Entity p, int gridSizeHorizontal, int gridSizeVertical){
        return null;
    }

    public Point targetedShot(Entity player, int gridSizeHorizontal, int gridSizeVertical){
        return null;
    }

    public void setShips(boolean[][] ships) {
        this.ships = ships;
    }

    public void setShots(boolean[][] shots) {
        this.shots = shots;
    }

}
