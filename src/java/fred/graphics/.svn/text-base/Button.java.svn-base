package fred.graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * Simple push button based on MouseOverArea. Handles pressed rendering better,
 * but does not provide sound features.
 * 
 * @author dstewart
 */
public class Button extends AbstractComponent {

    public static final int HIDDEN = 0;
    public static final int DISABLED = 1;
    public static final int ENABLED = 2;

    private final Image[] normalImages = new Image[3];
    private final Image[] mouseOverImages = new Image[3];
    private final Image[] mouseDownImages = new Image[3];
    
    private int state = ENABLED;
    
    private Rectangle area;
    private Image currentImage;
    private boolean mouseOver;
    private boolean mouseButtonDown;
    
    public Button(GUIContext container, Image enabledImage, int x, int y) {
        this(container, enabledImage, enabledImage, enabledImage, x, y);
    }
    
    public Button(GUIContext container, Image enabledImage, Image disabledImage, Image hiddenImage, int x, int y) {
        super(container);
        area = new Rectangle(x,y, enabledImage.getWidth(), enabledImage.getHeight());
        
        normalImages[HIDDEN] = hiddenImage;
        mouseOverImages[HIDDEN] = hiddenImage;
        mouseDownImages[HIDDEN] = hiddenImage;
        normalImages[DISABLED] = disabledImage;
        mouseOverImages[DISABLED] = disabledImage;
        mouseDownImages[DISABLED] = disabledImage;
        normalImages[ENABLED] = enabledImage;
        setMouseOverImage(enabledImage);
        setMouseDownImage(enabledImage);
        
        currentImage = enabledImage;

        Input currentInput = container.getInput();
        mouseOver = area.contains(currentInput.getMouseX(), currentInput.getMouseY());
        mouseButtonDown = currentInput.isMouseButtonDown(0);
        updateImage();
    }
    
    public void setMouseOverImage(Image image) {
        mouseOverImages[ENABLED] = image;
    }

    public void setMouseDownImage(Image image) {
        mouseDownImages[ENABLED] = image;
    }
    
    public void setState(int state) {
        if (state < HIDDEN || state > ENABLED) {
            throw new IllegalArgumentException("Invalid state: " + state);
        }
        if (this.state != state) {
            this.state = state;
            setAcceptingInput(state == ENABLED);
        }
    }
    
    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        super.setAcceptingInput(acceptingInput && state == ENABLED);
    }
    
    public boolean isEnabled() {
        return state == ENABLED;
    }
    
    @Override
    public void setLocation(int x, int y) {
        if (area != null) {
            area.setX(x);
            area.setY(y);
        }
    }

    @Override
    public int getX() {
        return (int) area.getX();
    }

    @Override
    public int getY() {
        return (int) area.getY();
    }
    

    @Override
    @SuppressWarnings("hiding")
    public void render(GUIContext container, Graphics g) {
        updateImage();
        if (currentImage != null) {
            int xp = (int) (area.getX() + ((getWidth() - currentImage.getWidth()) / 2));
            int yp = (int) (area.getY() + ((getHeight() - currentImage.getHeight()) / 2));
            currentImage.draw(xp, yp, Color.white);
        }
        else {
            g.setColor(Color.white);
            g.fill(area);
        }
    }

    private void updateImage() {
        if (!mouseOver) {
            currentImage = normalImages[state];
        }
        else if (mouseButtonDown) {
            currentImage = mouseDownImages[state];
        }
        else {
            currentImage = mouseOverImages[state];
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mouseOver = area.contains(newx, newy);
        if (mouseButtonDown && !mouseOver) {
            mouseButtonDown = false;
        }
    }

    @Override
    public void mousePressed(int button, int mx, int my) {
        mouseOver = area.contains(mx, my);
        if (mouseOver && button == 0) {
            mouseButtonDown = true;
            notifyListeners();
        }
    }
    
    @Override
    public void mouseReleased(int button, int mx, int my) {
        mouseOver = area.contains(mx, my);
        if (button == 0) {
            mouseButtonDown = false; 
        }
    }

    @Override
    public int getHeight() {
        return (int) area.getHeight();
    }

    @Override
    public int getWidth() {
        return (int) area.getWidth();
    }

}
