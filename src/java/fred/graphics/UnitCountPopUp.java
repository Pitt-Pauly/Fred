package fred.graphics;

import static fred.graphics.ImagePaths.GENERAL_ACCEPT;
import static fred.graphics.ImagePaths.GENERAL_ACCEPT_PRESSED;
import static fred.graphics.ImagePaths.MOVEMENTPOPUP_BACKGROUND;
import static fred.graphics.ImagePaths.MOVEMENTPOPUP_DECREASE;
import static fred.graphics.ImagePaths.MOVEMENTPOPUP_DECREASE_PRESSED;
import static fred.graphics.ImagePaths.MOVEMENTPOPUP_INCREASE;
import static fred.graphics.ImagePaths.MOVEMENTPOPUP_INCREASE_PRESSED;
import static fred.graphics.UIUtils.createButton;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.drawCenteredString;
import static fred.graphics.UIUtils.handleException;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

abstract public class UnitCountPopUp extends PopUp {

    final GameUI stateGame;

    int remainingCount;
    int selectedCount;

    Button buttonIncrease;
    Button buttonDecrease;
    Button buttonAccept;

    public UnitCountPopUp(GameContainer container, GameUI stateGame, float scalar) throws SlickException {
        super(container, MOVEMENTPOPUP_BACKGROUND, scalar, true);
        this.stateGame = stateGame;

        resetCounts();
        addComponent(getButtonIncrease());
        addComponent(getButtonDecrease());
        addComponent(getButtonAccept());
        setButtonLocations();
    }

    private void resetCounts() {
        remainingCount = 0;
        selectedCount = maxUnits();
    }

    abstract public boolean doAction();

    abstract public int maxUnits();

    public TerritoryUI getTarget() {
        return stateGame.worldUI.getSelectedTarget();
    }

    public List<TerritoryUI> getSources() {
        return stateGame.worldUI.getSelectedSources();
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput);
        if (acceptingInput) {
            resetCounts();
        }
    }
    
    public int getUnitCount() {
        return selectedCount;
    }

    private Button getButtonIncrease() throws SlickException {
        if (buttonIncrease == null) {
            buttonIncrease = createButton(container, 
                    createImage(MOVEMENTPOPUP_INCREASE, scalar), 
                    createImage(MOVEMENTPOPUP_INCREASE_PRESSED, scalar), 0, 0); // TODO fix images
            buttonIncrease.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    if (remainingCount > 0) {
                        selectedCount++;
                        remainingCount--;
                    }
                }
            });
        }
        return buttonIncrease;
    }

    private Button getButtonDecrease() throws SlickException {
        if (buttonDecrease == null) {
            buttonDecrease = createButton(container, 
                    createImage(MOVEMENTPOPUP_DECREASE, scalar), 
                    createImage(MOVEMENTPOPUP_DECREASE_PRESSED, scalar), 0, 0);
            buttonDecrease.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    if (selectedCount > 1) {
                        selectedCount--;
                        remainingCount++;
                    }
                }
            });
        }
        return buttonDecrease;
    }

    private Button getButtonAccept() throws SlickException {
        if (buttonAccept == null) {
            buttonAccept = createButton(container, 
                    createImage(GENERAL_ACCEPT, 0.5f), // TODO - fix source images
                    createImage(GENERAL_ACCEPT_PRESSED, 0.5f), 0, 0);
            buttonAccept.addListener(new ComponentListener() {
                public void componentActivated(AbstractComponent source) {
                    if (doAction()) {
                        stateGame.worldUI.deselectAllNodes();
                        setAcceptingInput(false);
                    }
                }
            });
        }
        return buttonAccept;
    }

    private int getCentreY() {
        return getY() + getHeight() / 2;
    }

    @Override
    public void render(GUIContext context, Graphics g) throws SlickException {
        if (!isAcceptingInput()) {
            return;
        }
    
        super.render(container, g);

        getButtonIncrease().render(context, g);
        getButtonDecrease().render(context, g);
        getButtonAccept().render(context, g);

        g.setColor(Color.black);
        drawCenteredString(g, "" + remainingCount, getX() + 50, getCentreY());
        drawCenteredString(g, "" + selectedCount, getX() + getWidth() - 86, getCentreY());
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        setButtonLocations();
    }

    private void setButtonLocations() {
        try {
            if (buttonIncrease != null && buttonDecrease != null && buttonAccept != null) {
                // After init only
                getButtonIncrease().setLocation(getX() + getWidth() - 85, getCentreY() + 2 - getButtonIncrease().getHeight()/2); // TODO fix images
                getButtonDecrease().setLocation(getX() + 12, getCentreY() - getButtonDecrease().getHeight()/2);
                getButtonAccept().setLocation(getX() + getWidth() - 50, getCentreY() - getButtonAccept().getHeight()/2);
            }
        }
        catch (SlickException e) {
            handleException(e);
        }
        
    }
    
    @Override
    public void mousePressed(int button, int newX, int newY) {
        super.mousePressed(button, newX, newY);
        if (isAcceptingInput() && newX < getX() || newX > getX() + getWidth() || newY < getY() || newY > getY() + getHeight()) {
            stateGame.worldUI.deselectAllNodes();
            setAcceptingInput(false);
        }
    }
}
