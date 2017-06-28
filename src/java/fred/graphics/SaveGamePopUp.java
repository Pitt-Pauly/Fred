package fred.graphics;

import static fred.graphics.ImagePaths.GENERAL_ACCEPT;
import static fred.graphics.ImagePaths.GENERAL_ACCEPT_PRESSED;
import static fred.graphics.ImagePaths.GENERAL_ARROW;
import static fred.graphics.ImagePaths.SAVEGAME_MENU;
import static fred.graphics.ImagePaths.SAVEGAME_TEXTBOX;
import static fred.graphics.UIUtils.createButton;
import static fred.graphics.UIUtils.createFont;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.createTextField;
import static fred.graphics.UIUtils.drawCenteredString;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

import fred.datahandling.SaveLoader;
import fred.graphics.PopUpListener.Result;

public class SaveGamePopUp extends PopUp {

    private static final UnicodeFont TEXTFIELD_FONT = createFont("Serif", true, false, 14);

    protected static final String WARNING_INVALID_CHARACTERS = "Invalid characters in filename removed";
    protected static final String WARNING_INVALID_FILENAME = "Invalid filename";
    protected static final String WARNING_CANNOT_SAVE_FILE = "Cannot save file";

    Button buttonBack;
    Button buttonAccept;
    final GameUI stateGame;
    private final Image textBoxImg;
    private TextField textFieldName;
    boolean showWarning = false;
    String warningText = "";

    public SaveGamePopUp(GameContainer gc, GameUI stateGame, float scalar) throws SlickException {
        super(gc, SAVEGAME_MENU, scalar, true);
        this.stateGame = stateGame;

        addComponent(getButtonBack());
        addComponent(getButtonAccept());
        addComponent(getTextFieldName());
        textBoxImg = createImage(SAVEGAME_TEXTBOX);
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput);
        if (acceptingInput) {
            showWarning = false;
            resetTextFieldName();
            getTextFieldName().setFocus(true);
        }
    }

    void resetTextFieldName() {
        String filename = stateGame.saveName;
        if (filename == null) {
            filename = "save";
        }
        else {
            filename = filename.substring(0, filename.length() - 4);
        }
        getTextFieldName().setText(filename);
    }

    TextField getTextFieldName() {
        if (textFieldName == null) {
            textFieldName = createTextField(container, TEXTFIELD_FONT, getX() + 170, getY() + 120, 120, 20);
            textFieldName.setBackgroundColor(new Color(0, 0, 0, 1));
            textFieldName.setBorderColor(new Color(0, 0, 0, 1));
            textFieldName.setTextColor(Color.black);
        }
        return textFieldName;
    }

    private Button getButtonBack() throws SlickException {
        if (buttonBack == null) {
            Image activeImage = createImage(GENERAL_ARROW, scalar);
            buttonBack = createButton(container, activeImage, getCenterX() - getWidth() / 4, getY() + getHeight() - activeImage.getHeight() / 2 - 20);
            buttonBack.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    SaveGamePopUp.this.close(Result.Cancel);
                }
            });
        }
        return buttonBack;
    }

    private Button getButtonAccept() throws SlickException {
        if (buttonAccept == null) {
            Image activeImage = createImage(GENERAL_ACCEPT, scalar);
            buttonAccept = createButton(container, activeImage, createImage(GENERAL_ACCEPT_PRESSED, scalar), getX() + getWidth() - 120, getY() + 120);
            buttonAccept.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    showWarning = false;
                    String filename = getTextFieldName().getText();
                    String cleanFilename = SaveLoader.getValidFilename(filename);
                    if (cleanFilename != null && cleanFilename.equals(filename)) {
                        SaveLoader saver = new SaveLoader();
                        try {
                            saver.save(cleanFilename + ".sav", stateGame.game.getReplay());
                            stateGame.saveName = cleanFilename + ".sav";
                            SaveGamePopUp.this.close(Result.Ok);
                        }
                        catch (IllegalArgumentException ex) {
                            warningText = WARNING_CANNOT_SAVE_FILE;
                            showWarning = true;
                            return;
                        }
                    }
                    else if (cleanFilename != null) {
                        warningText = WARNING_INVALID_CHARACTERS;
                        showWarning = true;
                        getTextFieldName().setText(cleanFilename);
                    }
                    else {
                        warningText = WARNING_INVALID_FILENAME;
                        showWarning = true;
                        resetTextFieldName();
                    }
                }
            });
        }
        return buttonAccept;
    }

    private int getCenterX() {
        return getX() + getWidth() / 2;
    }

    @Override
    @SuppressWarnings("hiding")
    public void render(GUIContext container, Graphics g) throws SlickException {
        if (!isAcceptingInput()) {
            return;
        }
    
        super.render(container, g);
        
        textBoxImg.draw(getTextFieldName().getX() + getTextFieldName().getWidth() / 2 - (textBoxImg.getWidth() * scalar) / 2, getTextFieldName().getY()
                + getTextFieldName().getHeight() / 2 - (textBoxImg.getHeight() * scalar) / 2 - 4, scalar);

        getButtonBack().render(container, g);
        getButtonAccept().render(container, g);
        getTextFieldName().render(container, g);
        
        if (showWarning) {
            g.setColor(Color.red);
            drawCenteredString(g, TEXTFIELD_FONT, warningText, getCenterX(), getTextFieldName().getY() + getTextFieldName().getHeight() + 20);
        }
    }

}
