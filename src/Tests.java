import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Tests{
    private TorpedoGame game;
    private Player player;
    private Computer computer;

    @BeforeEach
    public void setUp() {
        player = new Player(10, 10);
        computer = new Computer(10, 10);
        game = new TorpedoGame(new JFrame(), player, computer);
    }

    @Test
    public void testPlayerAddShip() {
        Point first = new Point(0, 0);
        Point last = new Point(0, 1);
        game.drawShip(first, last);
        assertTrue(player.ships[0][0]);
        assertTrue(player.ships[0][1]);
    }

    @Test
    public void testComputerPlaceShips() {
        computer.placeShips(new int[]{1, 1, 1, 1}, 10, 10);
        assertEquals(4, computer.shipsList.size());
    }

    @Test
    public void testAreAllShipsSunk() {
        ArrayList<Point> coordinates = new ArrayList<>();
        coordinates.add(new Point(0, 0));
        coordinates.add(new Point(0, 1));
        Ship ship = new Ship(coordinates);
        player.shipsList.add(ship);
        ship.setSunk();
        assertTrue(game.areAllShipsSunk(player));
    }

    @Test
    public void testIsShipSunk() {
        ArrayList<Point> coordinates = new ArrayList<>();
        coordinates.add(new Point(0, 0));
        coordinates.add(new Point(0, 1));
        Ship ship = new Ship(coordinates);
        player.shipsList.add(ship);
        player.shots[0][0] = true;
        player.shots[0][1] = true;
        assertTrue(game.isShipSunk(ship, player));
    }

    @Test
    public void testCheckAllShips() {
        ArrayList<Point> coordinates = new ArrayList<>();
        coordinates.add(new Point(0, 0));
        coordinates.add(new Point(0, 1));
        Ship ship = new Ship(coordinates);
        player.shipsList.add(ship);
        player.shots[0][0] = true;
        player.shots[0][1] = true;
        game.checkAllShips(player, player);
        assertTrue(ship.isSunk());
    }

    @Test
    public void testIsOverlapping() {
        Point first = new Point(0, 0);
        Point last = new Point(0, 1);
        game.drawShip(first, last);
        assertTrue(game.isOverlapping(first, last));
    }

    @Test
    public void testDrawShip() {
        Point first = new Point(0, 0);
        Point last = new Point(0, 1);
        game.drawShip(first, last);
        assertTrue(player.ships[0][0]);
        assertTrue(player.ships[0][1]);
    }

    @Test
    public void testRandomShot() {
        Point shot = computer.randomShot(player, 10, 10);
        assertNotNull(shot);
    }


    @Test
    public void isBetween() {
        assertTrue(TorpedoGame.between(0, 0, 1));
        assertFalse(TorpedoGame.between(0, 1, 2));
    }
}