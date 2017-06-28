package fred.graphics;

import static fred.graphics.GameUI.INFOPANE_HEIGHT;
import static fred.graphics.GameUI.SCREEN_HEIGHT;
import static fred.graphics.GameUI.SCREEN_WIDTH;
import static fred.graphics.UIUtils.createImage;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

abstract public class PopUp extends AbstractComponent {
    static final int INACTIVE = 0;
    static final int ACTIVE = 1;
    static final int PRESSED = 2;

    private final List<AbstractComponent> components = new ArrayList<AbstractComponent>();
    private final List<PopUpListener> popUpListeners = new ArrayList<PopUpListener>();

    private final Image background;
    protected final float scalar;
    private final int width, height;
    protected int x, y;

    public PopUp(GameContainer container, String backgroundFilepath, float scalar, boolean inGame) throws SlickException {
        super(container);
        this.scalar = scalar;
        background = createImage(backgroundFilepath, scalar);
        
        width = background.getWidth();
        height = background.getHeight();
        
        x = Math.max(0, SCREEN_WIDTH / 2 - getWidth() / 2);

        if (inGame) {
            y = Math.max(0, (SCREEN_HEIGHT - INFOPANE_HEIGHT) / 2 - getHeight() / 2);
        }
        else {
            y = Math.max(0, SCREEN_HEIGHT / 2 - getHeight() / 2);
        }
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput);

        for (AbstractComponent component : components) {
            component.setAcceptingInput(acceptingInput);
        }
    }

    @Override
    public final int getX() {
        return x;
    }

    @Override
    public final int getY() {
        return y;
    }

    @Override
    public final int getWidth() {
        return width;
    }

    @Override
    public final int getHeight() {
        return height;
    }

    @Override
    public void setLocation(int x, int y) {
        // Do nothing
    }

    @Override
    @SuppressWarnings("hiding")
    public void render(GUIContext container, Graphics g) throws SlickException {
        if (!isAcceptingInput()) {
            return;
        }

        background.draw(getX(), getY());
    }

    // If an event caused this to be created, then we need to stop it from doing
    // it again.
    public void consume() {
        consumeEvent();
    }

    /**
     * Register subcomponents (eg Buttons and TextFields) of this popup. Calling
     * setAcceptingInput() on the popup will then cascade down to the
     * subcomponents.
     * 
     * @param component
     *            subcomponent of the popup
     */
    public void addComponent(AbstractComponent component) {
        components.add(component);
    }

    public void addPopUpListener(PopUpListener listener) {
        popUpListeners.add(listener);
    }

    public void close(PopUpListener.Result result) {
        setAcceptingInput(false);
        notifyPopUpClosed(result);
    }

    private void notifyPopUpClosed(PopUpListener.Result result) {
        for (PopUpListener listener : popUpListeners) {
            listener.popUpClosed(result);
        }
    }

    @Override
    public void setFocus(boolean focus) {
        // Cannot be focused
    }

}
