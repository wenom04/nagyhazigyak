import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler implements Serializable {
    Computer computer;
    Player player;
    int gridSizeHorizontal;
    int gridSizeVertical;
    int maxLength;
    int[] shipNums;
    List<Integer> alreadyPlacedShips = new ArrayList<>();
    int alreadyPlacedShipsNum;
    public FileHandler(Player player, Computer computer, int gridSizeHorizontal,
                       int gridSizeVertical, int maxLength, int[] shipNums, List<Integer> alreadyPlacedShips, int alreadyPlacedShipsNum) {
        this.player = player;
        this.computer = computer;
        this.gridSizeHorizontal = gridSizeHorizontal;
        this.gridSizeVertical = gridSizeVertical;
        this.maxLength = maxLength;
        this.shipNums = shipNums;
        this.alreadyPlacedShips = alreadyPlacedShips;
        this.alreadyPlacedShipsNum = alreadyPlacedShipsNum;
    }
    public static void saveGame(FileHandler gamestate) {
        try {
            FileOutputStream f =
                    new FileOutputStream("game_state.ser");
            ObjectOutputStream out =
                    new ObjectOutputStream(f);
            out.writeObject(gamestate);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileHandler loadGame() {
        try {
            FileInputStream f =
                    new FileInputStream("game_state.ser");
            ObjectInputStream in =
                    new ObjectInputStream(f);
            FileHandler gamestate = (FileHandler)in.readObject();
            in.close();
            return gamestate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getGridSizeHorizontal() {
        return String.valueOf(gridSizeHorizontal);
    }

    public String getGridSizeVertical() {
        return String.valueOf(gridSizeVertical);
    }
}
