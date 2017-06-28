package fred.graphics;

import static fred.graphics.ImagePaths.GENERAL_ARROW;
import static fred.graphics.ImagePaths.SETTINGS_MENU;
import static fred.graphics.ImagePaths.SETTINGS_SOUND_OFF;
import static fred.graphics.ImagePaths.SETTINGS_SOUND_ON;
import static fred.graphics.UIUtils.createButton;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.createToggleButton;
import static fred.graphics.UIUtils.handleException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import fred.graphics.PopUpListener.Result;

public class SettingsPopUp extends PopUp {

    Button buttonBack;
    ToggleButton buttonMute;
    final GameUI stateGame;

    public SettingsPopUp(GameContainer gc, GameUI stateGame, float scalar, boolean inGame) throws SlickException {
        super(gc, SETTINGS_MENU, scalar, inGame);
        this.stateGame = stateGame;

        addComponent(getButtonBack());
        addComponent(getButtonMute());
    }

    private ToggleButton getButtonMute() throws SlickException {
        if (buttonMute == null) {
            Image soundOnImage = createImage(SETTINGS_SOUND_ON, scalar);
            Image soundOffImage = createImage(SETTINGS_SOUND_OFF, scalar);
            buttonMute = createToggleButton(container, soundOffImage, soundOnImage,  getCenterX(), getCenterY());
            buttonMute.setValue(stateGame.getMusic().playing());
            buttonMute.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    try {
                        if (buttonMute.getValue()) {
                            stateGame.getMusic().play();
                        }
                        else {
                            stateGame.getMusic().stop();
                        }
                    }
                    catch (SlickException e) {
                        handleException(e);
                    }
                }
            });
        }
        return buttonMute;
    }

    private Button getButtonBack() throws SlickException {
        if (buttonBack == null) {
            Image activeImage = createImage(GENERAL_ARROW, scalar);
            buttonBack = createButton(container, activeImage, 
                    getCenterX() - getWidth() / 4, getY() + getHeight() - activeImage.getHeight() / 2 - 20);
            buttonBack.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    SettingsPopUp.this.close(Result.Ok);
                }
            });
        }
        return buttonBack;
    }

    private int getCenterX() {
        return getX() + getWidth() / 2;
    }

    private int getCenterY() {
        return getY() + getHeight() / 2;
    }

    @Override
    @SuppressWarnings("hiding")
    public void render(GUIContext container, Graphics g) throws SlickException {
        if (!isAcceptingInput()) {
            return;
        }
    
        super.render(container, g);
    
        getButtonBack().render(container, g);
        getButtonMute().render(container, g);
    }

}
