package fred.graphics;

import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

public class ToggleButton extends MouseOverArea {

    private static final int FALSE = 0;
    private static final int TRUE = 1;
    
    private final Image[] normalImages = new Image[2];
    private final Image[] mouseOverImages = new Image[2];
    private final Image[] mouseDownImages = new Image[2];

    private boolean value = true;
    
    public ToggleButton(GUIContext container, Image trueImage, Image falseImage, int x, int y) {
        super(container, trueImage, x, y);
        normalImages[FALSE] = falseImage;
        normalImages[TRUE] = trueImage;
    }
    
    public void setMouseOverImages(Image trueImage, Image falseImage) {
        mouseOverImages[FALSE] = falseImage;
        mouseOverImages[TRUE] = trueImage;
        super.setMouseOverImage(value ? trueImage : falseImage);
    }

    public void setMouseDownImages(Image trueImage, Image falseImage) {
        mouseDownImages[FALSE] = falseImage;
        mouseDownImages[TRUE] = trueImage;
        super.setMouseDownImage(value ? trueImage : falseImage);
    }
    
    public void setValue(boolean value) {
        if (this.value != value) {
            this.value = value;
            super.setNormalImage(normalImages[value ? TRUE : FALSE]);
            super.setMouseOverImage(mouseOverImages[value ? TRUE : FALSE]);
            super.setMouseDownImage(mouseDownImages[value ? TRUE : FALSE]);
        }
    }
    
    public boolean getValue() {
        return value;
    }
    
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        super.mouseClicked(button, x, y, clickCount);
        if (button == 0 && isMouseOver()) {
            setValue(!value);
        }
    }

}
