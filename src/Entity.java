import java.awt.*;
import java.util.ArrayList;

public class Entity {
    boolean[][] ships;
    boolean[][] shots;
    ArrayList<Point> clickedPoints = new ArrayList<>();

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
}
