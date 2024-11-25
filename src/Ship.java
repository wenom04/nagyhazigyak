import java.awt.*;
import java.util.ArrayList;

public class Ship implements java.io.Serializable {
    private boolean isSunk;
    private ArrayList<Point> coordinates;

    /**
     * Konstruktor
     * @param coordinates Egy lista, ami a hajó koordinátáit tartalmazza
     */
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

    public int getLength() {
        return coordinates.size();
    }

}
