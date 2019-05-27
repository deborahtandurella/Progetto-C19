package logic.environment;

import logic.FileManager.AddAccount;
import logic.FileManager.Login;
import logic.sprite.Coordinate;
import logic.player.Player;
import logic.sprite.dinamic.SpaceShip;
import org.newdawn.slick.util.Log;

import java.io.IOException;

public class Menu {

    private Ranking ranking;
    private Customization customization;
    private Field field;
    private double maxHeight;
    private double maxWidth;
    private double shipSize;
    private SpaceShip defaultShip;
    private Player player;

    public Menu(double maxWidth, double maxHeight){
        ranking = new Ranking();
        customization = new Customization();
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.shipSize = maxWidth/20;

        Coordinate coordinate = new Coordinate((maxWidth/2 - shipSize/2),(maxHeight - shipSize));
        defaultShip = new SpaceShip(coordinate,shipSize);
    }

    public boolean newAccount(String name, String password) throws IOException {
        Player newPlayer;

        if(!AddAccount.newAccount(name,password)){
            newPlayer = new Player(name,defaultShip);
            this.player= newPlayer;
            return true;
        }
        return false;
    }

    public boolean logIn(String name, String password)throws IOException{

        if(Login.login(name,password)){
            this.player = new Player(name,defaultShip);
            field = new Field(player, maxWidth, maxHeight);
            return true;
        }
        else{
            return false;
        }
    }

    public void logOut(){
        this.player = null;
    }

    //Probabilmente ci sará da fare un' eccezione
    public void startGame(){
        if(player != null){
            field = new Field(player, maxWidth, maxHeight);
        }

//        //***************************************//
//        //DA TOGLIERE ALLA FINE E LASCIARE QUELLO SOPRA
//        Player player = new Player("arr",defaultShip);
//        field = new Field(player, maxWidth, maxHeight);
//        //***************************************//
    }

    //A programma sistemato togliere questo getter e fare che startGame restituisca il field
    public Field getField() {
        return field;
    }

    public Player getPlayer() {
        return player;
    }


    public Ranking getRanking() {
        return ranking;
    }
}
