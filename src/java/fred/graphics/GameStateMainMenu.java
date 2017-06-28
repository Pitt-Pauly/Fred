package fred.graphics;

import static fred.graphics.UIUtils.createImage;
import static fred.graphics.ImagePaths.GENERAL_BACKGROUND;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameStateMainMenu extends BasicGameState {

    public static final int STATE_ID = 2;

    private Image background;    

    private static final float SCALAR = 0.6f;

    private GameContainer gameContainer;
    private StateBasedGame stateGame;
    private MainMenuPopUp menu;

    @Override
    public int getID() {
        return STATE_ID;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        background = createImage(GENERAL_BACKGROUND);
        gameContainer = container; 
        stateGame = game;
        menu = new MainMenuPopUp(container, stateGame, SCALAR);
        menu.setAcceptingInput(false);
    }
 
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        background.draw(0, 0);
        menu.render(container, g);
    }

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // Nothing to do
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        menu.setAcceptingInput(true);
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        menu.setAcceptingInput(false);
    }
    
    @Override
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);
        if (key == Input.KEY_ESCAPE) {
            gameContainer.exit();
        }
    }
}
