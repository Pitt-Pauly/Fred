package fred.graphics;

import static fred.graphics.ImagePaths.CARD_BACKGROUND;
import static fred.graphics.ImagePaths.CARD_CANNON_ICON;
import static fred.graphics.ImagePaths.CARD_CAVALRY_ICON;
import static fred.graphics.ImagePaths.CARD_INFANTRY_ICON;
import static fred.graphics.ImagePaths.CARD_JOKER_ICON;
import static fred.graphics.UIUtils.createFont;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.drawCenteredStrings;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import fred.model.Card;

public class CardUI extends AbstractComponent {

    private static final float SCALAR = 0.6f;
    
    public static final int WIDTH = 50;
    public static final int HEIGHT = 70;
    private static final int ICON_HEIGHT_CENTRE = 25;
    private static final int TEXT_HEIGHT_CENTRE = 50;

    private static final UnicodeFont CARD_NAME_FONT = createFont("SansSerif", false, false, 8);

    private Image infantryIcon;
    private Image cavalryIcon;
    private Image cannonIcon;
    private Image jokerIcon;
    private Image cardBG;

    private Card card;
    private int x, y;
    private boolean selected;

    public CardUI(GUIContext container, int x, int y) throws SlickException {
        super(container);
        this.x = x;
        this.y = y;
        cardBG = createImage(CARD_BACKGROUND);
    }

    private void actionSelect() {
        if (selected) {
            selected = false;
        }
        else if (card != null) {
            selected = true;
        }
    }

    public void setCard(Card card) {
        this.card = card;
        if (card == null) {
            selected = false;
        }
    }

    public Card getCard() {
        return card;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    Image getInfantryIcon() throws SlickException {
        if (infantryIcon == null) {
            infantryIcon = createImage(CARD_INFANTRY_ICON, SCALAR);
        }
        return infantryIcon;
    }

    Image getCavalryIcon() throws SlickException {
        if (cavalryIcon == null) {
            cavalryIcon = createImage(CARD_CAVALRY_ICON, SCALAR);
        }
        return cavalryIcon;
    }

    Image getCannonIcon() throws SlickException {
        if (cannonIcon == null) {
            cannonIcon = createImage(CARD_CANNON_ICON, SCALAR);
        }
        return cannonIcon;
    }

    Image getJokerIcon() throws SlickException {
        if (jokerIcon == null) {
            jokerIcon = createImage(CARD_JOKER_ICON, SCALAR);
        }
        return jokerIcon;
    }

    @Override
    @SuppressWarnings("hiding")
    public void render(GUIContext container, Graphics g) throws SlickException {
        if (card != null) {
            g.setColor(selected ? Color.red : Color.black);
            g.drawRect(x, y, WIDTH, HEIGHT);
            g.setColor(Color.white);
            cardBG.draw(x + 2, y + 2, WIDTH - 4, HEIGHT - 4);
            g.setColor(Color.black);

            Image image = getCardIcon();
            image.draw(getX() + getWidth() / 2 - image.getWidth() / 2, getY() + ICON_HEIGHT_CENTRE - image.getHeight() / 2);

            String[] words = card.getTerritory().getName().split(" ");
            drawCenteredStrings(g, CARD_NAME_FONT, words, getX() + getWidth() / 2, getY() + TEXT_HEIGHT_CENTRE);
        }
        else {
            g.setColor(Color.black);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }

    private Image getCardIcon() throws SlickException {
        if (card == null) {
            return null;
        }
        switch (card.getType()) {
        case Infantry:
            return getInfantryIcon();
        case Cavalry:
            return getCavalryIcon();
        case Cannon:
            return getCannonIcon();
        default:
            return getJokerIcon();
        }
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    @SuppressWarnings("hiding")
    public void mouseClicked(int button, int x, int y, int clickCount) {
        super.mouseClicked(button, x, y, clickCount);
        if (Rectangle.contains(x, y, getX(), getY(), getWidth(), getHeight())) {
            actionSelect();
        }
    }

    public boolean isSelected() {
        return selected;
    }
}
