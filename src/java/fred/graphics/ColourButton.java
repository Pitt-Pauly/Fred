package fred.graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

import java.util.Random;

public class ColourButton extends MouseOverArea {

    static final int COLOUR_HEIGHT = 100;
    static final int COLOUR_WIDTH = 100;

    static final int COLOUR_ICON_HEIGHT = 25;
    static final int COLOUR_ICON_WIDTH = 25;

    private static Color purple = new Color(186, 28, 255);
    private static Color darkPurple = new Color(76, 0, 153);
    private static Color darkGreen = new Color(33, 132, 47);
    private static Color darkOrange = new Color(255, 75, 0);

    private static final Color[] COLOURS = { Color.yellow, Color.orange, darkOrange, Color.red, Color.green, darkGreen, Color.cyan, Color.blue, Color.pink,
            Color.magenta, purple, darkPurple, Color.white, Color.gray, Color.darkGray, Color.black, };

    private boolean colourChooserVisible = false;

    private Color colour;

    public ColourButton(GUIContext container, Color colour, int x, int y, int width, int height) {
        super(container, null, x - (width / 2), y - (height / 2), width, height);
        this.colour = colour;
    }

    @Override
    public void render(GUIContext rcontainer, Graphics g) {

        setNormalColor(colour);
        setMouseOverColor(colour);
        setMouseDownColor(colour);
        if (colourChooserVisible) {
            g.setColor(Color.darkGray);
            g.fillRect(getPopUpX(), getPopUpY(), COLOUR_WIDTH, COLOUR_HEIGHT);

            drawColours(g);
        }
        super.render(rcontainer, g);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        super.mouseClicked(button, x, y, clickCount);
        if (button == 0) {

            if (!colourChooserVisible && isMouseOver()) {
                colourChooserVisible = true;
                return;
            }

            if (colourChooserVisible) {

                int index = getCurrentColourIndex();
                int perRow = COLOUR_HEIGHT / COLOUR_ICON_HEIGHT;
                int perColumn = COLOUR_WIDTH / COLOUR_ICON_WIDTH;

                for (int i = 0; i < perColumn; i++) {

                    if ((x >= getPopUpX() + i * COLOUR_ICON_WIDTH) && (x < getPopUpX() + (i + 1) * COLOUR_ICON_WIDTH)) {

                        if (y >= getPopUpY() && y < getPopUpY() + 1 * COLOUR_ICON_HEIGHT)
                            index = i;
                        if (y >= getPopUpY() + 1 * COLOUR_ICON_HEIGHT && y < getPopUpY() + 2 * COLOUR_ICON_HEIGHT)
                            index = i + perRow;
                        if (y >= getPopUpY() + 2 * COLOUR_ICON_HEIGHT && y < getPopUpY() + 3 * COLOUR_ICON_HEIGHT)
                            index = i + 2 * perRow;
                        if (y >= getPopUpY() + 3 * COLOUR_ICON_HEIGHT && y < getPopUpY() + 4 * COLOUR_ICON_HEIGHT)
                            index = i + 3 * perRow;
                    }
                }
                if (index >= COLOURS.length) {
                    index = getCurrentColourIndex();
                }

                colour = COLOURS[index];
                colourChooserVisible = false;
                return;
            }
        }
    }

    public Color getColour() {
        return colour;
    }

    public Color randomColour() {
        Random generator = new Random();

        int randomIndex = generator.nextInt(COLOURS.length);

        return COLOURS[randomIndex];
    }

    public int getCurrentColourIndex() {
        for (int index = 0; index < COLOURS.length; index++) {
            if (COLOURS[index] == getColour()) {
                return index;
            }
        }
        return 0;
    }

    public int getPopUpX() {
        return getX() + getWidth();
    }

    public int getPopUpY() {
        return getY() + getHeight() / 2 - COLOUR_HEIGHT / 2;
    }

    public void drawColours(Graphics g) {
        int currentX = 0;
        int currentY = 0;
        for (int i = 0; i < COLOURS.length; i++) {
            g.setColor(COLOURS[i]);
            g.fillRect(getPopUpX() + currentX * COLOUR_ICON_WIDTH, getPopUpY() + currentY * COLOUR_ICON_HEIGHT, COLOUR_ICON_WIDTH, COLOUR_ICON_HEIGHT);
            currentX++;

            if (currentX >= COLOUR_WIDTH / COLOUR_ICON_WIDTH) {
                currentX = 0;
                currentY++;
            }

        }
    }
}