package fred.graphics;

import static fred.graphics.ImagePaths.GENERAL_BACKGROUND;
import static fred.graphics.UIUtils.createImage;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameStatePlayerMenu extends BasicGameState {

    public static final int STATE_ID = 3;

    private static final float SCALAR = 0.6f;

    private Image background;
    private PlayerSetupPopUp playerSetupPopUp;

    GameUI stateGame;

    @Override
    public int getID() {
        return STATE_ID;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        background = createImage(GENERAL_BACKGROUND);

        this.stateGame = (GameUI) game;
        playerSetupPopUp = new PlayerSetupPopUp(container, stateGame, SCALAR);
        playerSetupPopUp.setAcceptingInput(false);
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        background.draw(0, 0);
        playerSetupPopUp.render(container, g);
    }

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // Nothing to do
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        playerSetupPopUp.setAcceptingInput(true);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        playerSetupPopUp.setAcceptingInput(false);
    }

    @Override
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);
        if (key == Input.KEY_ESCAPE) {
            stateGame.enterState(GameStateMainMenu.STATE_ID);
        }
    }
}
