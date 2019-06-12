package logic.environment.manager.game;

public class Difficulty {

    private int difficulty;

    public Difficulty(){
        //millisecondi di sleep thread alieni
        this.difficulty = 900;
    }

    public void incrementDifficulty(){
        if(difficulty >= 500){
            difficulty -= 100;
        }
    }

    public int getDifficulty() {
        return difficulty;
    }
}
