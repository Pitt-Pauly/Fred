package fred.graphics;

import static fred.graphics.UIUtils.createButton;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.ImagePaths.*;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import fred.middle.Middle;

public class MiniReplay extends AbstractComponent {
	
	private final int x, y, width, height;
	protected Middle middle;
	private WorldUI mini;
	private Button buttonNewGame;
    private Button buttonOurExit;
	

	protected MiniReplay(GUIContext container, int x, int y, int width, Middle game, WorldUI ui) throws SlickException {
		super(container);
		this.x = x;
		this.y = y;
		this.width = width;		
		this.middle = new Middle(game);
		this.mini = new WorldUI(ui, x, y, width);
		this.height = mini.getHeight() + 20;
		mini.setGameInfo(middle);
	}
	
	private Button getButtonEndNewGame() throws SlickException {
        if (buttonNewGame == null) {
            buttonNewGame = createButton(container, createImage(GAMEOVER_REPLAY_PLAY), x + width - 50, y + height);
            buttonNewGame.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    middle.replayForwardTurn();
                }});
        }
        return buttonNewGame;
    }

    private Button getButtonExit() throws SlickException {
        if (buttonOurExit == null) {
            buttonOurExit = createButton(container, createImage(GAMEOVER_REPLAY_STOP), x + width - 20, y + height);
            buttonOurExit.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    middle.startReplayAtStart();
                }});
        }
        return buttonOurExit;
    }

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		mini.render(context, g);
		getButtonEndNewGame().render(context, g);
        getButtonExit().render(context, g);
	}

	@Override
	public void setLocation(int x, int y) {

	}
	
	@Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput);
        try {
        	getButtonEndNewGame().setAcceptingInput(acceptingInput);
        	getButtonExit().setAcceptingInput(acceptingInput);
        } catch (SlickException ex) {
        }
    }
}
