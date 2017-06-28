package fred.graphics;

import static fred.graphics.GameUI.INFOPANE_HEIGHT;
import static fred.graphics.GameUI.SCREEN_HEIGHT;
import static fred.graphics.GameUI.SCREEN_WIDTH;
import static fred.graphics.UIUtils.createButton;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameStateReplay extends BasicGameState {

	
	public static final int STATE_ID = 27;
	GameContainer gameContainer;
	GameUI stateGame;
	private AbstractComponent infoPane;
	Button backAction;
	Button forwardAction;
	Button backTurn;
	Button forwardTurn;
	Button backToStart;
	Button toEnd;
    PlayerUI playerUI;
    
    private static final int BUTTONS_CENTER_X = 650;
    private static final int BUTTONS_CENTER_Y = SCREEN_HEIGHT - (INFOPANE_HEIGHT / 2);
    static final int X_COLUMNS[] = {
        BUTTONS_CENTER_X - 95, 
        BUTTONS_CENTER_X, 
        BUTTONS_CENTER_X + 95
        };
    static final int Y_ROWS[] = {
        BUTTONS_CENTER_Y - 35,
        BUTTONS_CENTER_Y, 
        BUTTONS_CENTER_Y + 35, 
        };
    
	@Override
	public int getID() {
		return STATE_ID;
	}

	public void init(GameContainer container, StateBasedGame stategame) throws SlickException {
        this.gameContainer = container;
        this.stateGame = (GameUI) stategame;
    }
	
	@Override
	public void enter(GameContainer container, StateBasedGame stategame) throws SlickException {
        stateGame.game.startReplayAtEnd();
        playerUI = new PlayerUI(gameContainer, stateGame.game, 10, SCREEN_HEIGHT - 100);
    }


	public void render(GameContainer container, StateBasedGame stagegame, Graphics g) throws SlickException {
        stateGame.worldUI.render(container, g);
        getInfoPane().render(container, g);
    }
	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// Nothing

	}
	
	PlayerUI getPlayerUI() {
        /*if (playerUI == null) {
            playerUI = new PlayerUI(gameContainer, stateGame.game, 10, SCREEN_HEIGHT - 100);
        }*/
        return playerUI;
    }
	
	private AbstractComponent getInfoPane() {
        if (infoPane == null) {
            infoPane = new AbstractComponent(gameContainer) {

                @Override
                public int getHeight() {
                    return INFOPANE_HEIGHT;
                }

                @Override
                public int getWidth() {
                    return SCREEN_WIDTH;
                }

                @Override
                public int getX() {
                    return 0;
                }

                @Override
                public int getY() {
                    return SCREEN_HEIGHT - INFOPANE_HEIGHT;
                }

                @Override
                public void render(GUIContext context, Graphics g) throws SlickException {
                    g.setColor(Color.darkGray);
                    g.fillRect(0, SCREEN_HEIGHT - INFOPANE_HEIGHT, SCREEN_WIDTH, INFOPANE_HEIGHT);
                    getPlayerUI().render(container, g);
                    getButtonBackAction().render(context, g);
                    getButtonForwardAction().render(context, g);
                    getButtonBackTurn().render(context, g);
                    getButtonForwardTurn().render(context, g);
                    getButtonBackToStart().render(context, g);
                    getButtonToEnd().render(context, g);
                    g.setColor(Color.white);
                    g.drawString("Action", X_COLUMNS[1] - 25, Y_ROWS[0] - 15);
                    g.drawString("Turn " + stateGame.game.getReplay().replayTurnNo(), X_COLUMNS[1] - 25, Y_ROWS[1] - 15);
                    g.drawString("Game", X_COLUMNS[1] - 15, Y_ROWS[2] - 15);
                }

                @Override
                public void setLocation(int x, int y) {
                    // Do nothing
                }
            };

        }
        return infoPane;
    }

    Button getButtonBackAction() throws SlickException {
        if (backAction == null) {
            backAction = createButton(gameContainer, new Image("images/button-attack-active.png").getScaledCopy(90, 30), X_COLUMNS[0], Y_ROWS[0]);
            backAction.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    stateGame.game.replayBackAction();
                }
            });
        }
        return backAction;
    }

    Button getButtonForwardAction() throws SlickException {
        if (forwardAction == null) {
            forwardAction = createButton(gameContainer, new Image("images/button-conscription-active.png").getScaledCopy(90, 30), X_COLUMNS[2], Y_ROWS[0]);
            forwardAction.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    stateGame.game.replayForwardAction();
                }
            });
        }
        return forwardAction;
    }

    Button getButtonBackTurn() throws SlickException {
        if (backTurn == null) {
            backTurn = createButton(gameContainer, new Image("images/button-conscription-active.png").getScaledCopy(90, 30), X_COLUMNS[0], Y_ROWS[1]);
            backTurn.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    stateGame.game.replayBackTurn();
                }
            });
        }
        return backTurn;
    }

    Button getButtonForwardTurn() throws SlickException {
        if (forwardTurn == null) {
            forwardTurn = createButton(gameContainer, new Image("images/button-conscription-active.png").getScaledCopy(90, 30), X_COLUMNS[2], Y_ROWS[1]);
            forwardTurn.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    stateGame.game.replayForwardTurn();
                }
            });
        }
        return forwardTurn;
    }

    Button getButtonBackToStart() throws SlickException {
        if (backToStart == null) {
            backToStart = createButton(gameContainer, new Image("images/button-conscription-active.png").getScaledCopy(90, 30), X_COLUMNS[0], Y_ROWS[2]);
            backToStart.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    stateGame.game.startReplayAtStart();
                }
            });
        }
        return backToStart;
    }

    Button getButtonToEnd() throws SlickException {
        if (toEnd == null) {
            toEnd = createButton(gameContainer, new Image("images/button-conscription-active.png").getScaledCopy(90, 30), X_COLUMNS[2], Y_ROWS[2]);
            toEnd.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    stateGame.game.startReplayAtEnd();
                }
            });
        }
        return toEnd;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            stateGame.game.wakeUp();
            stateGame.enterState(GameStateMainGame.STATE_ID);
        }
    }
}
