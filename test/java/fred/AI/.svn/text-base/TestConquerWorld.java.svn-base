package fred.AI;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import fred.datahandling.MapLoader;
import fred.model.Game;
import fred.model.BasicGame;
import fred.model.Player;
import fred.model.BasicPlayer;
import fred.model.Territory;
import fred.model.TurnPhase;
import fred.model.World;
import fred.model.Player.PlayerType;

import org.newdawn.slick.Color;

public class TestConquerWorld {
	public Game game;
	public AI ai;
	public PrintWriter log;
	
	public static void main(String[] args) {
		TestConquerWorld thing = new TestConquerWorld();
		thing.setupLog();
		thing.setupWorldGame();
		thing.ai = new AI(thing.game, thing.game);
		thing.doStuff();
		if (thing.log != null) {
        	thing.log.close();
    }
	}
	
	public void setupLog() {
		try {
			log = new PrintWriter(new FileWriter("lt.txt")); // Hard-coded paths rule.
		} catch (IOException e) {
			//
		}
	}
	
	public void doStuff() {
		int turn = 0;
		while(!finished() && turn < 50)
		{
			if(game.getCurrentTurnPhase() != TurnPhase.SetupUnitDeployment && log != null) {
				if(game.getCurrentPlayer().getName() == "Tom")  {
					turn++;
					log.println("==== Turn "+ turn+ "====");
				}
				log.println(game.getCurrentPlayer().getName());
				log.println();
			}
			//System.out.println("turn");
			ai.doTurn();
			if(game.getCurrentTurnPhase() != TurnPhase.SetupUnitDeployment && log != null) {
				logturn();
				//logPosition();
				log.flush();
			}
		}
		if(turn == 50 && log != null) {
			logPosition();
		}
		//System.out.println("finished");
	}
	private boolean finished() {
		Territory lastTerritory = null;
		for(Territory loopTerritory : game.getWorld().getTerritories()) {
			//System.out.println("territory");
			if(lastTerritory != null) {
				if(lastTerritory.getOwner() != loopTerritory.getOwner()) {
					return false;
				}
			}
			lastTerritory = loopTerritory;			
		}
		return true;
	}
	private void logturn() {
		for(Player player : game.getPlayers()) {
			int iNumTerritories = 0;
			int iNumArmies = 0;
			for(Territory territory : game.getWorld().getTerritories()) {
				if(territory.getOwner() == player) {
					iNumTerritories++;
					iNumArmies += territory.getUnitCount();
				}
			}
			log.println("Player: " + player.getName());
			log.println("Territories: " + iNumTerritories);
			log.println("Armies: " + iNumArmies);
			if(player.getFreeUnitCount() > 0) {
				log.println("Free Units: " + player.getFreeUnitCount());
			}
			log.println();
		}
	}
	
	public void logPosition() {
		AICacheNAlgorithms cs = new AICacheNAlgorithms(game);
		for(Territory loopTerritory : game.getWorld().getTerritories()) {
			log.println("Name: "+loopTerritory.getName());
			log.println("Owner: " + loopTerritory.getOwner().getName());
			log.println(" Units:" + loopTerritory.getUnitCount());
			log.println("Border Distance :" + cs.borderDistance(loopTerritory));
			log.println();
		}
	}
	
	
	/*private void setupGameState() {

        Territory territoryArgentina = new Territory("Argentina");
        Territory territoryPeru = new Territory("Peru");
        Territory territoryBrazil = new Territory("Brazil");
        Territory territoryVenezuela = new Territory("Venezuela");
        Territory territoryNorthAfrica = new Territory("North Africa");

        territoryArgentina.addNeighbour(territoryPeru);
        territoryArgentina.addNeighbour(territoryBrazil);
        territoryPeru.addNeighbour(territoryVenezuela);
        territoryBrazil.addNeighbour(territoryPeru);
        territoryBrazil.addNeighbour(territoryVenezuela);
        territoryNorthAfrica.addNeighbour(territoryBrazil);

        List<Territory> territories = new ArrayList<Territory>();
        territories.add(territoryArgentina);
        territories.add(territoryPeru);
        territories.add(territoryBrazil);
        territories.add(territoryVenezuela);
        territories.add(territoryNorthAfrica);

        List<Player> players = new ArrayList<Player>();
        Player player1 = new BasicPlayer("player1");
        Player player2 = new BasicPlayer("player2");
        players.add(player1);
        players.add(player2);

        List<TerritoryGroup> territoryGroups = new ArrayList<TerritoryGroup>();
        World world = new World(territories, territoryGroups);

        game = new BasicGame(players, world);
        
        territoryArgentina.setOwner(player1);
        territoryPeru.setOwner(player2);
        territoryBrazil.setOwner(player1);
        territoryVenezuela.setOwner(player2);
        territoryNorthAfrica.setOwner(player1);

        territoryArgentina.setUnitCount(5);
        territoryPeru.setUnitCount(10);
        territoryBrazil.setUnitCount(15);
        territoryVenezuela.setUnitCount(20);
        territoryNorthAfrica.setUnitCount(10);
        
        territoryArgentina.setUnitCount(1);
        territoryPeru.setUnitCount(1);
        territoryBrazil.setUnitCount(1);
        territoryVenezuela.setUnitCount(1);
        territoryNorthAfrica.setUnitCount(20);
        game.endCurrentTurnPhase();
	}*/
	public boolean setupWorldGame() {
		
		MapLoader loader = new MapLoader() {
		    @Override
		    protected void addTerritoryUI(String name, int posX, int posY, Color RGB) {
		        // Override to avoid unit test pain with graphics libraries
		    }
		};
		/*FileReader map = null;
		try {
			map = new FileReader("world.txt"); // Did I not say hard-coded paths rule?
		} catch( FileNotFoundException ex) {
			return false;
		}*/
        World world = loader.load("World.txt", Game.Type.Basic);
        
		List<Player> players = new ArrayList<Player>();
        players.add(new BasicPlayer("Tom", Color.black, PlayerType.AI));
        players.add(new BasicPlayer("Dick", Color.blue, PlayerType.AI));
        players.add(new BasicPlayer("Harry", Color.green, PlayerType.AI));
        
        game = new BasicGame(players, world);
        game.setup();
               
		return true;
	}
}
