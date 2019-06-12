package logic.environment.creators;

import logic.sprite.unmovable.Bunker;

import java.util.ArrayList;

public class BunkersCreator implements Creator{
    private double maxHeight;
    private double maxWidth;
    private double brickSize;

    private ArrayList<Bunker> bunkers;

    public BunkersCreator(double maxHeight,double maxWidth, double brickSize){
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.brickSize = brickSize;

        bunkers = new ArrayList<>();
    }

    /**
     * Inizializzazione della lista di bunker, con attenzione particolare alla distanza tra ognuno di essi
     * proporzionale alla dimensione della schermata di gioco
     */
    public ArrayList<Bunker> create(){
        double baseX = (maxWidth - 35*brickSize)/2;
        double baseY = (maxHeight - 4*brickSize);
        double x = baseX;

        for(int i=1; i<5;i++){
            Bunker bunker = new Bunker(x,baseY, brickSize);
            bunkers.add(bunker);
            x = baseX + (10*brickSize)*i;
        }
        return bunkers;
    }
}
