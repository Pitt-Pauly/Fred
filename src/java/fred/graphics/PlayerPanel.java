package fred.graphics;

import static fred.graphics.ImagePaths.PLAYERSETUP_CROSSBOX;
import static fred.graphics.ImagePaths.PLAYERSETUP_CROSSBOX_CHECKED;
import static fred.graphics.ImagePaths.PLAYERSETUP_PORTRAIT;
import static fred.graphics.ImagePaths.PLAYERSETUP_TEXTBOX;
import static fred.graphics.ImagePaths.PLAYERSETUP_TOGGLE_DOWN;
import static fred.graphics.ImagePaths.PLAYERSETUP_TOGGLE_UP;
import static fred.graphics.UIUtils.createFont;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.createTextField;
import static fred.graphics.UIUtils.createToggleButton;
import static fred.graphics.UIUtils.handleException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

import fred.model.BasicPlayer;
import fred.model.Player;
import fred.model.Player.PlayerType;

public class PlayerPanel extends AbstractComponent {

    private final static String[] NAMES = { "Attila the Hun", "Adolf Hitler", "Benito Mussolini", "Cesar", "Mao Zedong", "George Bush", "Abraham Lincoln",
            "Joseph Stalin", "Tutankhamun", "King Canute", "Chuck Norris", "SkyNet", "HAL 9000", "FRED", "Napoleon Bonaparte", "John the Blind",
            "Jean-Claude Van Damme", "William Wallace", "Edward Longshanks" };

    private final static List<String> randomNames = new ArrayList<String>();

    private static final Color[] INITIAL_COLOURS = { Color.green, Color.red, Color.yellow, Color.blue, Color.black, Color.magenta, };

    private static final int BUTTON_HUMAN = 0;
    private static final int BUTTON_AI = 1;

    private static final String[] buttonImages = { PLAYERSETUP_TOGGLE_UP, PLAYERSETUP_TOGGLE_DOWN, };

    private static final String[] tickBoxes = { PLAYERSETUP_CROSSBOX_CHECKED, PLAYERSETUP_CROSSBOX, };

    private static final int SPACINGX = 8;

    private static final UnicodeFont TEXTFIELD_FONT = createFont("Serif", true, false, 14);

    
    private final int playerNumber;
    private final float scalar;

    private final int width;
    private final int height;
    private final Image portrait;
    private final Image textBoxImg;

    private final int x, y;

    private TextField textFieldName;
    private ToggleButton buttonHuman;
    private ToggleButton buttonPlaying;
    private ColourButton buttonColour;

    public PlayerPanel(GUIContext container, int centerX, int centerY, int width, int height, float scalar, int playerNumber) throws SlickException {
        super(container);
        this.playerNumber = playerNumber;
        this.scalar = scalar;
        this.width = width;
        this.height = height;
        x = centerX - (width / 2);
        y = centerY - (height / 2);
        portrait = createImage(PLAYERSETUP_PORTRAIT);
        textBoxImg = createImage(PLAYERSETUP_TEXTBOX);
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

    @SuppressWarnings("hiding")
    private ColourButton createColourButton(GUIContext context, Color colour, int x, int y, int width, int height) {
        return new ColourButton(context, colour, x, y, width, height);
    }

    @Override
    public void render(GUIContext context, Graphics g) throws SlickException {
        getButtonHuman().render(context, g);
        getButtonPlaying().render(context, g);
        textBoxImg.draw(getTextFieldName().getX() + getTextFieldName().getWidth() / 2 - (textBoxImg.getWidth() * scalar) / 2, 
                getTextFieldName().getY() + getTextFieldName().getHeight() / 2 - (textBoxImg.getHeight() * scalar) / 2 - 4, scalar);
        g.setColor(Color.black);
        getTextFieldName().render(context, g);

        getButtonColour().render(context, g);
        portrait.draw(getButtonColour().getX() + getButtonColour().getWidth() / 2 - (portrait.getWidth() * scalar) / 2, 
                getButtonColour().getY() + getButtonColour().getHeight() / 2 - (portrait.getHeight() * scalar) / 2, scalar);
    }

    private ToggleButton getButtonPlaying() throws SlickException {
        if (buttonPlaying == null) {
            buttonPlaying = createPlayingButton(container, getX() - 10, getY() + getHeight() / 2);
        }
        return buttonPlaying;
    }

    private TextField getTextFieldName() throws SlickException {
        if (textFieldName == null) {
            textFieldName = createTextField(container, TEXTFIELD_FONT, 
                    40 + getButtonPlaying().getX() + getButtonPlaying().getWidth() + SPACINGX + (int) (130 * scalar / 4),
                    getY() + getHeight() / 2 + 2, 120, 20);
            textFieldName.setBackgroundColor(new Color(0, 0, 0, 1));
            textFieldName.setBorderColor(new Color(0, 0, 0, 1));
            textFieldName.setTextColor(Color.black);
            textFieldName.setText(getRandomPlayerName());
            
        }
        return textFieldName;
    }

    private ToggleButton getButtonHuman() throws SlickException {
        if (buttonHuman == null) {
            buttonHuman = createHumanAIButton(container, getTextFieldName().getX() + getTextFieldName().getWidth() + -5 + SPACINGX + (int) (93 * scalar / 2),
                    getY() + getHeight() / 2);
        }
        return buttonHuman;
    }

    private ColourButton getButtonColour() throws SlickException {
        if (buttonColour == null) {
            buttonColour = createColourButton(container, INITIAL_COLOURS[playerNumber], 
                    getButtonHuman().getX() + getButtonHuman().getWidth() + 5 + SPACINGX + (int) (30 * scalar / 2), getY() + getHeight() / 2, 30, 40);
        }
        return buttonColour;
    }

    private ToggleButton createPlayingButton(GUIContext context, int centreX, int centreY) throws SlickException {
        return createToggleButton(context, createImage(tickBoxes[0], scalar), createImage(tickBoxes[1], scalar), centreX, centreY);
    }

    private ToggleButton createHumanAIButton(GUIContext context, int centreX, int centreY) throws SlickException {
        return createToggleButton(context, createImage(buttonImages[BUTTON_AI], scalar), createImage(buttonImages[BUTTON_HUMAN], scalar), centreX, centreY);
    }

    private String getRandomPlayerName() {
        if (randomNames.isEmpty()) {
            for (String name : NAMES) {
                randomNames.add(name);
            }
            Collections.shuffle(randomNames);
        }

        String randomName = randomNames.get(0);
        randomNames.remove(0);
        return randomName;
    }

    public Player getPlayer() throws SlickException {
        if (getTextFieldName().getText().length() == 0) {
            getTextFieldName().setText(getRandomPlayerName());
        }
        if (getTextFieldName().getText().length() > 0) {
            return new BasicPlayer(getTextFieldName().getText(), getButtonColour().getColour(), 
                    getButtonHuman().getValue() ? PlayerType.AI : PlayerType.Human);
        }
        return null;
    }

    public boolean isPlayerPlaying() throws SlickException {
        return getButtonPlaying().getValue();
    }
    
    @Override
    public void setLocation(int x, int y) {
        // Do nothing
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput);
        try {
            getButtonHuman().setAcceptingInput(acceptingInput);
            getButtonPlaying().setAcceptingInput(acceptingInput);
            getButtonColour().setAcceptingInput(acceptingInput);
            getTextFieldName().setAcceptingInput(acceptingInput);
        }
        catch (SlickException e) {
            handleException(e);
        }

    }

    @Override
    public void setFocus(boolean focus) {
        // Cannot be focused
    }

}
