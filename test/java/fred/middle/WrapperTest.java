package fred.middle;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import fred.model.GameDoer;
import fred.model.BasicGameTest;
import fred.model.Game;
import fred.model.BasicGame;
import fred.model.AttackRecord;
import fred.model.Territory;

import org.junit.Before;
import org.junit.Test;
import static org.easymock.classextension.EasyMock.*;


public class WrapperTest {

	GameDoer mockGameInterface = createMock(GameDoer.class);
	Game mockGame = createMock(Game.class);
	
	Object[] mocks = {mockGameInterface, mockGame};
	
	BasicGameTest cheat;
	Game game;
	@Before
	public void setUp() {
		cheat = new BasicGameTest();
		cheat.setUp();
		game = new BasicGame(cheat.players, cheat.world);
	}
		
	@Test
	public void testDeployUnits() {
		Wrapper wrapper = new Wrapper(mockGameInterface, game);
		mockGameInterface.distributeFreeUnits(cheat.territory1, 1);
		
		replay(mocks);
		wrapper.distributeFreeUnits(cheat.territory1, 1);
		verify(mocks);
	}
	
	@Test
	public void testDeployUnitsAction() {
		Recorder recorder = new Recorder(mockGameInterface, game);
		Wrapper wrapper = new Wrapper(recorder, game);
		wrapper.distributeFreeUnits(cheat.territory1, 1);
		DeployAction deploy = (DeployAction) recorder.getLastAction();
		assertTrue(recorder.getLastAction() instanceof DeployAction);
		assertEquals(deploy.getHereName(), cheat.territory1.getName());
		assertEquals(deploy.getNumUnits(), 1);		
		
	}
	
	@Test
	public void testMoveUnits() {
		Wrapper wrapper = new Wrapper(mockGameInterface, game);
		mockGameInterface.moveUnits(cheat.territory1, cheat.territory2, 5);
		
		replay(mocks);
		wrapper.moveUnits(cheat.territory1, cheat.territory2, 5);
		verify(mocks);
	}
	
	@Test
	public void testMoveUnitsAction() {
		Recorder recorder = new Recorder(mockGameInterface, game);
		Wrapper wrapper = new Wrapper(recorder, game);
		wrapper.moveUnits(cheat.territory1, cheat.territory2, 5);
		MoveAction action = (MoveAction) recorder.getLastAction();
		assertTrue(recorder.getLastAction() instanceof MoveAction);
		assertEquals(action.getHereName(), cheat.territory1.getName());
		assertEquals(action.getThereName(), cheat.territory2.getName());
		assertEquals(action.getNumUnits(), 5);		
	}
	
	
	
	@Test
	public void testAttackUnits() {
		Wrapper wrapper = new Wrapper(mockGameInterface, game);
		ArrayList<Territory> cheatAttackTerritoryList = new ArrayList<Territory>();
		cheatAttackTerritoryList.add(cheat.territory1);
		ArrayList<Integer> cheatAttackUnitList = new ArrayList<Integer>();
		cheatAttackUnitList.add(10000);
		expect(mockGameInterface.attack(cheatAttackTerritoryList, cheat.territory3, cheatAttackUnitList, false, false)).andReturn(null);
		
		replay(mocks);
		wrapper.attack(cheatAttackTerritoryList, cheat.territory3, cheatAttackUnitList, false, false);
		verify(mocks);
	}
	
	@Test
	public void testAttackUnitsAction() {
		Recorder recorder = new Recorder(mockGameInterface, game);
		Wrapper wrapper = new Wrapper(recorder, game);
		ArrayList<Territory> cheatAttackTerritoryList = new ArrayList<Territory>();
		cheatAttackTerritoryList.add(cheat.territory1);
		ArrayList<Integer> cheatAttackUnitList = new ArrayList<Integer>();
		cheatAttackUnitList.add(10000);
		List<String> cheatAttackTerritoryNameList = new ArrayList<String>();
		cheatAttackTerritoryNameList.add(cheat.territory1.getName());
		AttackRecord record = wrapper.attack(cheatAttackTerritoryList, cheat.territory3, cheatAttackUnitList, false, false);
		AttackAction action = (AttackAction) recorder.getLastAction();
		assertTrue(recorder.getLastAction() instanceof AttackAction);
		assertEquals(action.getHereName(), cheatAttackTerritoryNameList);
		assertEquals(action.getThereName(), cheat.territory3.getName());
		assertEquals(action.getNumUnits(), cheatAttackUnitList);
		assertSame(action.getRecord(), record);		
	}
	
	@Test
	public void testEndTurnPhase() {
		Wrapper wrapper = new Wrapper(mockGameInterface, game);
		mockGameInterface.endCurrentTurnPhase();
		replay(mocks);
		wrapper.endCurrentTurnPhase();
		verify(mocks);
	}
}
