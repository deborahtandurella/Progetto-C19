package gui.states;

import logic.environment.manager.game.MovingDirections;
import logic.environment.manager.game.OfflineGameManager;
import logic.player.Player;
import logic.sprite.dinamic.SpaceShip;
import logic.sprite.dinamic.bullets.Bullet;
import logic.sprite.dinamic.bullets.SpaceShipBullet;
import logic.thread.ThreadInvader;
import network.server.Commands;
import network.server.GameStates;

public class SinglePlayer {

    private Player player;
    private OfflineGameManager offlineGameManager;

    private ThreadInvader threadInvader;
    public boolean newThread;

    public SinglePlayer(Player player, OfflineGameManager offlineGameManager) {
        this.player = player;
        this.offlineGameManager = offlineGameManager;
        player.getSpaceShip().init();
        newThread = false;
    }

    public void execCommand(Commands commands){
        switch (commands){
            case MOVE_LEFT:
                offlineGameManager.shipMovement(player.getSpaceShip(), MovingDirections.LEFT);
                break;
            case MOVE_RIGHT:
                offlineGameManager.shipMovement(player.getSpaceShip(),MovingDirections.RIGHT);
                break;
            case SHOT:
                offlineGameManager.shipShot(player.getSpaceShip());
                break;
            case EXIT:
                threadInvader.stop();
                break;
        }
    }

    public void loop() {
        if (getSpaceShipBullet() != null) {
            player.getSpaceShip().getShipBullet().move();
        }
        if (getSpaceShipBullet() != null) {
            offlineGameManager.checkSpaceShipShotCollision(getSpaceShip());
        }
        for (Bullet bullet : offlineGameManager.getInvaderBullets()) {
            bullet.move();
        }
        offlineGameManager.checkInvaderShotCollision(getSpaceShip());
        threadManager();
    }

    public GameStates checkGameState(){
        if (offlineGameManager.isGameOver()) {
            threadInvader.stop();
            offlineGameManager.checkHighscore(player);

            if (offlineGameManager.isNewHighscore()) {
                offlineGameManager.setNewHighscore(true);
                return GameStates.NEWHIGHSCORE;
            }
            return GameStates.GAMEOVER;
        }
        return null;
    }

    private void threadManager(){

        if (!newThread) {
            threadInvader = new ThreadInvader(offlineGameManager.getDifficulty(), offlineGameManager);
            threadInvader.start();
            newThread = true;
        }
        if (offlineGameManager.isNewLevel()) {
            threadInvader.stop();
            offlineGameManager.setNewLevel(false);
            newThread = false;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public SpaceShip getSpaceShip(){
        return player.getSpaceShip();
    }

    public SpaceShipBullet getSpaceShipBullet(){
        return getSpaceShip().getShipBullet();
    }

    public OfflineGameManager getOfflineGameManager() {
        return offlineGameManager;
    }
}

