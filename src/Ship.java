import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Ship {
    private boolean isSunk;
    private ArrayList<Point> coordinates;
    public Ship(ArrayList<Point> coordinates) {
        this.coordinates = coordinates;
        this.isSunk = false;
    }

    public ArrayList<Point> getCoordinates() {
        return coordinates;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void setSunk() {
        isSunk = true;
    }

    public int getLength(){
        return coordinates.size();
    }
}
