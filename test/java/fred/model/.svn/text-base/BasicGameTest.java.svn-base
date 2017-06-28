package fred.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BasicGameTest {

	public List<Player> players = new ArrayList<Player>();
	public World world;
	public Territory territory1 = new BasicTerritory("1");
	public Territory territory2 = new BasicTerritory("2");
	public Territory territory3 = new BasicTerritory("3");
	private Territory territory4 = new BasicTerritory("4");
	private Territory territory5 = new BasicTerritory("5");
	public Player player1 = new BasicPlayer("player1");
	private Player player2 = new BasicPlayer("player2");
	private List<TerritoryGroup> territoryGroups = new ArrayList<TerritoryGroup>();
	private TerritoryGroup territoryGroup1;
	private TerritoryGroup territoryGroup2;

	private World conscriptionWorld;
	private Game basicTestGame;

	@Before
	public void setUp() {
		List<Territory> territories = new ArrayList<Territory>();
		territories.add(territory1);
		territories.add(territory2);
		territories.add(territory3);
		territories.add(territory4);
		territories.add(territory5);

		territory1.addNeighbour(territory2);
		territory1.addNeighbour(territory3);
		territory3.addNeighbour(territory1);
		territory2.addNeighbour(territory1);
		territory5.addNeighbour(territory1);
		territory1.addNeighbour(territory5);
		
		world = new World(territories, territoryGroups);

		players.add(player1);
		players.add(player2);

		territory1.setOwner(player1);
		territory1.setUnitCount(1);

		territory2.setOwner(player1);
		territory2.setUnitCount(2);

		territory3.setOwner(player2);
		territory3.setUnitCount(3);

		// Unconnected territory
		territory4.setOwner(player1);
		territory4.setUnitCount(4);

		territories = new ArrayList<Territory>();
		territories.add(territory1);
		territories.add(territory2);
		territoryGroup1 = new TerritoryGroup("tg1", territories, 2);

		territories = new ArrayList<Territory>();
		territories.add(territory3);
		territoryGroup2 = new TerritoryGroup("tg2", territories, 3);

		territoryGroups.add(territoryGroup1);
		territoryGroups.add(territoryGroup2);

		List<Territory> basicTestGameTerritories = new ArrayList<Territory>();
		for (int index = 0; index < 50; index++) {
			basicTestGameTerritories.add(new BasicTerritory("" + index));
		}

		// First 13 territories in a territory group
		List<Territory> territoriesInGroup = new ArrayList<Territory>();
		for (int index = 0; index < 13; index++) {
			territoriesInGroup.add(basicTestGameTerritories.get(index));
		}

		List<TerritoryGroup> basicTestGameTerritoryGroups = new ArrayList<TerritoryGroup>();
		basicTestGameTerritoryGroups.add(new TerritoryGroup("tg3",
				territoriesInGroup, 5));

		conscriptionWorld = new World(basicTestGameTerritories,
				basicTestGameTerritoryGroups);
		basicTestGame = new BasicGame(players, conscriptionWorld);
		basicTestGame.setCurrentTurnPhase(TurnPhase.TurnAction);
		assertEquals(player1, basicTestGame.getCurrentPlayer());
		player1.setFreeUnitCount(0);
		assertEquals(0, basicTestGame.getOwnedTerritoryCount(player1));
		assertEquals(0, basicTestGame.getOwnedTerritoryCount(player2));
	}

	@Test
	public void testConstructor_invalid() {
		try {
			new BasicGame(null, world);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}
		try {
			new BasicGame(players, null);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}
		try {
			new BasicGame(new ArrayList<Player>(), world);
			fail("empty player list should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		players.add(new BasicPlayer("player3"));
		players.add(new BasicPlayer("player4"));
		players.add(new BasicPlayer("player5"));
		players.add(new BasicPlayer("player6"));
		players.add(new BasicPlayer("player7"));
		
		try {
			new BasicGame(players, world);
			fail("Too many players in list, should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
		
		players.remove("player3");
		players.remove("player4");
		players.remove("player5");
		players.remove("player6");
		players.remove("player7");
		
		
		players.add(new AdvancedPlayer("Advanced"));
		
		try {
			new BasicGame(players, world);
			fail("Advanced player in list, should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
		
	}

	@Test
	public void testBasicSetup() {
		Game game = new BasicGame(players, world);
		assertSame(players, game.getPlayers());
		assertSame(world, game.getWorld());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(TurnPhase.SetupUnitDeployment, game.getCurrentTurnPhase());
	}

	@Test
	public void testNextPlayer_setup() {
		Game game = new BasicGame(players, world);
		player1.setFreeUnitCount(4);
		player2.setFreeUnitCount(1);
		player1.setRemainingDeployCount(0);
		player2.setRemainingDeployCount(0);
		game.setCurrentTurnPhase(TurnPhase.SetupUnitDeployment);

		assertEquals(player1, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player2, game.getCurrentPlayer());
		assertEquals(1, player2.getRemainingDeployCount());
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(2, player1.getRemainingDeployCount());
	}

	@Test
	public void testNextPlayer_setupSkippingPlayersWithoutUnits() {
		Game game = new BasicGame(players, world);
		player1.setFreeUnitCount(4);
		player2.setFreeUnitCount(0);
		game.setCurrentTurnPhase(TurnPhase.SetupUnitDeployment);

		assertEquals(player1, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());

		game.currentPlayerIndex = 1;
		assertEquals(player2, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
	}

	@Test
	public void testNextPlayer_setupNoUnitsLeftToDeploy() {
		Game game = new BasicGame(players, world);
		player1.setFreeUnitCount(0);
		player2.setFreeUnitCount(0);
		game.currentPlayerIndex = 1;
		game.setCurrentTurnPhase(TurnPhase.SetupUnitDeployment);

		assertEquals(player2, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(TurnPhase.TurnExchangeCards, game.getCurrentTurnPhase());
	}

	@Test
	public void testNextPlayer_turn() {
		Game game = new BasicGame(players, world);
		player1.setFreeUnitCount(0);
		player2.setFreeUnitCount(0);
		game.setCurrentTurnPhase(TurnPhase.TurnUnitDeployment);

		assertEquals(player1, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player2, game.getCurrentPlayer());
		assertTrue(player1.getFreeUnitCount() == 0);
		assertTrue(player2.getFreeUnitCount() > 0);
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
		assertTrue(player1.getFreeUnitCount() > 0);
		assertTrue(player2.getFreeUnitCount() > 0);
	}

	@Test
	public void testNextPlayer_turnSkippingPlayersWithoutTerritories() {
		Player player3 = new BasicPlayer("player3");
		players.add(player3);
		territory3.setOwner(player3);

		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnUnitDeployment);

		assertEquals(player1, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player3, game.getCurrentPlayer()); // skipping player2
	}

	@Test
	public void testNextPlayer_turnEndGame() {
		Player player3 = new BasicPlayer("player3");
		players.add(player3);
		Game game = new BasicGame(players, world);
		territory3.setOwner(player1);

		game.setCurrentTurnPhase(TurnPhase.TurnUnitDeployment);

		assertEquals(player1, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(TurnPhase.GameOver, game.getCurrentTurnPhase());

		game.currentPlayerIndex = 1;
		game.setCurrentTurnPhase(TurnPhase.TurnUnitDeployment);

		assertEquals(player2, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(TurnPhase.GameOver, game.getCurrentTurnPhase());
	}

	@Test
	public void testCurrentPlayerProgression_eliminatedPlayers() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnUnitDeployment);
		assertEquals(player1, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player2, game.getCurrentPlayer());
		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
	}

	

	@Test
	public void testAddFreeUnits() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnUnitDeployment);
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());

		try {
			game.addFreeUnits(0);
			fail("invalid count should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		try {
			game.addFreeUnits(-1);
			fail("invalid count should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		game.addFreeUnits(1);
		assertEquals(1, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(4, player2.getFreeUnitCount());
		game.addFreeUnits(3);
		assertEquals(1, player1.getFreeUnitCount());
		assertEquals(7, player2.getFreeUnitCount());
		assertEquals(player2, game.getCurrentPlayer());

		game.addFreeUnits(2);
		assertEquals(1, player1.getFreeUnitCount());
		assertEquals(9, player2.getFreeUnitCount());
		assertEquals(player2, game.getCurrentPlayer());
	}

	@Test
	public void testDistributeFreeUnits_invalid() {
		Game game = new BasicGame(players, world);
		game.addFreeUnits(5);
		assertEquals(5, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		game.setCurrentTurnPhase(TurnPhase.SetupUnitDeployment);
		player1.setRemainingDeployCount(1);
		assertTrue(game.canDistributeFreeUnits());

		try {
			game.distributeFreeUnits(territory1, 2);
			fail("More than Deploy count should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
		assertFalse(game.canDistributeFreeUnits(territory1, 2));

		player1.setRemainingDeployCount(6);
		game.setCurrentTurnPhase(TurnPhase.TurnUnitDeployment);
		assertTrue(game.canDistributeFreeUnits());

		try {
			game.distributeFreeUnits(territory1, 0);
			fail("invalid count should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
		assertFalse(game.canDistributeFreeUnits(territory1, 0));

		try {
			game.distributeFreeUnits(territory1, -1);
			fail("invalid count should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
		assertFalse(game.canDistributeFreeUnits(territory1, -1));

		try {
			game.distributeFreeUnits(territory1, 6);
			fail("too many should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
		assertFalse(game.canDistributeFreeUnits(territory1, 6));

		try {
			game.distributeFreeUnits(null, 1);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}
		try {
			game.canDistributeFreeUnits(null, 1);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}

		try {
			game.distributeFreeUnits(territory3, 1);
			fail("not owned should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
		assertFalse(game.canDistributeFreeUnits(territory3, 1));

		for (TurnPhase turnPhase : TurnPhase.values()) {
			if (turnPhase != TurnPhase.TurnUnitDeployment
					&& turnPhase != TurnPhase.TurnConscriptUnitDeployment
					&& turnPhase != TurnPhase.SetupUnitDeployment) {
				game.setCurrentTurnPhase(turnPhase);
				try {
					game.distributeFreeUnits(territory1, 1);
					fail("deployment in wrong phase should have failed");
				} catch (IllegalStateException ex) {
					// Expected exception
				}
				assertFalse(game.canDistributeFreeUnits());
				assertFalse(game.canDistributeFreeUnits(territory1, 1));
			}
		}
	}

	@Test
	public void testDistributeFreeUnits() {
		checkDistributeFreeUnits(TurnPhase.TurnUnitDeployment,
				TurnPhase.TurnAction);
	}

	@Test
	public void testDistributeFreeUnits_conscript() {
		checkDistributeFreeUnits(TurnPhase.TurnConscriptUnitDeployment,
				TurnPhase.TurnPostActionMovement);
	}

	private void checkDistributeFreeUnits(TurnPhase initialPhase,
			TurnPhase finalPhase) {
		Game game = checkDistributeFreeUnitsSetup(initialPhase);
		assertEquals(5, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());

		assertTrue(game.canDistributeFreeUnits());

		assertEquals(1, territory1.getUnitCount());
		assertTrue(game.canDistributeFreeUnits(territory1, 1));
		game.distributeFreeUnits(territory1, 1);
		assertEquals(2, territory1.getUnitCount());
		assertEquals(4, player1.getFreeUnitCount());
		assertEquals(initialPhase, game.getCurrentTurnPhase());

		assertEquals(2, territory2.getUnitCount());
		assertTrue(game.canDistributeFreeUnits(territory2, 4));
		game.distributeFreeUnits(territory2, 4);
		assertEquals(6, territory2.getUnitCount());
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(finalPhase, game.getCurrentTurnPhase());
	}

	public Game checkDistributeFreeUnitsSetup(TurnPhase initialPhase) {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(initialPhase);
		game.addFreeUnits(5);
		player1.setRemainingDeployCount(0);
		return game;
	}

	@Test
	public void testMoveUnits_invalid() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnUnlimitedMovement);
		territory1.setUnitCount(6);
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(6, territory1.getUnitCount());

		assertTrue(game.canMoveUnits());
		// TODO Calling turnphase
		try {
			game.moveUnits(territory1, territory2, 0);
			fail("invalid count should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		try {
			game.moveUnits(territory1, territory2, -1);
			fail("invalid count should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		try {
			game.moveUnits(territory1, territory2, 6);
			fail("too many should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		try {
			game.moveUnits(null, territory2, 1);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}

		try {
			game.moveUnits(territory1, null, 1);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}

		try {
			game.moveUnits(territory1, territory3, 1);
			fail("not owned should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		try {
			game.moveUnits(territory3, territory2, 1);
			fail("not owned should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		try {
			game.moveUnits(territory1, territory1, 1);
			fail("same territory should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		game.setCurrentTurnPhase(TurnPhase.TurnPostActionMovement);
		game.getCurrentPlayer().setRemainingMoveCount(1);
		assertEquals(game.getCurrentPlayer().getRemainingMoveCount(), 1);
		territory1.setUnitCount(6);

		assertTrue(game.canMoveUnits());

		try {
			game.moveUnits(territory1, territory2, 2);
			fail("Not enough movement remaining should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

		territory4.setOwner(player1);
		territory4.setUnitCount(4);
		territory2.addNeighbour(territory4);
		game.getCurrentPlayer().setRemainingMoveCount(2);

		try {
			game.moveUnits(territory1, territory4, 2);
			fail("Not enough movement remaining should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}

	}

	@Test
	public void testMoveUnits_invalidNotConnected() {
		Game game = new BasicGame(players, world);
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(1, territory1.getUnitCount());
		assertEquals(4, territory4.getUnitCount());

		try {
			game.moveUnits(territory4, territory1, 1);
			fail("not connected should have failed");
		} catch (IllegalArgumentException ex) {
			// Expected exception
		}
	}

	@Test
	public void testMoveUnits() {
		Game game = setupTestMoveUnits();
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(6, territory1.getUnitCount());
		assertEquals(2, territory2.getUnitCount());

		game.moveUnits(territory1, territory2, 5);
		assertEquals(1, territory1.getUnitCount());
		assertEquals(7, territory2.getUnitCount());
	}

	public Game setupTestMoveUnits() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnUnlimitedMovement);
		game.getCurrentPlayer().setRemainingMoveCount(1);
		assertEquals(1, game.getCurrentPlayer().getRemainingMoveCount());
		territory1.setUnitCount(6);
		return game;
	}

	@Test
	public void testMoveUnitsDistant() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnPostActionMovement);
		game.getCurrentPlayer().setRemainingMoveCount(11);
		assertEquals(11, game.getCurrentPlayer().getRemainingMoveCount());
		territory1.setUnitCount(10);
		territory4.setOwner(player1);
		territory4.setUnitCount(4);
		territory2.addNeighbour(territory4);
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(10, territory1.getUnitCount());
		assertEquals(4, territory4.getUnitCount());

		assertEquals(9, game.maxUnitsToMove(territory1, territory2));
		assertEquals(5, game.maxUnitsToMove(territory1, territory4));

		game.moveUnits(territory1, territory4, 5);
		assertEquals(5, territory1.getUnitCount());
		assertEquals(9, territory4.getUnitCount());
		assertEquals(1, game.getCurrentPlayer().getRemainingMoveCount());
	}

	@Test
	public void testMoveUnitsLimited() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnPostActionMovement);
		game.getCurrentPlayer().setRemainingMoveCount(5);
		assertEquals(5, game.getCurrentPlayer().getRemainingMoveCount());
		territory1.setUnitCount(6);
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(6, territory1.getUnitCount());
		assertEquals(2, territory2.getUnitCount());

		game.moveUnits(territory1, territory2, 5);
		assertEquals(1, territory1.getUnitCount());
		assertEquals(7, territory2.getUnitCount());
		assertEquals(0, game.getCurrentPlayer().getRemainingMoveCount());
	}

	@Test
	public void testAttack_invalidParameters1Source() {
		Game game = new BasicGame(players, world);
		territory1.setUnitCount(6);
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(6, territory1.getUnitCount());
		assertEquals(3, territory3.getUnitCount());
		
		game.setCurrentTurnPhase(TurnPhase.TurnAttack);
		assertTrue(game.canAttack());
		
		ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1);
		sourceUnits.add(-1);
		
		checkAttack_invalidParameters(sourceTerritories, territory3, sourceUnits, "invalid unitcount should have failed", game);
        
		sourceUnits.clear();
		sourceUnits.add(6);
		
		checkAttack_invalidParameters(sourceTerritories, territory3, sourceUnits, "too many units should have failed", game);
        
		sourceUnits.clear();
		sourceUnits.add(1);
		
		checkAttack_invalidParameters(sourceTerritories, territory2, sourceUnits, "target territory owned should have failed", game);
        
        sourceTerritories.clear();
        sourceTerritories.add(territory3);
        sourceUnits.clear();
		sourceUnits.add(1);
		
        checkAttack_invalidParameters(sourceTerritories, territory2, sourceUnits, "not owned should have failed", game);
        
        sourceTerritories.clear();
        sourceTerritories.add(territory1);
        
		
		try {
            game.attack(null, territory3, sourceUnits, true, false);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }

        try {
            game.attack(sourceTerritories, null, sourceUnits, true, false);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }
        try {
            game.attack(sourceTerritories, territory3, null, true, false);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }

	}
	@Test
	public void testAttack_invalidParametersSeveralSources() {
		Game game = new BasicGame(players, world);
		territory1.setUnitCount(1);
		territory2.setUnitCount(2);
		territory3.setUnitCount(2);
		territory1.setOwner(player2);
		territory2.setOwner(player1);
		territory3.setOwner(player1);
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(1, territory1.getUnitCount());
		assertEquals(2, territory3.getUnitCount());
		assertEquals(2, territory2.getUnitCount());
		game.setCurrentTurnPhase(TurnPhase.TurnAttack);
		assertTrue(game.canAttack());
		
		ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory2);
		sourceUnits.add(1);
		sourceUnits.add(2);
		checkAttack_invalidParameters(sourceTerritories, territory1, sourceUnits, "unequal list lengths should have failed", game);

		sourceTerritories.add(territory3);
		sourceUnits.add(-1);
		sourceUnits.add(1);
		checkAttack_invalidParameters(sourceTerritories, territory1, sourceUnits, "invalid number of units should have failed", game);

		sourceUnits.clear();
		sourceUnits.add(6);
		sourceUnits.add(1);
		checkAttack_invalidParameters(sourceTerritories, territory1, sourceUnits, "too many units should have failed", game);
        
		sourceUnits.clear();
		sourceUnits.add(1);
		sourceUnits.add(1);
		checkAttack_invalidParameters(sourceTerritories, territory2, sourceUnits, "target territory also source territory should have failed", game);
        
		territory2.setOwner(player2);
        checkAttack_invalidParameters(sourceTerritories, territory1, sourceUnits, "not owned should have failed", game);
        
        territory2.setOwner(player1);
        territory1.setOwner(player1);
		checkAttack_invalidParameters(sourceTerritories, territory1, sourceUnits, "target territory owned should have failed", game);
        
		territory1.setOwner(player2);
		try {
            game.attack(null, territory1, sourceUnits, true, false);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }

        try {
            game.attack(sourceTerritories, null, sourceUnits, true, false);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }
        try {
            game.attack(sourceTerritories, territory1, null, true, false);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }
	}
	
	private void checkAttack_invalidParameters(ArrayList<Territory> sources, Territory target, ArrayList<Integer> unitCount, String failText, Game game){
	    
	    try {
            game.attack(sources, target, unitCount, true, false);
            fail(failText);
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
	}
	@Test
	public void testAttack_invalidPhase() {
		Game game = new BasicGame(players, world);
		territory1.setUnitCount(6);
		assertEquals(0, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(6, territory1.getUnitCount());
		assertEquals(3, territory3.getUnitCount());

		for (TurnPhase phase : TurnPhase.values()) {
			ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
			ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
			
			sourceTerritories.add(territory1);
			sourceUnits.add(1);
			
			if (phase != TurnPhase.TurnAction && phase != TurnPhase.TurnAttack) {
				game.setCurrentTurnPhase(phase);
				
				try {
                    game.attack(sourceTerritories, territory3, sourceUnits, true, false);
                    fail("wrong phase should have failed");
                } catch (IllegalArgumentException ex) {
                    // Expected exception
                }
				assertFalse(game.canAttack());
			}
		}
	}

    @Test
    public void testAttack_invalidNotConnected() {
        Game game = new BasicGame(players, world);
        game.setCurrentTurnPhase(TurnPhase.TurnAttack);
        assertTrue(game.canAttack());
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(2, territory2.getUnitCount());
        territory1.setUnitCount(2);
        
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory2); //territory 2 not connected to T3
		sourceUnits.add(1); //1 Unit should be legal in all cases

		
		//Single Source Attacks
 
        try {
            game.attack(sourceTerritories, territory3, sourceUnits, true, false);
            fail("not connected should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        
        //Multi-source attacks
        sourceTerritories.add(territory1);
        sourceUnits.add(1);
       
 
        try {
            game.attack(sourceTerritories, territory3, sourceUnits, true, false);
            fail("not connected should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

	@Test
	public void testAttack_overwhelmingOdds() {
		// It's a bug if someone is this lucky.
		Game game1 = new BasicGame(players, world);
        game1.setCurrentTurnPhase(TurnPhase.TurnAction);
        assertTrue(game1.canAttack());
        assertEquals(player1, game1.getCurrentPlayer());
        territory1.setUnitCount(2);
        territory3.setUnitCount(50);
        AttackRecord record;
        
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1);
		sourceUnits.add(1); //Overwhelming odds against winning
        
        record = game1.attack(sourceTerritories, territory3, sourceUnits, false, false);
        assertEquals(1, territory1.getUnitCount());
        assertEquals(territory3.getOwner(), player2);
        assertEquals(record.isSuccess(), false);
        
        territory1.setUnitCount(10001);
        territory3.setUnitCount(30);
        
        sourceUnits.clear();
        sourceUnits.add(10000); //Overwhelming odds for winning
        
        assertEquals(10000, (int) sourceUnits.get(0));
        assertEquals(10001, territory1.getUnitCount());
        assertEquals(TurnPhase.TurnAttack, game1.getCurrentTurnPhase());
		assertEquals(territory1.getOwner(), player1);
		assertEquals(territory3.getOwner(), player2);
		assertEquals(30, territory3.getUnitCount());
		assertEquals(1, sourceTerritories.size());
		assertEquals(sourceUnits.size(), 1);
        
		
		
		record = game1.attack(sourceTerritories, territory3, sourceUnits, false, false);
		assertEquals(1, territory1.getUnitCount());
		assertEquals(territory3.getOwner(), player1);
		assertEquals(TurnPhase.TurnAttack, game1.getCurrentTurnPhase());
	}

	@Test
	public void testAttack_fixedDie_AttackRecord() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnAttack);
		assertTrue(game.canAttack());
		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		assertTrue(game.canAttack());
		assertEquals(player1, game.getCurrentPlayer());
		territory1.setUnitCount(6);
		territory3.setUnitCount(3);

		TestDie die = new TestDie();
		game.getBattlefield().setDie(die);
		die.setValues(new int[] { 6, 6, 6, 6, 6, 6, 6, 1, 5, 5, 1, 6, 3, 3 });
		AttackRecord record;
		
		ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1); //want to attack from territory 1
		sourceUnits.add(5); //want to attack with 5 units
		
		record = game.attack(sourceTerritories, territory3, sourceUnits, false, false);

		assertEquals(1, sourceTerritories.size());
		
		assertEquals(record.attackerName, player1.getName());
		assertEquals(record.defenderName, player2.getName());

		//Creating List<String> from List<Integer>
		List<String> sourceTerritoriesNames = new ArrayList<String>();
		for (Territory source: sourceTerritories) {
			sourceTerritoriesNames.add(source.getName());
		}
		
		assertEquals(record.fromName, sourceTerritoriesNames);
		assertEquals(record.toName, territory3.getName());
		assertEquals(record.initialAttackers, sourceUnits);
		assertEquals(record.initialDefenders, 3);

		// 1st round 6,6,6 vs 6,6 defenders win on ties.
		assertEquals(record.getAttackersAfterRound(0), 3);
		assertEquals(record.getDefendersAfterRound(0), 3);

		// 2nd round 6,6,1 vs 5,5 attackers win both.
		assertEquals(record.getAttackersAfterRound(1), 3);
		assertEquals(record.getDefendersAfterRound(1), 1);

		// 3rd round 1,6,3 vs 3. defender only gets on die. it faces the best
		// attacker die.
		assertEquals(record.getAttackersAfterRound(2), 3);
		assertEquals(record.getDefendersAfterRound(2), 0);
		assertEquals(record.getAttackRolls(2).size(), 3);
		assertEquals(record.getAttackRolls(2).get(0), Integer.valueOf(6));
		assertEquals(record.getAttackRolls(2).get(1), Integer.valueOf(3));
		assertEquals(record.getAttackRolls(2).get(2), Integer.valueOf(1));
		assertEquals(record.getDefenceRolls(2).size(), 1);

		assertEquals(record.getRounds(), 3);
		assertEquals(record.getSurvivors(), 3);
		assertTrue(record.isSuccess());
		assertTrue(record.isFinished());

		assertEquals(TurnPhase.TurnAttack, game.getCurrentTurnPhase());

		String cardName = record.CardTerritoryName;
		Boolean hasCard = false;
		for (Card card : player1.getCards()) {
			if (card.getTerritory().getName() == cardName) {
				hasCard = true;
			}
		}

		assertEquals(player1.getCards().size(), 1);
		
		assertTrue(hasCard);
	}

	@Test
	public void testChooseTurnType_invalidPhase() {
		Game game = new BasicGame(players, world);

		for (TurnPhase phase : TurnPhase.values()) {
			if (phase != TurnPhase.TurnAction) {
				game.setCurrentTurnPhase(phase);
				try {
					game.chooseTurnType(TurnPhase.TurnUnlimitedMovement);
					fail("wrong phase should have failed");
				} catch (IllegalStateException ex) {
					// Expected exception
				}
				assertFalse(game.canChooseTurnType());
			}
		}

		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		assertTrue(game.canChooseTurnType());
		for (TurnPhase phase : TurnPhase.values()) {
			if (phase != TurnPhase.TurnAttack
					&& phase != TurnPhase.TurnConscriptUnitDeployment
					&& phase != TurnPhase.TurnUnlimitedMovement) {
				try {
					game.chooseTurnType(phase);
					fail("wrong type should have failed");
				} catch (IllegalArgumentException ex) {
					// Expected exception
				}
			}
		}
	}

    @Test
    public void testAttackSingle_loseRound_1Source() {
        List<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(2);
        int initialTargetUnits = 10000;
        int[] dieValues = { 1, 6, 6 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(1);
        boolean victory = false;
        int finalSourceUnits = 1;
        int finalTargetUnits = 10000;
        int finalAttackers = 0;
        int finalDefenders = 10000;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory1);
        Territory targetTerritory = territory2;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    
    @Test
    public void testAttackSingle_winRound_notWinTerritory_1Source() {
    	List<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(2);
        int initialTargetUnits = 10000;
        int[] dieValues = { 6, 1, 1 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(1);
        boolean victory = false;
        int finalSourceUnits = 1;
        int finalTargetUnits = 9999;
        int finalAttackers = 1;
        int finalDefenders = 9999;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory1);
        Territory targetTerritory = territory2;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    
    @Test
    public void testAttackSingle_winRound_winTerritory_1Source() {
    	ArrayList<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(4);
        int initialTargetUnits = 2;
        int[] dieValues = { 6, 6, 6, 1, 1 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(3);
        boolean victory = true;
        int finalSourceUnits = 1;
        int finalTargetUnits = 3;
        int finalAttackers = 3;
        int finalDefenders = 0;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory1);
        Territory targetTerritory = territory2;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    
    @Test
    public void testAttackSingle_drawRound_1Source() {
    	ArrayList<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(3);
        int initialTargetUnits = 10000;
        int[] dieValues = { 6, 1, 5, 1 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(2);
        boolean victory = false;
        int finalSourceUnits = 1;
        int finalTargetUnits = 9999;
        int finalAttackers = 1;
        int finalDefenders = 9999;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory1);
        Territory targetTerritory = territory2;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    
    @Test
    public void testAttackSingle_loseRound_3Sources() {
    	List<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(2);
        initialSourceUnits.add(2);
        initialSourceUnits.add(3);
        int initialTargetUnits = 10000;
        int[] dieValues = { 1, 1, 1, 6, 6 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(1);
        attackUnits.add(1);
        attackUnits.add(2);
        boolean victory = false;
        int finalSourceUnits = 3;
        int finalTargetUnits = 10000;
        int finalAttackers = 2;
        int finalDefenders = 10000;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory2);
        sourceTerritories.add(territory3);
        sourceTerritories.add(territory5);
        Territory targetTerritory = territory1;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    @Test
    public void testAttackSingle_drawRound_3Source() {
    	ArrayList<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(2);
        initialSourceUnits.add(2);
        initialSourceUnits.add(3);
        int initialTargetUnits = 10000;
        int[] dieValues = { 6, 1, 1, 5, 2 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(1);
        attackUnits.add(1);
        attackUnits.add(2);
        boolean victory = false;
        int finalSourceUnits = 3;
        int finalTargetUnits = 9999;
        int finalAttackers = 3;
        int finalDefenders = 9999;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory2);
        sourceTerritories.add(territory3);
        sourceTerritories.add(territory5);
        Territory targetTerritory = territory1;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    @Test
    public void testAttackSingle_winRound_winTerritory_3Source() {
    	List<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(2);
        initialSourceUnits.add(2);
        initialSourceUnits.add(3);
        int initialTargetUnits = 2;
        int[] dieValues = { 6, 6, 6, 1, 1 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(1);
        attackUnits.add(1);
        attackUnits.add(2);
        boolean victory = true;
        int finalSourceUnits = 3;
        int finalTargetUnits = 4;
        int finalAttackers = 4;
        int finalDefenders = 0;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory2);
        sourceTerritories.add(territory3);
        sourceTerritories.add(territory5);
        Territory targetTerritory = territory1;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    @Test
    public void testAttackSingle_winRound_notWinTerritory_3Source() {
    	List<Integer> initialSourceUnits = new ArrayList<Integer>();
        initialSourceUnits.add(2);
        initialSourceUnits.add(2);
        initialSourceUnits.add(3);
        int initialTargetUnits = 10000;
        int[] dieValues = { 6, 6, 6, 1, 1 };
        ArrayList<Integer> attackUnits = new ArrayList<Integer>();
        attackUnits.add(1);
        attackUnits.add(1);
        attackUnits.add(2);
        boolean victory = false;
        int finalSourceUnits = 3;
        int finalTargetUnits = 9998;
        int finalAttackers = 4;
        int finalDefenders = 9998;
        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
        sourceTerritories.add(territory2);
        sourceTerritories.add(territory3);
        sourceTerritories.add(territory5);
        Territory targetTerritory = territory1;
        checkAttackSingle(sourceTerritories, targetTerritory, initialSourceUnits, initialTargetUnits, dieValues, attackUnits, victory,
                finalSourceUnits, finalTargetUnits, finalAttackers, finalDefenders);
    }
    
    private void checkAttackSingle(ArrayList<Territory> sourceTerritories, Territory targetTerritory, List<Integer> initialSourceUnits, int initialTargetUnits, int[] dieValues, ArrayList<Integer> attackUnits, boolean victory,
            int finalSourceUnits, int finalTargetUnits, int finalAttackers, int finalDefenders) {
        Game game = new BasicGame(players, world);
        for(int index = 0; index < sourceTerritories.size(); index++) {
        	sourceTerritories.get(index).setUnitCount(initialSourceUnits.get(index));
        	sourceTerritories.get(index).setOwner(player1);
        }
        targetTerritory.setUnitCount(initialTargetUnits);
        targetTerritory.setOwner(player2);
        TestDie die = new TestDie();
        game.getBattlefield().setDie(die);
        die.setValues(dieValues);

        game.setCurrentTurnPhase(TurnPhase.TurnAction);
        
        assertEquals(player1, game.getCurrentPlayer());
        AttackRecord record = game.attack(sourceTerritories, targetTerritory, attackUnits, true, false);
        
        int leftoverSourceUnits = 0;
        for (Territory sourceTerritory: sourceTerritories) {
        	leftoverSourceUnits = leftoverSourceUnits + sourceTerritory.getUnitCount();
        }
        
        assertEquals(finalSourceUnits, leftoverSourceUnits);
        assertEquals(finalTargetUnits, targetTerritory.getUnitCount());
        assertEquals(victory ? player1 : player2, targetTerritory.getOwner());
        assertEquals(victory, record.isSuccess());
        assertEquals(victory ? 1 : 0, player1.getCardHandCount());
        assertEquals(attackUnits, record.initialAttackers);
        assertEquals(initialTargetUnits, record.initialDefenders);
        assertEquals(finalTargetUnits, record.getSurvivors());
        assertEquals(1, record.getRounds());
        assertEquals(finalAttackers, record.getAttackersAfterRound(0));
        assertEquals(finalDefenders, record.getDefendersAfterRound(0));
        assertEquals(TurnPhase.TurnAttack, game.getCurrentTurnPhase());
    }
    
    @Test
    public void testAttackSingle_winRound_winTerritory_sixthCard() {
        territory1.setOwner(player1);
        territory2.setOwner(player2);
        Game game = new BasicGame(players, world);
        territory1.setUnitCount(3);
        territory2.setUnitCount(2);
        TestDie die = new TestDie();
        game.getBattlefield().setDie(die);
        die.setValues(new int[] { 6, 6, 1, 1 });
        game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
        game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
        game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
        game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
        game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());

        game.setCurrentTurnPhase(TurnPhase.TurnAction);

        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1); //want to attack from territory 1
		sourceUnits.add(2); //want to attack with 2 units
		
        assertEquals(player1, game.getCurrentPlayer());
        AttackRecord record = game.attack(sourceTerritories, territory2, sourceUnits, true, false);
        assertEquals(1, territory1.getUnitCount());
        assertEquals(2, territory2.getUnitCount());
        assertEquals(player1, territory2.getOwner());
        assertEquals(true, record.isSuccess());
        assertEquals(5, player1.getCardHandCount());
        assertEquals(sourceUnits, record.initialAttackers);
        assertEquals(2, record.initialDefenders);
        assertEquals(2, record.getSurvivors());
        assertEquals(1, record.getRounds());
        assertEquals(2, record.getAttackersAfterRound(0));
        assertEquals(0, record.getDefendersAfterRound(0));
        assertEquals(TurnPhase.TurnAttack, game.getCurrentTurnPhase());
    }
    
    @Test
    public void testAttackSingle_withdrawToSingleTerritory(){
    	territory1.setOwner(player1);
        territory2.setOwner(player2);
        Game game = new BasicGame(players, world);
        territory1.setUnitCount(3);
        territory2.setUnitCount(2);
        TestDie die = new TestDie();
        game.getBattlefield().setDie(die);
        die.setValues(new int[] { 6, 2, 6, 1 });
        
        game.setCurrentTurnPhase(TurnPhase.TurnAction);

        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1); //want to attack from territory 1
		sourceUnits.add(2); //want to attack with 2 units
		
		assertEquals(player1, game.getCurrentPlayer());
		game.attack(sourceTerritories, territory2, sourceUnits, true, false);
		assertEquals(territory1.getUnitCount(), 1);
		assertTrue(!game.getBattlefield().isFinished());
		assertTrue(!game.getBattlefield().isCleared());
		assertEquals(game.getBattlefield().getAttackers(), 1);
		
	    AttackRecord record = game.attack(sourceTerritories, territory2, sourceUnits, true, true);
	    assertTrue(record.isWithdrawn());
		assertTrue(game.getBattlefield().isCleared());
	    assertEquals(territory1.getUnitCount(), 2);
	    
    }
    
    @Test
    public void testAttackSingle_withdrawToSeveralTerritories(){
    	territory1.setOwner(player1);
        territory2.setOwner(player2);
        Game game = new BasicGame(players, world);
        territory1.setUnitCount(3);
        territory2.setUnitCount(2);
        TestDie die = new TestDie();
        game.getBattlefield().setDie(die);
        die.setValues(new int[] { 6, 2, 6, 1 });
        
        game.setCurrentTurnPhase(TurnPhase.TurnAction);

        ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1); //want to attack from territory 1
		sourceUnits.add(2); //want to attack with 2 units
		
		assertEquals(player1, game.getCurrentPlayer());
		game.attack(sourceTerritories, territory2, sourceUnits, true, false);
		assertEquals(territory1.getUnitCount(), 1);
		assertTrue(!game.getBattlefield().isFinished());
		assertTrue(!game.getBattlefield().isCleared());
		assertEquals(game.getBattlefield().getAttackers(), 1);
		
	    AttackRecord record = game.attack(sourceTerritories, territory2, sourceUnits, true, true);
	    assertTrue(record.isWithdrawn());
		assertTrue(game.getBattlefield().isCleared());
	    assertEquals(territory1.getUnitCount(), 3); //Yes i have put this here to remind myself to start here, it should say two
	    
	    //TODO Change to several territories to attack from and withdraw to
	    
    }    

    @Test
	public void testAttackFull_defeat() {
		territory1.setOwner(player1);
		territory2.setOwner(player2);
		Game game = new BasicGame(players, world);
		territory1.setUnitCount(2);
		territory2.setUnitCount(10000);

		game.setCurrentTurnPhase(TurnPhase.TurnAction);

		ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1); //want to attack from territory 1
		sourceUnits.add(1); //want to attack with 1 unit
		
		assertEquals(player1, game.getCurrentPlayer());
		AttackRecord record = game.attack(sourceTerritories, territory2, sourceUnits, false, false);
		assertEquals(1, territory1.getUnitCount());
		assertEquals(player2, territory2.getOwner());
		assertEquals(false, record.isSuccess());
		assertEquals(0, player1.getCardHandCount());
        assertEquals(TurnPhase.TurnAttack, game.getCurrentTurnPhase());
	}

	@Test
	public void testAttackFull_victory() {
		territory1.setOwner(player1);
		territory2.setOwner(player2);
		territory3.setOwner(player2);
		Game game = new BasicGame(players, world);
		territory1.setUnitCount(10001);
		territory2.setUnitCount(2);

		game.setCurrentTurnPhase(TurnPhase.TurnAction);

		ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1); //want to attack from territory 1
		sourceUnits.add(10000); //want to attack with 10000 units
		
		assertEquals(player1, game.getCurrentPlayer());
		AttackRecord record = game.attack(sourceTerritories, territory2, sourceUnits, false, false);
		assertEquals(1, territory1.getUnitCount());
		assertEquals(player1, territory2.getOwner());
		assertEquals(true, record.isSuccess());
		assertEquals(1, player1.getCardHandCount());
        assertEquals(TurnPhase.TurnAttack, game.getCurrentTurnPhase());

		territory1.setUnitCount(10001);
		territory3.setUnitCount(2);

		game.setCurrentTurnPhase(TurnPhase.TurnAttack);

		sourceUnits.clear();
		sourceUnits.add(10000); //want to attack with 10000 units
		
		assertEquals(player1, game.getCurrentPlayer());
		record = game.attack(sourceTerritories, territory3, sourceUnits, false, false);
		assertEquals(1, territory1.getUnitCount());
		assertEquals(player1, territory3.getOwner());
		assertEquals(true, record.isSuccess());
		assertEquals(1, player1.getCardHandCount());
	}

	@Test
	public void testAttackFull_victory_sixthCard() {
		territory1.setOwner(player1);
		territory2.setOwner(player2);
		Game game = new BasicGame(players, world);
		territory1.setUnitCount(10001);
		territory2.setUnitCount(2);
		game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
		game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
		game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
		game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());
		game.getCurrentPlayer().addCardHandCard(game.cardDeck.getDealtCard());

		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		
		ArrayList<Territory> sourceTerritories = new ArrayList<Territory>();
		ArrayList<Integer> sourceUnits = new ArrayList<Integer>();
		
		sourceTerritories.add(territory1); //want to attack from territory 1
		sourceUnits.add(10000); //want to attack with 10000 units
		
		assertEquals(player1, game.getCurrentPlayer());
		AttackRecord record = game.attack(sourceTerritories, territory2, sourceUnits, false, false);
		assertEquals(1, territory1.getUnitCount());
		assertEquals(player1, territory2.getOwner());
		assertEquals(true, record.isSuccess());
		assertEquals(5, player1.getCardHandCount());
        assertEquals(TurnPhase.TurnAttack, game.getCurrentTurnPhase());
	}

	@Test
	public void testChooseTurnType() {
		Game game = new BasicGame(players, world);

		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		game.chooseTurnType(TurnPhase.TurnAttack);
		assertEquals(TurnPhase.TurnAttack, game.getCurrentTurnPhase());

		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		game.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
		assertEquals(TurnPhase.TurnConscriptUnitDeployment, game
				.getCurrentTurnPhase());

		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		game.chooseTurnType(TurnPhase.TurnUnlimitedMovement);
		assertEquals(TurnPhase.TurnUnlimitedMovement, game
				.getCurrentTurnPhase());
	}

	@Test
	public void testSetCurrentTurnPhase() {
		Game game = new BasicGame(players, world);

		// allows unit test override
		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		assertEquals(TurnPhase.TurnAction, game.getCurrentTurnPhase());
		game.setCurrentTurnPhase(TurnPhase.TurnPostActionMovement);
		assertEquals(TurnPhase.TurnPostActionMovement, game
				.getCurrentTurnPhase());
	}

	@Test
	public void testEndCurrentTurnPhase_invalid() {
		Game game = new BasicGame(players, world);

		game.setCurrentTurnPhase(null);
		try {
			game.endCurrentTurnPhase();
			fail("null phase should not happen");
		} catch (IllegalStateException ex) {
			// Expected exception
		}

		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		try {
			game.endCurrentTurnPhase();
			fail("ambiguous phase - not sure of next phase");
		} catch (IllegalStateException ex) {
			// Expected exception
		}
	}

	@Test
	public void testEndCurrentTurnPhase_exchangeCards_UnitDeployment() {
		Game game = new BasicGame(players, world);

		game.setCurrentTurnPhase(TurnPhase.TurnExchangeCards);
		game.endCurrentTurnPhase();
		assertSame(game.getCurrentTurnPhase(), TurnPhase.TurnUnitDeployment);

		game.endCurrentTurnPhase();
		assertSame(game.getCurrentTurnPhase(), TurnPhase.TurnAction);
	}

	@Test
	public void testEndCurrentTurnPhase_attack() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnAttack);
		assertEquals(0, game.getCurrentPlayer().getRemainingMoveCount());

		game.endCurrentTurnPhase();
		assertSame(game.getCurrentTurnPhase(), TurnPhase.TurnPostActionMovement);
		assertEquals(7, game.getCurrentPlayer().getRemainingMoveCount());
	}

	@Test
	public void testEndCurrentTurnPhase_conscriptUnitDeployment() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnConscriptUnitDeployment);
		assertEquals(0, game.getCurrentPlayer().getRemainingMoveCount());

		game.endCurrentTurnPhase();
		assertSame(game.getCurrentTurnPhase(), TurnPhase.TurnPostActionMovement);
		assertEquals(7, game.getCurrentPlayer().getRemainingMoveCount());
	}

	@Test
	public void testEndCurrentTurnPhase_turnUnlimitedMovement_EndTurn() {
		Game game = new BasicGame(players, world);
		game.setCurrentTurnPhase(TurnPhase.TurnUnlimitedMovement);
		assertEquals(player1, game.getCurrentPlayer());
		assertEquals(0, player2.getFreeUnitCount());

		game.endCurrentTurnPhase();
		assertEquals(player2, game.getCurrentPlayer());
		assertEquals(4, player2.getFreeUnitCount());
	}

	@Test
	public void testConscript_invalid() {
		Game game = new BasicGame(players, world);

		for (TurnPhase turnPhase : TurnPhase.values()) {
			if (turnPhase != TurnPhase.TurnAction) {
				game.setCurrentTurnPhase(turnPhase);
				try {
					game.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
					fail("conscripting in wrong phase should have failed");
				} catch (IllegalStateException ex) {
					// Expected exception
				}
				assertFalse(game.canConscript());
			}
		}
	}

	@Test
	public void testConscript_0Territories() {
		assertTrue(basicTestGame.canConscript());
		basicTestGame.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);

		assertEquals(3, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(TurnPhase.TurnConscriptUnitDeployment, basicTestGame
				.getCurrentTurnPhase());
	}

	@Test
	public void testConscript_1Territories() {
		for (int index = 0; index < 1; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index).setOwner(player1);
		}
		basicTestGame.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
		assertEquals(3, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(TurnPhase.TurnConscriptUnitDeployment, basicTestGame
				.getCurrentTurnPhase());
	}

	@Test
	public void testConscript_9Territories() {
		for (int index = 0; index < 9; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index).setOwner(player1);
		}
		basicTestGame.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
		assertEquals(3, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(TurnPhase.TurnConscriptUnitDeployment, basicTestGame
				.getCurrentTurnPhase());
	}

	@Test
	public void testConscript_10Territories() {
		for (int index = 0; index < 10; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index).setOwner(player1);
		}
		basicTestGame.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
		assertEquals(4, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(TurnPhase.TurnConscriptUnitDeployment, basicTestGame
				.getCurrentTurnPhase());
	}

	@Test
	public void testConscript_12Territories() {
		for (int index = 0; index < 12; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index).setOwner(player1);
		}
		basicTestGame.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
		assertEquals(4, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(TurnPhase.TurnConscriptUnitDeployment, basicTestGame
				.getCurrentTurnPhase());
	}

	@Test
	public void testConscript_13Territories() {
		for (int index = 0; index < 13; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index).setOwner(player1);
		}
		basicTestGame.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
		assertEquals(5, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(TurnPhase.TurnConscriptUnitDeployment, basicTestGame
				.getCurrentTurnPhase());
	}

	@Test
	public void testGetOwnedTerritoryCount_invalid() {
		try {
			basicTestGame.getOwnedTerritoryCount(null);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}
	}

	@Test
	public void testGetOwnedTerritoryCount() {
		for (int index = 0; index < 5; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index).setOwner(player1);
		}
		for (int index = 0; index < 7; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index + 5).setOwner(player2);
		}
		assertEquals(5, basicTestGame.getOwnedTerritoryCount(player1));
		assertEquals(7, basicTestGame.getOwnedTerritoryCount(player2));
	}

	@Test
	public void testReceiveTurnUnits_invalid() {
		Game game = new BasicGame(players, world);

		for (TurnPhase turnPhase : TurnPhase.values()) {
			if (turnPhase != TurnPhase.TurnReceiveTurnUnits) {
				game.setCurrentTurnPhase(turnPhase);
				try {
					game.receiveTurnUnits();
					fail("receiving in wrong phase should have failed");
				} catch (IllegalStateException ex) {
					// Expected exception
				}
			}
		}
	}

	@Test
	public void testReceiveTurnUnits_0Territories() {
		checkReceiveTurnUnits(0, 1);
	}

	@Test
	public void testReceiveTurnUnits_1Territories() {
		checkReceiveTurnUnits(1, 1);
	}

	@Test
	public void testReceiveTurnUnits_5Territories() {
		checkReceiveTurnUnits(5, 1);
	}

	@Test
	public void testReceiveTurnUnits_6Territories() {
		checkReceiveTurnUnits(6, 2);
	}

	@Test
	public void testReceiveTurnUnits_11Territories() {
		checkReceiveTurnUnits(11, 3);
	}

	@Test
	public void testReceiveTurnUnits_12Territories() {
		checkReceiveTurnUnits(12, 4);
	}

	@Test
	public void testReceiveTurnUnits_13Territories() {
		checkReceiveTurnUnits(13, 9);
	}

	private void checkReceiveTurnUnits(int ownedTerritoryCount,
			int expectedFreeUnitCount) {
		basicTestGame.setCurrentTurnPhase(TurnPhase.TurnReceiveTurnUnits);
		for (int index = 0; index < ownedTerritoryCount; index++) {
			List<Territory> territories = conscriptionWorld.getTerritories();
			territories.get(index).setOwner(player1);
		}
		basicTestGame.receiveTurnUnits();
		assertEquals(expectedFreeUnitCount, player1.getFreeUnitCount());
		assertEquals(0, player2.getFreeUnitCount());
		assertEquals(TurnPhase.TurnExchangeCards, basicTestGame
				.getCurrentTurnPhase());
	}

	@Test
	public void testIsGameOver() {
		Game game = new BasicGame(players, world);
		for (TurnPhase phase : TurnPhase.values()) {
			if (phase != TurnPhase.GameOver) {
				game.setCurrentTurnPhase(phase);
				assertFalse(game.isGameOver());
			}
		}
		game.setCurrentTurnPhase(TurnPhase.GameOver);
		assertTrue(game.isGameOver());
	}

	@Test
	public void testHandOutTerritories() {
		Game clearGame = new BasicGame(players, conscriptionWorld);
		clearGame.handOutTerritories();
		int player1count = 0;
		int player2count = 0;
		for (Territory territory : clearGame.getWorld().getTerritories()) {
			if (territory.getOwner() == player1) {
				player1count++;
			} else if (territory.getOwner() == player2) {
				player2count++;
			} else {
				fail("Just who does own this territory?");
			}
		}
		assertEquals(player1count, 25);
		assertEquals(player2count, 25);
	}

	@Test
	public void testHandOutTerritories_someAlreadyOwned() {
		Game clearGame = new BasicGame(players, conscriptionWorld);

		for (int index = 0; index < 10; index++) {
			clearGame.getWorld().getTerritories().get(index).setOwner(player1);
		}

		clearGame.handOutTerritories();
		int player1count = 0;
		int player2count = 0;
		for (Territory territory : clearGame.getWorld().getTerritories()) {
			if (territory.getOwner() == player1) {
				player1count++;
			} else if (territory.getOwner() == player2) {
				player2count++;
			} else {
				fail("Just who does own this territory?");
			}
		}
		assertEquals(player1count, 30);
		assertEquals(player2count, 20);
	}

	@Test
	public void testSetup4players() {
		Player player3 = new BasicPlayer("player3");
		Player player4 = new BasicPlayer("player4");
		players.add(player3);
		players.add(player4);
		Game clearGame = new BasicGame(players, conscriptionWorld);
		clearGame.setup();
		int player1count = 0;
		int player2count = 0;
		int player3count = 0;
		int player4count = 0;
		for (Territory territory : clearGame.getWorld().getTerritories()) {
			if (territory.getOwner() == player1) {
				player1count++;
			} else if (territory.getOwner() == player2) {
				player2count++;
			} else if (territory.getOwner() == player3) {
				player3count++;
			} else if (territory.getOwner() == player4) {
				player4count++;
			} else {
				fail("Just who does own this territory?");
			}
		}
		assertEquals(player1count, 12);
		assertEquals(player2count, 12);
		assertEquals(player3count, 13);
		assertEquals(player4count, 13);
		for (Player player : players) {
			int armies = player.getFreeUnitCount();
			for (Territory territory : clearGame.getWorld().getTerritories()) {
				if (territory.getOwner() == player) {
					armies += territory.getUnitCount();
				}
			}
			assertEquals(armies, 30);
		}
		assertEquals(clearGame.getCurrentPlayer().getRemainingDeployCount(), 2);
	}

	@Test
	public void testExchangeCards_invalid() {
		Game game = new BasicGame(players, world);
		Player player = game.getCurrentPlayer();
		player.addCardHandCard(new Card(territory1, CardType.Cannon));
		player.addCardHandCard(new Card(territory2, CardType.Cannon));
		player.addCardHandCard(new Card(territory3, CardType.Cannon));
		Card[] cards = player.getCards().toArray(new Card[3]);

		for (TurnPhase turnPhase : TurnPhase.values()) {
			if (turnPhase != TurnPhase.TurnExchangeCards) {
				game.setCurrentTurnPhase(turnPhase);
                try {
                    game.getExchangeCardsValue(cards);
                    fail("exchange cards in wrong phase should have failed");
                } catch (IllegalStateException ex) {
                    // Expected exception
                }
                try {
                    game.exchangeCards(cards);
                    fail("exchange cards in wrong phase should have failed");
                } catch (IllegalStateException ex) {
                    // Expected exception
                }
				assertFalse(game.canExchangeCards());
			}
		}

		game.setCurrentTurnPhase(TurnPhase.TurnExchangeCards);
		assertTrue(game.canExchangeCards());

        try {
            game.canExchangeCards(null);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }
        try {
            game.getExchangeCardsValue(null);
            fail("null should have failed");
        } catch (NullPointerException ex) {
            // Expected exception
        }
		try {
			game.exchangeCards(null);
			fail("null should have failed");
		} catch (NullPointerException ex) {
			// Expected exception
		}

		// 2 cards
		Card[] badCards = new Card[] { cards[0], cards[1] };
		assertFalse(game.canExchangeCards(badCards));
        try {
            game.getExchangeCardsValue(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        try {
            game.exchangeCards(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

		// 4 cards
		badCards = new Card[] { cards[0], cards[1], cards[2],
				new Card(territory4, CardType.Cavalry) };
		assertFalse(game.canExchangeCards(badCards));
        try {
            game.getExchangeCardsValue(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        try {
            game.exchangeCards(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

		// mismatched cards
		badCards = new Card[] { cards[0], cards[1],
				new Card(territory4, CardType.Cavalry) };
		assertFalse(game.canExchangeCards(badCards));
        try {
            game.getExchangeCardsValue(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        try {
            game.exchangeCards(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

		// unowned cards
		badCards = new Card[] { cards[0], cards[1],
				new Card(territory4, CardType.Cannon) };
		assertFalse(game.canExchangeCards(badCards));
        try {
            game.getExchangeCardsValue(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        try {
            game.exchangeCards(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

		// same card
		badCards = new Card[] { cards[0], cards[1],
				new Card(territory1, CardType.Cannon) };
		assertFalse(game.canExchangeCards(badCards));
        try {
            game.getExchangeCardsValue(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        try {
            game.exchangeCards(badCards);
            fail("invalid cards should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

	}

	@Test
	public void testExchangeCards() {
		// 3 Infantry
		checkExchangeCards(CardType.Infantry, CardType.Infantry,
				CardType.Infantry, 4);

		// 3 Cavalry
		checkExchangeCards(CardType.Cavalry, CardType.Cavalry,
				CardType.Cavalry, 6);

		// 3 Cannons
		checkExchangeCards(CardType.Cannon, CardType.Cannon, CardType.Cannon, 8);

		// 1 of each
		checkExchangeCards(CardType.Cannon, CardType.Cavalry,
				CardType.Infantry, 10);

		// 2 Infantry 1 Joker
		checkExchangeCards(CardType.Joker, CardType.Infantry,
				CardType.Infantry, 4);

		// 2 Cavalry 1 Joker
		checkExchangeCards(CardType.Joker, CardType.Cavalry, CardType.Cavalry,
				6);

		// 2 Cannons 1 Joker
		checkExchangeCards(CardType.Joker, CardType.Cannon, CardType.Cannon, 8);

		// 1 Cannon 1 Cavalry 1 Joker
		checkExchangeCards(CardType.Cannon, CardType.Cavalry, CardType.Joker,
				10);

		// 1 Cannon 1 Infantry 1 Joker
		checkExchangeCards(CardType.Cannon, CardType.Joker, CardType.Infantry,
				10);

		// 1 Infantry 1 Cavalry 1 Joker
		checkExchangeCards(CardType.Joker, CardType.Cavalry, CardType.Infantry,
				10);

		// 1 Infantry 2 Joker
		checkExchangeCards(CardType.Joker, CardType.Joker, CardType.Infantry,
				10);

		// 1 Cavalry 2 Joker
		checkExchangeCards(CardType.Joker, CardType.Cavalry, CardType.Joker, 10);

		// 1 Cannon 2 Joker
		checkExchangeCards(CardType.Joker, CardType.Joker, CardType.Cannon, 10);
	}

    private void checkExchangeCards(CardType cardType1, CardType cardType2, CardType cardType3, int expectedUnits) {
        Game game = new BasicGame(players, world);
        game.setCurrentTurnPhase(TurnPhase.TurnExchangeCards);
        Player player = game.getCurrentPlayer();

        Card card1 = new Card(new BasicTerritory("c1"), cardType1);
        Card card2 = new Card(new BasicTerritory("c2"), cardType2);
        Card card3 = new Card(new BasicTerritory("c3"), cardType3);
        Card card4 = new Card(new BasicTerritory("c4"), CardType.Cavalry);
        player.addCardHandCard(card1);
        player.addCardHandCard(card2);
        player.addCardHandCard(card3);
        player.addCardHandCard(card4);
        Card[] cards = new Card[] { card1, card2, card3 };

        assertEquals(0, player.getFreeUnitCount());

        player.setFreeUnitCount(2);

        assertEquals(2, player.getFreeUnitCount());

        assertEquals(expectedUnits, game.getExchangeCardsValue(cards));

        game.exchangeCards(cards);
        assertEquals(expectedUnits + 2, player.getFreeUnitCount());
        assertEquals(1, player.getCardHandCount());
        assertSame(card4, player.getCards().get(0));
        assertSame(TurnPhase.TurnUnitDeployment, game.getCurrentTurnPhase());

        player.getCardHandCard(card4);
        player.setFreeUnitCount(0);

        assertEquals(0, player.getCardHandCount());
        assertEquals(0, player.getFreeUnitCount());
    }

	@Test
	public void testExchangeCards_OwningExchangedTerritory() {
		for (int index = 0; index < 3; index++) {
			Game game = new BasicGame(players, world);
			game.setCurrentTurnPhase(TurnPhase.TurnExchangeCards);
			Player player = game.getCurrentPlayer();

			Card card1 = new Card(territory1, CardType.Infantry);
			Card card2 = new Card(territory2, CardType.Infantry);
			Card card3 = new Card(territory3, CardType.Infantry);
			Card card4 = new Card(territory4, CardType.Cavalry);
			player.addCardHandCard(card1);
			player.addCardHandCard(card2);
			player.addCardHandCard(card3);
			Card[] cards = new Card[] { card1, card2, card3 };

			territory1.setUnitCount(1);
			territory2.setUnitCount(1);
			territory3.setUnitCount(1);
			territory4.setUnitCount(1);

			territory1.setOwner(player2);
			territory2.setOwner(player2);
			territory3.setOwner(player2);
			territory4.setOwner(player2);

			// 3 Infantry, Owns one territory
			if (index == 0) {
				territory1.setOwner(player);
				player.addCardHandCard(card4);
				assertEquals(6, game.getExchangeCardsValue(cards));
				game.exchangeCards(cards);
				assertEquals(3, territory1.getUnitCount());
				assertEquals(1, territory2.getUnitCount());
				assertEquals(1, territory3.getUnitCount());
				assertEquals(1, territory4.getUnitCount());
			}
			// 3 Infantry, Owns two territories
			if (index == 1) {
				territory1.setOwner(player);
				territory2.setOwner(player);
				assertEquals(8, game.getExchangeCardsValue(cards));
				game.exchangeCards(cards);
				assertEquals(3, territory1.getUnitCount());
				assertEquals(3, territory2.getUnitCount());
				assertEquals(1, territory3.getUnitCount());
				assertEquals(1, territory4.getUnitCount());
			}
			// 3 Infantry, Owns all territories
			if (index == 2) {
				territory1.setOwner(player);
				territory2.setOwner(player);
				territory3.setOwner(player);
                assertEquals(10, game.getExchangeCardsValue(cards));
				game.exchangeCards(cards);
				assertEquals(3, territory1.getUnitCount());
				assertEquals(3, territory2.getUnitCount());
				assertEquals(3, territory3.getUnitCount());
				assertEquals(1, territory4.getUnitCount());
			}
		}
	}

	public static void assertGameEquals(Game expected, Game actual) {
		assertEquals(expected.cardDeck, actual.cardDeck);
		assertEquals(expected.getPlayers(), actual.getPlayers());
		assertEquals(expected.getWorld(), actual.getWorld());
		assertEquals(expected.currentPlayerIndex, actual.currentPlayerIndex);
		assertEquals(expected.currentPlayerVictories,
				actual.currentPlayerVictories);
		assertEquals(expected.getCurrentTurnPhase(), actual
				.getCurrentTurnPhase());
	}

	@Test
	public void testMaxUnitsToMove() {
		Game game = new BasicGame(players, world);
		Territory source = null;
		Territory target = null;
		territory1.setUnitCount(7);
		territory2.setUnitCount(7);
		territory3.setUnitCount(7);

		try {
			game.maxUnitsToMove(source, target);
			fail("should have failed, null parameters");
		} catch (NullPointerException ex) {
		    // Expected exception
		}

		source = territory1;
		target = source;

		assertEquals(0, game.maxUnitsToMove(source, target));

		target = territory2;
		target.setOwner(player1);
		source.setOwner(player2);

		assertEquals(0, game.maxUnitsToMove(source, target));

		territory1.setOwner(player2);
		territory2.setOwner(player1);
		territory3.setOwner(player1);

		assertEquals(0, game.maxUnitsToMove(territory2, territory3));

		territory1.setOwner(player1);
		game.setCurrentTurnPhase(TurnPhase.TurnUnlimitedMovement);
		territory1.addNeighbour(territory2);
		territory2.addNeighbour(territory3);

		assertEquals(7, territory1.getUnitCount());
		assertTrue(game.getCurrentTurnPhase() == TurnPhase.TurnUnlimitedMovement);
		assertTrue(world.getPathwayCost(territory1, territory2) >= 1);

		assertEquals(territory1.getUnitCount() - 1, game.maxUnitsToMove(
				territory1, territory2));

		game.setCurrentTurnPhase(TurnPhase.TurnAction);
		assertEquals(territory1.getUnitCount() - 1, game.maxUnitsToMove(
				territory1, territory2));

	}

	@Test
	public void testMaxUnitsToAttack() {
		List<Territory> sources = null;
		Territory target = null;

		territory1.setUnitCount(7);
		territory2.setUnitCount(7);
		territory3.setUnitCount(7);

		try {
			basicTestGame.maxUnitsToAttack(sources, target);
			fail("should have failed, null parameters");
		} catch (NullPointerException ex) {
		    // Expected exception
		}
		
		sources = new ArrayList<Territory>();
		sources.add(territory1);
		target = territory2;
		target.setOwner(player1);
		List<Integer> sourceUnits = new ArrayList<Integer>();
		
		for(Territory source: sources) {
			source.setOwner(player2);
			sourceUnits.add(source.getUnitCount() - 1);
		}
		
		assertEquals(sourceUnits, basicTestGame.maxUnitsToAttack(
				sources, target));
	}
}
