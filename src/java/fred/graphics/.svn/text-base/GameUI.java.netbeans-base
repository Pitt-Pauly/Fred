package fred.graphics;

import static fred.graphics.UIUtils.handleException;

import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import fred.middle.Middle;
import fred.model.Player;

public class GameUI extends StateBasedGame {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 600;
    static final int INFOPANE_HEIGHT = 115;

    //TODO MENU, GAMEOVER, UNITPOPUP dimensions need to be redone
    // for proper rescaling, not hardcoded values.
    static final int MENU_HEIGHT = 260;
    static final int MENU_WIDTH = 180;
    static final int GAMEOVER_HEIGHT = 220;
    static final int GAMEOVER_WIDTH = 200;

    Middle game;    
    List<Player> players;
    WorldUI worldUI;
    String saveName;
    private Music music;
   
    public GameUI(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new GameStateIntro());
        addState(new GameStateMainMenu());
        addState(new GameStateReplay());
        addState(new GameStatePlayerMenu());
        addState(new GameStateMainGame());
        addState(new GameStateInitialiseGame());
        enterState(GameStateIntro.STATE_ID);
    }
    
    Music getMusic() throws SlickException {
        if (music == null) {
            music = new Music("music/fred-loops.ogg",true);
        }
        return music;
    }
    
    public static void main(String[] args) {
        try {
            Input.disableControllers();
            AppGameContainer app = new AppGameContainer(new GameUI("F.r.e.d."));
            app.setDisplayMode(800, 600 ,false);
            app.setTargetFrameRate(50);
            app.start();
        } catch (SlickException e) {
            handleException(e);
        }
    }

}
