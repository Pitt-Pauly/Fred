package fred.graphics;

import static org.junit.Assert.assertEquals;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameStateMainMenuTest {

    GameStateMainMenu mainMenu = new GameStateMainMenu();
    
    private final GameUI game = new GameUI("test");
    
    private TestGameContainer gameContainer;

    @Test
    public void testGetID() {
        assertEquals(GameStateMainMenu.STATE_ID, mainMenu.getID());
    }

    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testKeyPressed() throws Exception {
//        Display.setDisplayMode(new DisplayMode(800,600));
//        Display.setTitle("An example title");
//        Display.create();
//        
        mainMenu.init(gameContainer, game);
        
        mainMenu.keyPressed(Input.KEY_DELETE, 'c');
        
        mainMenu.keyPressed(Input.KEY_ESCAPE, 'c');
    }

    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testInit() throws Exception {
        mainMenu.init(gameContainer, game);
        // Just check no exceptions
    }
    
//    @Before
//    public void setUp() throws SlickException {
//        gameContainer = new TestGameContainer(new TestGame());
//        gameContainer.setup();
//    }
//    
//    @After
//    public void tearDown() {
//        gameContainer.destroy();
//    }
    
    class TestGameContainer extends AppGameContainer {

        public TestGameContainer(Game game) throws SlickException {
            super(game);
        }
        
        @Override
        protected void setup() throws SlickException {
            super.setup();
        }
        
        @Override
        public void destroy() {
            super.destroy();
        }
    }
    
    class TestGame extends BasicGame {

        public TestGame() {
            super("test");
            // TODO Auto-generated constructor stub
        }

        @Override
        public void init(GameContainer container) throws SlickException {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void update(GameContainer container, int delta) throws SlickException {
            // TODO Auto-generated method stub
            
        }

        public void render(GameContainer container, Graphics g) throws SlickException {
            // TODO Auto-generated method stub
            
        }
        
    }

}
