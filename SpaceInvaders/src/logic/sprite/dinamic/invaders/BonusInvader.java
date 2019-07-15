package logic.sprite.dinamic.invaders;

import logic.sprite.Coordinate;
import logic.sprite.Sprite;
import main.Dimensions;

public class BonusInvader extends Sprite {

    private final int value = 100;

    public BonusInvader(Coordinate coordinate){
        super(coordinate, Dimensions.BONUSINVADER_WIDTH, Dimensions.BONUSINVADER_HEIGHT);
    }

    /**
     * A differenza degli altri invaders, quello bonus si sposta solo verso sinistra
     * @param delta tempo di aggionramento della grafica
     */
    public void moveLeft(int delta){
        float HORIZONTAL_OFFSET = 0.02f;
        super.setX(super.getX() - HORIZONTAL_OFFSET * delta);
    }

    public int getValue() {
        return value;
    }

}