package gui.states.single;

import gui.drawer.SpriteDrawer;
import gui.states.GameState;
import gui.states.IDStates;
import logic.manager.game.single.SinglePlayer;
import logic.manager.game.States;
import logic.manager.menu.Menu;
import logic.sprite.dinamic.bullets.Bullet;
import logic.sprite.dinamic.invaders.Invader;
import logic.sprite.unmovable.Brick;
import logic.sprite.unmovable.Bunker;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * Stato che rappresenta la partita del giocatore singolo.
 */
public class SinglePlayerState extends GameState {
    private SpriteDrawer spriteDrawer;
    private Menu menu;
    private SinglePlayer singlePlayer;

    public SinglePlayerState(Menu menu) {
        this.menu = menu;
        spriteDrawer = new SpriteDrawer();
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        super.init(gameContainer, stateBasedGame);
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        super.enter(gameContainer, stateBasedGame);
        singlePlayer = menu.getSinglePlayer();
        singlePlayer.startGame();
        spriteDrawer.addShipImage(menu.getCustomization().getCurrentShip());
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        super.render(gameContainer, stateBasedGame, graphics);
        Color color;
        int highscore;
        if (singlePlayer.getPlayer().getHighScore() >= singlePlayer.getSpaceShip().getCurrentScore()) {
            color = Color.white;
            highscore = singlePlayer.getPlayer().getHighScore();
        } else {
            color = Color.green;
            highscore = singlePlayer.getSpaceShip().getCurrentScore();
        }
        String textScore = "Score: " + singlePlayer.getSpaceShip().getCurrentScore();
        String textHighscore = "Highscore: " + highscore;
        getDataFont().drawString((gameContainer.getWidth() - getDataFont().getWidth(textScore))/2f,2*gameContainer.getHeight()/100f, textScore, color);
        getDataFont().drawString(2*gameContainer.getWidth()/100f,2*gameContainer.getHeight()/100f, textHighscore, Color.green);
        spriteRender();
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        super.update(gameContainer, stateBasedGame, delta);
        Input input = gameContainer.getInput();
        //STATO GIOCO
        if (singlePlayer.getGameState() == States.GAMEOVER) {
            stateBasedGame.enterState(IDStates.GAMEOVERSINGLE_STATE, new FadeOutTransition(), new FadeInTransition());
        }else if(singlePlayer.getGameState() == States.NEWHIGHSCORE) {
            stateBasedGame.enterState(IDStates.NEWHIGHSCORE_STATE, new FadeOutTransition(), new FadeInTransition());
        }
        if(getLife() > singlePlayer.getSpaceShip().getLife()){
            getAudioplayer().explosion();
        }
        setLife(singlePlayer.getSpaceShip().getLife());
        //COMANDI
        for(Integer key : getKeyboardKeys().keySet()){
            if(input.isKeyDown(key)){
                singlePlayer.execCommand(getKeyboardKeys().get(key), delta);
            }
        }
        singlePlayer.update(delta);
    }

    /**
     * Renderizzazione di tutti gli sprite del gioco.
     */
    private void spriteRender(){
        spriteDrawer.render(singlePlayer.getSpaceShip());
        if (singlePlayer.isBonusInvader()) {
            spriteDrawer.render(singlePlayer.getBonusInvader());
        }
        if (singlePlayer.getSpaceShip().isShipShot()) {
            spriteDrawer.render(singlePlayer.getSpaceShipBullet());
        }
        for (Invader invader : singlePlayer.getInvaders()) {
            spriteDrawer.render(invader);
        }
        for (Bullet invaderBullet : singlePlayer.getInvadersBullet()) {
            spriteDrawer.render(invaderBullet);
        }
        for (Bunker bunker : singlePlayer.getBunkers()) {
            for (Brick brick : bunker.getBricks()) {
                spriteDrawer.render(brick);
            }
        }
    }

     @Override
    public int getID() {
        return IDStates.SINGLEPLAYER_STATE;
    }
}