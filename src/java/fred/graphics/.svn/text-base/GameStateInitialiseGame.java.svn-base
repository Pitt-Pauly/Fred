package fred.graphics;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fred.datahandling.MapLoader;
import fred.datahandling.SaveLoader;
import fred.model.Game;
import fred.model.BasicGame;
import fred.model.Player;
import fred.model.BasicPlayer;
import fred.model.World;
import fred.model.Player.PlayerType;
import fred.middle.Middle;


public class GameStateInitialiseGame extends BasicGameState {

    public static final int STATE_ID = 12;

    private GameUI stateGame;

    public void init(GameContainer container, StateBasedGame stategame) throws SlickException {
        this.stateGame = (GameUI) stategame;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame stategame) throws SlickException {
    	if(stateGame.saveName != null) {
    		load(container);
    	} else if(stateGame.players != null) {
    		loadMap(container);
    	} else {
        	List<Player> players = new ArrayList<Player>();
            Player player1 = new BasicPlayer("Orange", Color.orange, PlayerType.AI);
            Player player2 = new BasicPlayer("Cyan", Color.cyan, PlayerType.AI);
            Player player3 = new BasicPlayer("Black", Color.black, PlayerType.AI);
            players.add(player1);
            players.add(player2);
            players.add(player3);
            stateGame.players = players;
            loadMap(container);
        }        
        
        stateGame.enterState(GameStateMainGame.STATE_ID);
    }
    
    void load(GameContainer container) throws SlickException {
    	SaveLoader loader = new SaveLoader();
    	stateGame.game = loader.load(stateGame.saveName, "World.txt");
    	stateGame.worldUI = new WorldUI(container, loader.getTerritoryUIs());
    	stateGame.worldUI.setGameInfo(stateGame.game);
    }
    
    void loadMap(GameContainer container) throws SlickException {
    	MapLoader mapLoader = new MapLoader();
        World world = mapLoader.load("World.txt", Game.Type.Basic);

        Game game = new BasicGame(stateGame.players, world);
        game.setup();
        stateGame.game = new Middle(game, game);
        
        stateGame.worldUI = new WorldUI(container, mapLoader.getTerritoryUIs());
        stateGame.worldUI.setGameInfo(stateGame.game);
    }

    public void update(GameContainer container, StateBasedGame stategame, int delta) throws SlickException {
        // not used
    }

    public void render(GameContainer container, StateBasedGame stagegame, Graphics g) throws SlickException {
        // not used
    }

    @Override
    public int getID() {
        return STATE_ID;
    }

}