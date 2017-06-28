package fred.graphics;


import static fred.graphics.GameUI.SCREEN_WIDTH;
import static fred.graphics.UIUtils.createFont;
import static fred.graphics.UIUtils.drawString;
import static fred.graphics.UIUtils.getTruncatedName;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import fred.model.Card;
import fred.model.GameInfo;
import fred.model.Player;
import fred.model.TurnPhase;

public class PlayerUI extends AbstractComponent {

    private static final int WIDTH = 485;
    private static final int HEIGHT = 90;
    
    private static final int CARD_X_START = 7*SCREEN_WIDTH/16 ;
    private static final int CARD_Y_OFFSET = 10;
    private static final int CARD_X_SPACE = 5;

    private static final UnicodeFont PLAYER_TEXT_FONT = createFont("Serif", true, false, 14);
    private static final int PLAYER_NAME_MAX_PIXEL_LENGTH = 140;

    private GameInfo game;
    private int x, y;
    private CardUI[] cardUIs;
    private GUIContext container;

    public PlayerUI(GUIContext container, GameInfo game, int x, int y) {
        super(container);
        this.game = game;
        this.x = x;
        this.y = y;
        this.container = container;
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
    
    private CardUI[] getCardUIs() throws SlickException {
        if (cardUIs == null) {
            cardUIs = new CardUI[5];
            for (int index = 0; index < 5; index++) {
                cardUIs[index] = new CardUI(container, x + CARD_X_START + index * (CardUI.WIDTH + CARD_X_SPACE), y + CARD_Y_OFFSET);
            }
        }
        return cardUIs;
    }

    public Card[] getSelectedCards() throws SlickException {
        int count = 0;
        for (CardUI cardUI : getCardUIs()) {
            if (cardUI.isSelected()) {
                count++;
            }
        }
        Card[] result = new Card[count];
        count = 0;
        for (CardUI cardUI : getCardUIs()) {
            if (cardUI.isSelected() && count < result.length) {
                result[count++] = cardUI.getCard();
            }
        }
        return result;
    }
    
    @Override
    @SuppressWarnings("hiding")
    public void render(GUIContext container, Graphics g) throws SlickException {
        Player player = game.getCurrentPlayer();

        g.setColor(Color.white);
        drawString(g, PLAYER_TEXT_FONT, getTruncatedName(player.getName(), PLAYER_TEXT_FONT, PLAYER_NAME_MAX_PIXEL_LENGTH), x + 5, y + 5);
        drawString(g, PLAYER_TEXT_FONT, "" + player.getType(), x + 5, y + 20);
        if(game.canDistributeFreeUnits()) {
            drawString(g, PLAYER_TEXT_FONT, "Free Units: " + player.getFreeUnitCount(), x + 5, y + 35);
            drawString(g, PLAYER_TEXT_FONT, "Deploy Units: " + player.getRemainingDeployCount(), x + 5, y + 50);
        }
        if(game.getCurrentTurnPhase() == TurnPhase.TurnPostActionMovement || game.getCurrentTurnPhase() == TurnPhase.TurnAttack) {
            drawString(g, PLAYER_TEXT_FONT, "Moves: " + player.getRemainingMoveCount(), x + 5, y + 50);
        }

        drawString(g, PLAYER_TEXT_FONT, game.getUICurrentTurnPhase(), x + 5, y + 65);
        int cardIndex = 0;
        for (Card card : player.getCards()) {
            getCardUIs()[cardIndex].setCard(card);
            getCardUIs()[cardIndex++].render(container, g);
        }
        for (; cardIndex < 5; cardIndex++) {
            getCardUIs()[cardIndex].setCard(null);
            getCardUIs()[cardIndex].render(container, g);
        }
        
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
