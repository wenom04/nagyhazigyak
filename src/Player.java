import java.awt.*;

public class Player extends Entity {
    public Player(int gridSizeHorizontal, int gridSizeVertical) {
        super(gridSizeHorizontal, gridSizeVertical);
    }

    @Override
    public Point randomShot(Entity player, int gridSizeHorizontal, int gridSizeVertical, boolean wasHit) {
        return null;
    }

    @Override
    public Point targetedShot(Entity player, Point p, int gridSizeHorizontal, int gridSizeVertical) {
        return null;
    }

}
