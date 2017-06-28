package fred.graphics;

import static fred.graphics.ImagePaths.GENERAL_ARROW;
import static fred.graphics.ImagePaths.PLAYERSETUP_MENU;
import static fred.graphics.ImagePaths.PLAYERSETUP_STARTGAME;
import static fred.graphics.ImagePaths.PLAYERSETUP_STARTGAME_PRESSED;
import static fred.graphics.UIUtils.createButton;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.handleException;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import fred.model.Player;

public class PlayerSetupPopUp extends PopUp {

    private static final int BUTTON_STARTGAME = 0;
    private static final int BUTTON_BACK = 1;

    private final int buttonY;
    private final int panelX;

    private static PlayerPanel[] playerPanels = null;

    private Button buttonStartGame;
    private Button buttonBack;

    private GameContainer gameContainer;
    StateBasedGame stateGame;

    private String[][] buttonImageFilenames = {
            { PLAYERSETUP_STARTGAME, PLAYERSETUP_STARTGAME_PRESSED, PLAYERSETUP_STARTGAME, },
            { GENERAL_ARROW, GENERAL_ARROW, GENERAL_ARROW, },
    }; 

    public PlayerSetupPopUp(GameContainer container, StateBasedGame game, float scalar) throws SlickException{
        super(container, PLAYERSETUP_MENU, scalar, false);
        gameContainer = container;
        stateGame = game;
        buttonY = getY() + getHeight() - getHeight()/13;
        panelX = getX() + (getWidth()/2);
        
        addComponent(getButtonBack());
        addComponent(getButtonStartGame());
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput);
        try {
            for (PlayerPanel playerPanel : getPlayerPanels()) {
                playerPanel.setAcceptingInput(acceptingInput);
            }
        }
        catch (SlickException e) {
            handleException(e);
        }
    }

    private Button getButtonStartGame() throws SlickException {
        if (buttonStartGame == null) {
            buttonStartGame = createButton(gameContainer,
                    createImage(buttonImageFilenames[BUTTON_STARTGAME][INACTIVE], scalar),
                    createImage(buttonImageFilenames[BUTTON_STARTGAME][ACTIVE], scalar),
                    createImage(buttonImageFilenames[BUTTON_STARTGAME][PRESSED], scalar), getCenterX()+getWidth()/5, buttonY);
           buttonStartGame.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                   try {
                        setPlayers();
                    }
                    catch (SlickException e) {
                        handleException(e);
                    }
                    stateGame.enterState(GameStateInitialiseGame.STATE_ID);
                }});
        }
        return buttonStartGame;
    }

    private Button getButtonBack() throws SlickException {
        if (buttonBack == null) {
            buttonBack = createButton(gameContainer,
                    createImage(buttonImageFilenames[BUTTON_BACK][INACTIVE], scalar),
                    createImage(buttonImageFilenames[BUTTON_BACK][ACTIVE], scalar),
                    createImage(buttonImageFilenames[BUTTON_BACK][PRESSED], scalar), getCenterX() - getWidth()/4 , buttonY);
            buttonBack.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                	((GameUI) stateGame).saveName = "save.sav";
                    stateGame.enterState(GameStateMainMenu.STATE_ID);
                }});
        }
        return buttonBack;
    }

    void setPlayers() throws SlickException {
        List<Player> players = new ArrayList<Player>();
        for (PlayerPanel playerPanel: getPlayerPanels()) {
        	if (playerPanel != null && playerPanel.isPlayerPlaying()) {
        		players.add(playerPanel.getPlayer());
        	}
        }
        ((GameUI) stateGame).saveName = null;
        ((GameUI) stateGame).players = (players.size() > 0) ? players : null;
    }

     private PlayerPanel[] getPlayerPanels() throws SlickException {
    	if (playerPanels == null) {
            playerPanels = new PlayerPanel[6];
            int SpacingY = (getHeight()/(playerPanels.length+2));
            int panelY = getY() + getHeight()/11 - 5;
            int panelHeight = getHeight()/playerPanels.length+1;
            int panelWidth = getWidth()/2;
            for (int i = 0; i < playerPanels.length; i++){
                    panelY = panelY + SpacingY;
                    if (playerPanels[i] == null){
                            playerPanels[i] = new PlayerPanel(gameContainer, panelX, panelY, panelWidth, panelHeight, scalar, i);
                    }

            }//for
        }
        return playerPanels;
    }

    @Override
    @SuppressWarnings("hiding") 
    public void render(GUIContext container, Graphics g) throws SlickException {
        if (!isAcceptingInput()) {
            return;
        }
    
        super.render(container, g);
    
        getButtonStartGame().render(container, g);
        getButtonBack().render(container, g);

        getPlayerPanels();
        for (int players = 0; players < playerPanels.length; players++) {
            playerPanels[players].render(container, g);
        }
    }

    private int getCenterX() {
        return (getX() + getWidth()/2);
    }

}
