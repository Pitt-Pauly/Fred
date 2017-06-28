package fred.graphics;

import static fred.graphics.GameUI.SCREEN_HEIGHT;
import static fred.graphics.GameUI.SCREEN_WIDTH;
import static fred.graphics.UIUtils.createFont;
import static fred.graphics.UIUtils.drawCenteredString;
import static fred.graphics.UIUtils.handleException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

import fred.model.GameInfo;
import fred.model.Territory;

public class TerritoryUI extends Image {

    private static final UnicodeFont TEXTBOX_FONT = createFont("Serif", true, false, 14);
    private static final UnicodeFont UNIT_COUNT_FONT = createFont("SansSerif", false, false, 14);

    public static final int CIRCLE_RADIUS = 10;
    private static final int BIG_CIRCLE_RADIUS = 15;

    private static final Color COLOUR_SELECTED_AS_SOURCE = Color.blue;
    private static final Color COLOUR_SELECTED_AS_TARGET = Color.red;
    private static final Color COLOUR_UNSELECTED = Color.lightGray;

    private static final Color COLOUR_TEXTBOX_FONT_SELECTED_AS_SOURCE = Color.white;
    private static final Color COLOUR_TEXTBOX_FONT_SELECTED_AS_TARGET = Color.white;
    private static final Color COLOUR_TEXTBOX_FONT_UNSELECTED = Color.black;

    private static final float TEXTBOX_ALPHA = 0.7f;
    private static final Color COLOUR_TEXTBOX_SELECTED_AS_SOURCE = createTextboxColour(COLOUR_SELECTED_AS_SOURCE);
    private static final Color COLOUR_TEXTBOX_SELECTED_AS_TARGET = createTextboxColour(COLOUR_SELECTED_AS_TARGET);
    private static final Color COLOUR_TEXTBOX_UNSELECTED = createTextboxColour(COLOUR_UNSELECTED);

    private static Color createTextboxColour(Color color) {
        return new Color(color.r, color.g, color.b, TEXTBOX_ALPHA);
    }
    
    
    private int x = 0, y = 0;
    protected final int mapx, mapy;

    private Color RGBColour = Color.lightGray;

    private boolean isSelectedAsSource = false;
    private boolean isSelectedAsTarget = false;
    private boolean isHoveredOver = false;
    private final String territoryName;
    private GameInfo game;

    private Image redImage;
    private static final String PATH_TO_RED = "images/Design2/3_GameScreen/Coloured versions/";
    private transient boolean hasRedImage = true;


    public TerritoryUI(String territoryName, int px, int py, Color rgb) {
        if (territoryName == null) {
            throw new NullPointerException();
        }
        this.territoryName = territoryName;
        this.x = px;
        this.y = py;
        this.mapx = px;
        this.mapy = py;
        this.RGBColour = rgb;
    }
    
    TerritoryUI(TerritoryUI clone) {
    	this.territoryName = clone.territoryName;
        this.x = clone.x;
        this.y = clone.y;
        this.mapx = clone.mapx;
        this.mapy = clone.mapy;
        this.RGBColour = clone.RGBColour;
    }

    protected void setGame(GameInfo game) {
        this.game = game;
    }

    public void setX(int px) {
        this.x = px;
    }

    public void setY(int py) {
        this.y = py;
    }

    public Color getColourRGB() {
        return this.RGBColour;
    }

    public void setUnselected() {
        isSelectedAsTarget = false;
        isSelectedAsSource = false;
    }

    public void setSelectedAsSource() {
        isSelectedAsTarget = false;
        isSelectedAsSource = true;
    }

    public void setSelectedAsTarget() {
        isSelectedAsSource = false;
        isSelectedAsTarget = true;
    }

    public void setHoveredOver(boolean setHover) {
        this.isHoveredOver = setHover;
    }

    public Territory getTerritory() {
        return game.getWorld().getTerritoryByName(territoryName);
    }

    // Circle Boundaries - TODO - replace with x^2 + y^2 = r^2 ?
    public boolean inBoundaries(int mouseX, int mouseY) {
        if ((mouseX >= this.x - CIRCLE_RADIUS && mouseX <= this.x + CIRCLE_RADIUS) && (mouseY >= this.y - CIRCLE_RADIUS && mouseY <= this.y + CIRCLE_RADIUS)) {
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isSelectedAsSource() {
        return isSelectedAsSource;
    }

    public boolean isSelectedAsTarget() {
        return isSelectedAsTarget;
    }

    public boolean isHoveredOver() {
        return isHoveredOver;
    }

    private Color getColor() {
        if (isSelectedAsSource) {
            return COLOUR_SELECTED_AS_SOURCE;
        }
        if (isSelectedAsTarget) {
            return COLOUR_SELECTED_AS_TARGET;
        }
        return COLOUR_UNSELECTED;
    }

    private Color getTextboxColor() {
        if (isSelectedAsSource) {
            return COLOUR_TEXTBOX_SELECTED_AS_SOURCE;
        }
        if (isSelectedAsTarget) {
            return COLOUR_TEXTBOX_SELECTED_AS_TARGET;
        }
        return COLOUR_TEXTBOX_UNSELECTED;
    }

    private Color getTextboxFontColor() {
        if (isSelectedAsSource) {
            return COLOUR_TEXTBOX_FONT_SELECTED_AS_SOURCE;
        }
        if (isSelectedAsTarget) {
            return COLOUR_TEXTBOX_FONT_SELECTED_AS_TARGET;
        }
        return COLOUR_TEXTBOX_FONT_UNSELECTED;
    }

    private String getOwner() {
        return (getTerritory().getOwner() == null) ? "None" : getTerritory().getOwner().getName();
    }

    // TODO Size of the Box is to small for some Territory names, -> Wrap Text
    // TODO intelligent drawing of the boxes, prevent overlapping of the boxes
    public void draw(Graphics g, int scale) {

        // TODO Line up red borders. Something different for select as target.
        if (isSelectedAsSource && scale == 1) {
            if (redImage == null && hasRedImage) {
                String path = PATH_TO_RED + territoryName + ".png";
                if ((new java.io.File(path)).exists()) {
                    try {
                        redImage = new Image(path);
                    }
                    catch (SlickException ex) {
                        handleException(ex);
                    }
                }
                if (redImage == null) {
                    hasRedImage = false;
                }
            }
            if (hasRedImage) {
                redImage.draw(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            }
        }

        if (game.getWorld().getTerritoryByName(territoryName).getOwner() != null) {
            g.setColor(game.getWorld().getTerritoryByName(territoryName).getOwner().getColor());
            g.fillOval(x - BIG_CIRCLE_RADIUS / scale, y - BIG_CIRCLE_RADIUS / scale, BIG_CIRCLE_RADIUS * 2 /scale, BIG_CIRCLE_RADIUS * 2 /scale);
        }

        g.setColor(getColor());
        g.fillOval(x - CIRCLE_RADIUS/scale, y - CIRCLE_RADIUS/scale, CIRCLE_RADIUS * 2/scale, CIRCLE_RADIUS * 2/scale);
        g.setColor(Color.black);
        drawCenteredString(g, UNIT_COUNT_FONT, "" + getUnitCount(), x, y - 2);

        String unitsText = getUnitCount() + (getUnitCount() == 1 ? " Unit" : " Units");
        int textboxWidth = getMaxTextWidth(getTerritoryName(), getOwner(), unitsText) + 10;
        int textHeight = TEXTBOX_FONT.getHeight(unitsText);
        int textboxHeight = textHeight * 3 + 22;
        
        if (isHoveredOver || isSelectedAsTarget || isSelectedAsSource) {
            g.setColor(getTextboxColor());
            g.fillRect(x, y, textboxWidth, textboxHeight);
            g.drawRect(x, y, textboxWidth, textboxHeight);
            g.setColor(getTextboxFontColor());
            drawCenteredString(g, TEXTBOX_FONT, getTerritoryName(), x + textboxWidth / 2, y + textHeight/2 + 5);
            drawCenteredString(g, TEXTBOX_FONT, getOwner(), x + textboxWidth / 2, y + textHeight*3/2 + 15);
            drawCenteredString(g, TEXTBOX_FONT, getUnitCount() + (getUnitCount() == 1 ? " Unit" : " Units"), x + textboxWidth / 2, y + textHeight*5/2 + 17);
        }

    }

    private int getMaxTextWidth(String... strings) {
        int max = 0;
        for (String string : strings) {
            max = Math.max(max, TEXTBOX_FONT.getWidth(string));
        }
        return max;
    }

    public int getUnitCount() {
        return game.getWorld().getTerritoryByName(territoryName).getUnitCount();
    }

    public String getTerritoryName() {
        return territoryName;
    }
}