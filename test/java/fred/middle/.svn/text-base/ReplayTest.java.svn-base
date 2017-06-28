package fred.middle;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import fred.AI.TestConquerWorld;
import fred.AI.AI;
import fred.model.Game;
import fred.model.Territory;

public class ReplayTest {

    public Wrapper wrapper;

    @Test
    public void systemTest() {
        TestConquerWorld tcw = new TestConquerWorld();
        tcw.setupWorldGame();
        wrapper = new Wrapper(tcw.game, tcw.game);
        tcw.ai = new AI(wrapper, tcw.game);
        // tcw.setupLog();
        // tcw.logPosition();
        tcw.doStuff();
        Game replayer = wrapper.replay.initialPosition();
        // tcw.game = replayer;
        // tcw.logPosition();
        // tcw.log.close();

        Action next = wrapper.replay.nextAction();
        while (next != null) {
            // System.out.println(next.toString());
            next.perform(replayer, replayer);
            next = wrapper.replay.nextAction();
        }
        testGameEquality(replayer, wrapper.refGame);
        testGameEquality(wrapper.replay.lastPosition(), wrapper.refGame);
    }

    private void testGameEquality(Game game1, Game game2) {
        for (Territory territory : game1.getWorld().getTerritories()) {
            Territory equiv = game2.getWorld().getTerritoryByName(territory.getName());
            assertNotNull(equiv);
            assertEquals(territory.getOwner().getName(), equiv.getOwner().getName());
            assertEquals(territory.getUnitCount(), equiv.getUnitCount());
        }
        // TODO: test player state including cards
    }

}
