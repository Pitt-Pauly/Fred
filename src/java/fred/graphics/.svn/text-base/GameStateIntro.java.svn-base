package fred.graphics;

import static fred.graphics.ImagePaths.INTRO_BACKGROUND;
import static fred.graphics.ImagePaths.INTRO_CLOVER_LOGO;
import static fred.graphics.ImagePaths.INTRO_FRED_LOGO;
import static fred.graphics.UIUtils.createImage;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


class GameStateIntro extends BasicGameState{

    public final static int STATE_ID = 7;

    private Image background;
    private Image fredIntro;
    private Image fredIntroClover;
    private float alpha = 0;
    private Color color = new Color(1f, 1f, 1f, alpha);
    private boolean displayClover = false;
    private GameUI stateGame;

    @Override
    public int getID() {
        return STATE_ID;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        background = createImage(INTRO_BACKGROUND);
        fredIntro = createImage(INTRO_FRED_LOGO);
        fredIntroClover = createImage(INTRO_CLOVER_LOGO);
        stateGame = (GameUI) game;
        stateGame.getMusic().play();
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        background.draw(0, 0);
        if (!displayClover) {
            g.drawImage(fredIntro, 0, 0, color);
        }
        else {
            g.drawImage(fredIntroClover, 0, 0);
        }
    }

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (alpha < 1) {
            container.sleep(50);
            alpha = (float) (alpha + 0.005);
            color = new Color(1f, 1f, 1f, alpha);
        }
        else {
            displayClover = true;
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);
        stateGame.enterState(GameStateMainMenu.STATE_ID);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        super.mousePressed(button, x, y);
        stateGame.enterState(GameStateMainMenu.STATE_ID);
    }

}
