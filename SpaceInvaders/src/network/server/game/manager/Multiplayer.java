package network.server.game.manager;

import logic.manager.game.Game;
import logic.manager.game.States;
import logic.player.Player;
import logic.player.Team;
import logic.sprite.Coordinate;
import logic.sprite.dinamic.SpaceShip;
import logic.sprite.dinamic.bullets.Bullet;
import logic.thread.ThreadUpdate;
import network.data.MessageBuilder;
import java.util.concurrent.ConcurrentHashMap;

public class Multiplayer extends Game {
    private Team team;
    private States gameState;
    private ThreadUpdate threadUpdate;
    private MessageBuilder messageBuilder;
    private static int DELTA = 1; //tempo aggiornamento update

    public Multiplayer(MessageBuilder messageBuilder){
        this.messageBuilder = messageBuilder;
        team = new Team();
        gameState = States.WAITING;
    }

    public Player init(int ID){
        Coordinate coordinate = new Coordinate(0,0);
        SpaceShip defaultShip = new SpaceShip(coordinate);
        defaultShip.init();
        Player player = new Player(Integer.toString(ID), defaultShip);
        team.addPlayer(ID, player);
        return player;
    }

    public void startGame(){
        super.startGame();
        gameState = States.START;
        update(DELTA);
    }

    public void stopGame() {
        super.stopThreadInvader();
        if(isThreadRunning()) {
            threadUpdate.stop();
        }
        team.clear();
        gameState = States.WAITING;
    }

    /**
     * Attivazione thread di aggiornamento di tutti gli elementi presenti sul campo di gioco
     */
    public void update(int delta) {
        threadUpdate = new ThreadUpdate(this, messageBuilder, delta);
        threadUpdate.start();
    }

    public void setGameState(States gameState){
        this.gameState = gameState;
    }

    public Team getTeam(){
        return team;
    }

    public States getGameState(){
        return gameState;
    }

    public ConcurrentHashMap<Integer, Player> getPlayers(){
        return team.getPlayers();
    }

    public SpaceShip getSpaceShip(int ID){
        return getPlayers().get(ID).getSpaceShip();
    }

    public Bullet getSpaceShipBullet(int ID){
        return getSpaceShip(ID).getShipBullet();
    }

}
