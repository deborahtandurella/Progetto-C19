package logic.sprite.unmovable;

import logic.sprite.Coordinate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Bunker {

    private double brick_size;
    private ArrayList<Brick> bricks;


    public Bunker(double index_x, double index_y, double brick_size) {
        this.brick_size = brick_size;
        createBunker(index_x, index_y);
    }

    public ArrayList<Brick> getBricks() {
        return bricks;
    }

    /**
     * Creazione bunker da lettura file in cui è presente la struttura da creare.
     *
     * @param index_x: indice di partenza coordinata x
     * @param index_y: indice di partenza coordinata x
     */
    public void createBunker(double index_x, double index_y) {
        bricks = new ArrayList<>();
        double ind_x = index_x;
        double ind_y = index_y;

        try {
            BufferedReader in = new BufferedReader(new FileReader("res/bunker.txt"));
            String riga = in.readLine();
            while (riga != null) {
                for (int i = 0; i < riga.length(); i++) {
                    if (riga.charAt(i) == '*') {
                        Coordinate coordinate = new Coordinate(ind_x, ind_y);
                        Brick brick = new Brick(coordinate, brick_size);
                        bricks.add(brick);
                        ind_x++;
                    }
                    else {
                        ind_x++;
                    }
                }
                ind_y--;
                riga = in.readLine();
                ind_x = index_x;
            }
        }catch (IOException err) {
            System.err.println(err.getMessage());
        }
    }

}
