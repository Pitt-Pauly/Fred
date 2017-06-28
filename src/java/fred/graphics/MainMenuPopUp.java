package fred.graphics;

import static fred.graphics.ImagePaths.MAINMENU_CREDITS;
import static fred.graphics.ImagePaths.MAINMENU_CREDITS_PRESSED;
import static fred.graphics.ImagePaths.MAINMENU_EXIT;
import static fred.graphics.ImagePaths.MAINMENU_EXIT_PRESSED;
import static fred.graphics.ImagePaths.MAINMENU_LOAD;
import static fred.graphics.ImagePaths.MAINMENU_LOAD_PRESSED;
import static fred.graphics.ImagePaths.MAINMENU_MENU;
import static fred.graphics.ImagePaths.MAINMENU_NEWGAME;
import static fred.graphics.ImagePaths.MAINMENU_NEWGAME_PRESSED;
import static fred.graphics.ImagePaths.MAINMENU_SETTINGS;
import static fred.graphics.ImagePaths.MAINMENU_SETTINGS_PRESSED;
import static fred.graphics.UIUtils.createButton;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.handleException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuPopUp extends PopUp {

    static final int BUTTON_NEWGAME = 0;
    static final int BUTTON_LOADGAME = 1;
    static final int BUTTON_CREDITS = 2;
    static final int BUTTON_SETTINGS = 3;
    static final int BUTTON_EXIT = 4;

    static final String[][] buttonImageFilepaths = { 
            { MAINMENU_NEWGAME, MAINMENU_NEWGAME_PRESSED, MAINMENU_NEWGAME },
            { MAINMENU_LOAD, MAINMENU_LOAD_PRESSED, MAINMENU_LOAD }, 
            { MAINMENU_CREDITS, MAINMENU_CREDITS_PRESSED, MAINMENU_CREDITS },
            { MAINMENU_SETTINGS, MAINMENU_SETTINGS_PRESSED, MAINMENU_SETTINGS }, 
            { MAINMENU_EXIT, MAINMENU_EXIT_PRESSED, MAINMENU_EXIT } };

    private int buttonX = 0;
    private int buttonSpaceY = 0;

    Button buttonNewGame;
    Button buttonExit;
    Button buttonLoadGame;
    Button buttonCredits;
    Button buttonSettings;

    private SettingsPopUp settingsPopUp;
    private LoadGamePopUp loadGamePopUp;

    GameContainer gameContainer;
    StateBasedGame stateGame;

    public MainMenuPopUp(GameContainer container, StateBasedGame game, float scalar) throws SlickException {
        super(container, MAINMENU_MENU, scalar, false);
        gameContainer = container;
        stateGame = game;
        buttonX = getX() + getWidth() / 2;
        buttonSpaceY = getHeight() / (BUTTON_EXIT + 1) - 5;

        addComponent(getButtonNewGame());
        addComponent(getButtonLoadGame());
        addComponent(getButtonCredits());
        addComponent(getButtonSettings());
        addComponent(getButtonExit());
        getSettingsPopUp();
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput);
        if (!acceptingInput) {
            getSettingsPopUp().setAcceptingInput(false);
            getLoadGamePopUp().setAcceptingInput(false);
        }
    }

    private Button getButtonNewGame() throws SlickException {
        if (buttonNewGame == null) {
            buttonNewGame = createButton(gameContainer, 
                    createImage(buttonImageFilepaths[BUTTON_NEWGAME][INACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_NEWGAME][ACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_NEWGAME][PRESSED], scalar), buttonX, getY()
                    + getHeight() / 9 + 5);
            buttonNewGame.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    stateGame.enterState(GameStatePlayerMenu.STATE_ID);
                }
            });
        }
        return buttonNewGame;
    }

    private Button getButtonLoadGame() throws SlickException {
        if (buttonLoadGame == null) {
            buttonLoadGame = createButton(gameContainer, 
                    createImage(buttonImageFilepaths[BUTTON_LOADGAME][INACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_LOADGAME][ACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_LOADGAME][PRESSED], scalar), buttonX,
                    getButtonNewGame().getY() + (BUTTON_LOADGAME * buttonSpaceY));
            buttonLoadGame.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    MainMenuPopUp.this.setAcceptingInput(false);
                    getLoadGamePopUp().setAcceptingInput(true);
                }
            });
        }
        return buttonLoadGame;
    }

    private Button getButtonCredits() throws SlickException {
        if (buttonCredits == null) {
            buttonCredits = createButton(gameContainer, 
                    createImage(buttonImageFilepaths[BUTTON_CREDITS][INACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_CREDITS][ACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_CREDITS][PRESSED], scalar), buttonX,
                    getButtonNewGame().getY() + (BUTTON_CREDITS * buttonSpaceY));
            buttonCredits.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    // TODO show credits screen
                }
            });
        }
        return buttonCredits;
    }

    private Button getButtonSettings() throws SlickException {
        if (buttonSettings == null) {
            buttonSettings = createButton(gameContainer, 
                    createImage(buttonImageFilepaths[BUTTON_SETTINGS][INACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_SETTINGS][ACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_SETTINGS][PRESSED], scalar), buttonX,
                    getButtonNewGame().getY() + (BUTTON_SETTINGS * buttonSpaceY));
            buttonSettings.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    MainMenuPopUp.this.setAcceptingInput(false);
                    getSettingsPopUp().setAcceptingInput(true);
                }
            });
        }
        return buttonSettings;
    }

    SettingsPopUp getSettingsPopUp() {
        if (settingsPopUp == null) {
            try {
                settingsPopUp = new SettingsPopUp(gameContainer, (GameUI) stateGame, scalar, false);
                settingsPopUp.setAcceptingInput(false);
                settingsPopUp.addPopUpListener(new PopUpListener() {
                    public void popUpClosed(Result result) {
                        MainMenuPopUp.this.setAcceptingInput(true);
                    }
                });
            }
            catch (SlickException e) {
                handleException(e);
            }
        }
        return settingsPopUp;
    }

    LoadGamePopUp getLoadGamePopUp() {
        if (loadGamePopUp == null) {
            try {
                loadGamePopUp = new LoadGamePopUp(gameContainer, (GameUI) stateGame, scalar, false);
                loadGamePopUp.setAcceptingInput(false);
                loadGamePopUp.addPopUpListener(new PopUpListener() {
                    public void popUpClosed(Result result) {
                        if (result == Result.Cancel) {
                            MainMenuPopUp.this.setAcceptingInput(true);
                        }
                    }
                });
            }
            catch (SlickException e) {
                handleException(e);
            }
        }
        return loadGamePopUp;
    }

    private Button getButtonExit() throws SlickException {
        if (buttonExit == null) {
            buttonExit = createButton(gameContainer, 
                    createImage(buttonImageFilepaths[BUTTON_EXIT][INACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_EXIT][ACTIVE], scalar), 
                    createImage(buttonImageFilepaths[BUTTON_EXIT][PRESSED], scalar), buttonX,
                    getButtonNewGame().getY() + (BUTTON_EXIT * buttonSpaceY));
            buttonExit.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    gameContainer.exit();
                }
            });
        }
        return buttonExit;
    }

    @Override
    @SuppressWarnings("hiding")
    public void render(GUIContext container, Graphics g) throws SlickException {
        if (getSettingsPopUp().isAcceptingInput()) {
            getSettingsPopUp().render(container, g);
        }
        else if (getLoadGamePopUp().isAcceptingInput()) {
            getLoadGamePopUp().render(container, g);
        }
        else {
            super.render(container, g);
        
            getButtonNewGame().render(container, g);
            getButtonLoadGame().render(container, g);
            getButtonCredits().render(container, g);
            getButtonSettings().render(container, g);
            getButtonExit().render(container, g);
        }
    }

}
