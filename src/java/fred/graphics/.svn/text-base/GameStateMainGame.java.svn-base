package fred.graphics;

import static fred.graphics.GameUI.GAMEOVER_HEIGHT;
import static fred.graphics.GameUI.INFOPANE_HEIGHT;
import static fred.graphics.GameUI.MENU_HEIGHT;
import static fred.graphics.GameUI.MENU_WIDTH;
import static fred.graphics.GameUI.SCREEN_HEIGHT;
import static fred.graphics.GameUI.SCREEN_WIDTH;
import static fred.graphics.ImagePaths.*;
import static fred.graphics.UIUtils.createButton;
import static fred.graphics.UIUtils.createFont;
import static fred.graphics.UIUtils.createImage;
import static fred.graphics.UIUtils.createToggleButton;
import static fred.graphics.UIUtils.drawCenteredString;
import static fred.graphics.UIUtils.getTruncatedName;
import static fred.graphics.UIUtils.handleException;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fred.datahandling.SaveLoader;
import fred.graphics.PopUpListener.Result;
import fred.model.AttackRecord;
import fred.model.Card;
import fred.model.Territory;
import fred.model.TurnPhase;

public class GameStateMainGame extends BasicGameState {

    public static final int STATE_ID = 1;

    static final UnicodeFont GAMEOVER_FONT = createFont("Serif", true, false, 20);
    static final UnicodeFont ATTACKRECORD_FONT = createFont("Serif", true, false, 14);

    static final float SCALAR = 0.6f;

    private static final int ATTACK = 0;
    private static final int MOVE = 1;
    private static final int DRAFT = 2;

    
    GameContainer gameContainer;
    GameUI stateGame;

    private InfoPane infoPane;
    PopUp currentPopUp;
    private PlayerUI playerUI;
    private boolean controlDown = false;
    private MovementUnitCountPopUp movementUnitCountPopUp;
    private AttackUnitCountPopUp attackUnitCountPopUp;
    private AttackRecordPopUp attackRecordPopUp;
    private DistributeUnitCountPopUp distributeUnitCountPopUp;
    private GameOverPopUp gameOverPopUp;
    private MenuPopup menuPopup;

    public void init(GameContainer container, StateBasedGame stategame) throws SlickException {
        this.gameContainer = container;
        this.stateGame = (GameUI) stategame;
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame stategame) throws SlickException {
    	playerUI = new PlayerUI(gameContainer, stateGame.game, 150, SCREEN_HEIGHT - INFOPANE_HEIGHT +13);
        infoPane = getInfoPane();
        setAcceptingInput(true);
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        setAcceptingInput(false);
    }
    
    private void setAcceptingInput(boolean acceptingInput) throws SlickException {
        getInfoPane().setAcceptingInput(acceptingInput);
        
        if (isPopUpVisible() && !acceptingInput) {
            currentPopUp.setAcceptingInput(false);
        }
    }
    
    PlayerUI getPlayerUI() {
        return playerUI;
    }

    public void update(GameContainer gc, StateBasedGame stategame, int delta) throws SlickException {
        getInfoPane().update();
    }
    
    public void render(GameContainer container, StateBasedGame stagegame, Graphics g) throws SlickException {
        stateGame.worldUI.render(container, g);
        getInfoPane().render(container, g);
       // renderPlayerColourLegend(g); TODO - need this ?
        if (isPopUpVisible()) { 
            currentPopUp.render(container, g);
        }
    }

    @Override
    public int getID() {
        return STATE_ID;
    }
    
    @Override
    public void mouseMoved(int oldX, int oldY, int newX, int newY) {
        if (isPopUpVisible()) {
            return;
        }
        stateGame.worldUI.selectHoveredOn(newX, newY);
    }

    @Override
    public void mousePressed(int button, int x, int y) {

        if (isPopUpVisible()) {
            return;
        }

        if (stateGame.game.isGameOver()) {
            try {
                showPopUp(getGameOverPopUp());
            }
            catch (SlickException e) {
                handleException(e);
            }
            currentPopUp.consume();
            return;
        }

        try {
            if (infoPane.isInfoPaneMouseEvent(x, y)) {
                return;
            }
        }
        catch (SlickException e) {
            handleException(e);
            return;
        }
        
        // left mouse button pressed
        if (button == 0) {
            TerritoryUI selectedTerritory;
            if (controlDown) {
                selectedTerritory = stateGame.worldUI.selectSourceOn(x, y, stateGame.game.getCurrentPlayer(), true);
            }
            else {
                stateGame.worldUI.deselectAllNodes();
                selectedTerritory = stateGame.worldUI.selectSourceOn(x, y, stateGame.game.getCurrentPlayer(), false);
            }

            if (!isPopUpVisible() && selectedTerritory != null) {
                if (stateGame.game.canDistributeFreeUnits()) {
                    if (stateGame.game.canDistributeFreeUnits(selectedTerritory.getTerritory(), 1)) {
                        try {
                            actionPlaceUnit(selectedTerritory);
                        }
                        catch (SlickException e) {
                            handleException(e);
                        }
                    }
                }
            }
        }

        // right mouse button pressed
        if (button == 1) {
            stateGame.worldUI.selectTargetOn(x, y, stateGame.game.getCurrentPlayer(), stateGame.game.canMoveUnits() && !stateGame.game.canAttack(),
                    stateGame.game.canAttack() && !stateGame.game.canMoveUnits());
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        // not needed
    }

  
    /*private void renderPlayerColourLegend(Graphics g) {
    	int y = GameUI.SCREEN_HEIGHT - GameUI.INFOPANE_HEIGHT 
                     - (stateGame.game.getPlayers().size()+1)*20;
    	for (Player player: stateGame.game.getPlayers()) {
    		y += 20;
    		g.setColor(player.getColor());
    		g.fillRect(10, y, 12, 12);
    		g.setColor(Color.black);
    		String name = player.getName();
    		if(player == stateGame.game.getCurrentPlayer()) {
    			name = name + " -";
    		}
    		g.drawString(name, 27, y - 3);
    	}
    }*/

    void actionAttack() throws SlickException {
        if (!stateGame.worldUI.getSelectedSources().isEmpty() && stateGame.worldUI.getSelectedTarget() != null) {
            showPopUp(getAttackUnitCountPopUp());
        }
    }
   
    private void actionPlaceUnit(TerritoryUI territory) throws SlickException {
        if (!stateGame.worldUI.getSelectedSources().isEmpty()) {
            getDistributeUnitCountPopUp().setLocation(territory);
    		showPopUp(getDistributeUnitCountPopUp());
    	}
    }

    void actionMovement() throws SlickException {
        if (!stateGame.worldUI.getSelectedSources().isEmpty() && stateGame.worldUI.getSelectedTarget() != null) {
            showPopUp(getMovementUnitCountPopUp());  
        }
    }

    void actionConscription() {
    	if(stateGame.game.canChooseTurnType()) {
    		stateGame.game.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
    	}
    }

    void actionEndTurn() {
        if (stateGame.game.getCurrentPlayer().isAI()) {
            stateGame.game.AIdoTurn();
            stateGame.worldUI.deselectAllNodes();
        }
        else {
            if (stateGame.game.canEndTurnPhase()) {
                stateGame.game.endCurrentTurnPhase();
                stateGame.worldUI.deselectAllNodes();
                if (stateGame.game.getCurrentPlayer().isAI()) {
                    stateGame.game.AIdoTurn();
                }
            }
        }
    }

    void actionExchangeCards() throws SlickException {
    	if(!stateGame.game.getCurrentPlayer().isAI()) {
    		stateGame.game.exchangeCards(getPlayerUI().getSelectedCards());
    	}
    }
    
    void actionSave() {
    	SaveLoader saver = new SaveLoader();
    	saver.save("save.sav", stateGame.game.getReplay());
    }
    
    void actionShowMenu() throws SlickException{
        showPopUp(getMenuPopup());
    }
    

    class InfoPane extends AbstractComponent {

        // Spacing between the buttons in the middle of the infopane (endturn
        // etc.)
        private static final int SPACING_MIDDLE = 5;
        // distance from the actual center
        private static final int CENTER_OFFSET = -10;

        private Image infoPaneImg;
        private Image bgImg;
        private Image bolts;
        private Image portrait;
        private Image datamap;
        private Button buttonLeft;
        private Button buttonMiddle;
        private Button buttonRight;
        private ExchangeCardsPopUp exhangePopUp;
        private final Image attackPressed;
        private final Image attackNotPressed;
        private final Image movementPressed;
        private final Image movementNotPressed;
        private final Image conscriptionPressed;
        private final Image conscriptionNotPressed;
        private boolean isAttackEnabled = true;
        private boolean isMovementEnabled = false;
        private boolean isConscriptionEnabled = false;
        private Color[] datamapColours = { new Color(0, 255, 18), new Color(255, 0, 0), new Color(0, 30, 255) };

        private float buttonScalar = (float) 0.55;

        public InfoPane(GameContainer gameContainer) throws SlickException {
            super(gameContainer);
            bgImg = createImage(INFOPANE_BACKGROUND);
            infoPaneImg = createImage(INFOPANE_MENU);
            bolts = createImage(INFOPANE_BOLTS).getFlippedCopy(false, true);
            portrait = createImage(INFOPANE_PORTRAIT);
            attackPressed = createImage(INFOPANE_ATTACK_PRESSED, buttonScalar);
            attackNotPressed = createImage(INFOPANE_ATTACK, buttonScalar);
            movementPressed = createImage(INFOPANE_MOVE_PRESSED, buttonScalar);
            movementNotPressed = createImage(INFOPANE_MOVE, buttonScalar);
            conscriptionPressed = createImage(INFOPANE_DRAFT_PRESSED, buttonScalar);
            conscriptionNotPressed = createImage(INFOPANE_DRAFT, buttonScalar);
        }

        @Override
        public void mousePressed(int button, int x, int y) {
            if (isPopUpVisible()) {
                return;
            }

            try {
                if (Rectangle.contains(x, y, 0, SCREEN_HEIGHT - getDatamap().getHeight(), getDatamap().getWidth(), getDatamap().getHeight())) {

                    switch (getButtonOn(x, y)) {
                    case ATTACK:
                        consumeEvent();
                        actionAttack();
                        return;
                    case MOVE:
                        consumeEvent();
                        actionMovement();
                        return;
                    case DRAFT:
                        consumeEvent();
                        actionConscription();
                        return;
                    }
                }
            }
            catch (SlickException ex) {
                handleException(ex);
            }
        }

        public boolean isInfoPaneMouseEvent(int x, int y) throws SlickException {
            if (y > SCREEN_HEIGHT - INFOPANE_HEIGHT) {
                return true;
            }
            if ((y > SCREEN_HEIGHT - getDatamap().getHeight()) && (x < getDatamap().getWidth())) {
                return getButtonOn(x, y) > -1;
            }

            return false;
        }

        public void update() throws SlickException {
            if (!stateGame.game.getCurrentPlayer().isAI() && stateGame.game.canExchangeCards()) {
                if (stateGame.game.canExchangeCards(getPlayerUI().getSelectedCards())) {
                    getExchangeCardsPopUp().setAcceptingInput(true);
                }
                else {
                    getExchangeCardsPopUp().setAcceptingInput(false);
                }
            }

            List<TerritoryUI> sources = stateGame.worldUI.getSelectedSources();
            TerritoryUI target = stateGame.worldUI.getSelectedTarget();

            if (stateGame.game.canEndTurnPhase() || stateGame.game.getCurrentPlayer().isAI()) {
                getButtonMiddle().setState(Button.ENABLED);
            }
            else {
                getButtonMiddle().setState(Button.DISABLED);
            }

            isMovementEnabled = false;
            if (stateGame.game.canMoveUnits()) {
                if (!sources.isEmpty() && target != null) {
                    for (TerritoryUI source : sources) {
                        if (stateGame.game.canMoveUnits(source.getTerritory(), target.getTerritory(), stateGame.game.maxUnitsToMove(source.getTerritory(),
                                target.getTerritory()))) {
                            isMovementEnabled = true;
                            break;
                        }
                    }
                }
            }

            isAttackEnabled = false;
            if (stateGame.game.canAttack()) {
                if (!sources.isEmpty() && target != null) {
                    List<Integer> numberOfUnits = new ArrayList<Integer>();
                    List<Territory> territories = new ArrayList<Territory>();
                    for (TerritoryUI t : sources) {
                        territories.add(t.getTerritory());
                        numberOfUnits.add(t.getUnitCount() - 1);
                    }
                    isAttackEnabled = stateGame.game.canAttack(territories, target.getTerritory(), numberOfUnits);
                }
            }

            isConscriptionEnabled = stateGame.game.canConscript();
        }

        @Override
        public void setAcceptingInput(boolean acceptingInput) {
            super.setAcceptingInput(acceptingInput);
            try {
                getButtonLeft().setAcceptingInput(acceptingInput);
                getButtonMiddle().setAcceptingInput(acceptingInput);
                getButtonRight().setAcceptingInput(acceptingInput);
                getExchangeCardsPopUp().setAcceptingInput(false);
            }
            catch (SlickException e) {
                handleException(e);
            }
        }

        private Image getDatamap() throws SlickException {
            if (datamap == null) {
                datamap = createImage(INFOPANE_ACTIONS_DATAMAP, buttonScalar);
            }
            return datamap;
        }

        private int getButtonOn(int mX, int mY) throws SlickException {
            if (mX < 0 || mX > datamap.getWidth()) {
                return -1;
            }

            if (mY < SCREEN_HEIGHT - datamap.getHeight() || mY > SCREEN_HEIGHT) {
                return -1;
            }

            Color pixelColour = getDatamap().getColor((int) (mX / buttonScalar), (int) ((mY - (SCREEN_HEIGHT - datamap.getHeight())) / buttonScalar));

            if (datamapColours[ATTACK].equals(pixelColour)) {
                return ATTACK;
            }
            else if (datamapColours[MOVE].equals(pixelColour)) {
                return MOVE;
            }
            else if (datamapColours[DRAFT].equals(pixelColour)) {
                return DRAFT;
            }

            return -1;
        }

        @Override
        public int getHeight() {
            return INFOPANE_HEIGHT;
        }

        @Override
        public int getWidth() {
            return SCREEN_WIDTH;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return SCREEN_HEIGHT - INFOPANE_HEIGHT;
        }

        @Override
        public void render(GUIContext context, Graphics g) throws SlickException {
            bgImg.draw(0, getY(), SCREEN_WIDTH, INFOPANE_HEIGHT);

            getButtonLeft().render(context, g);
            getButtonMiddle().render(context, g);
            getButtonRight().render(context, g);

            infoPaneImg.draw(0, getY(), SCREEN_WIDTH, INFOPANE_HEIGHT);
            portrait.draw(SCREEN_WIDTH - 125, 0, SCALAR);

            bolts.draw(-30, getY() - 30, 150, 200);

            getAttackImg().draw(0, SCREEN_HEIGHT - getAttackImg().getHeight());
            getMovementImg().draw(0, SCREEN_HEIGHT - getMovementImg().getHeight());
            getConscriptionImg().draw(0, SCREEN_HEIGHT - getConscriptionImg().getHeight());
            // getDatamap().draw(0,SCREEN_HEIGHT - getDatamap().getHeight());

            getExchangeCardsPopUp().render(context, g);
            getPlayerUI().render(context, g);

        }

        private Button getButtonLeft() throws SlickException {
            if (buttonLeft == null) {
                Image pressedImage = createImage(INFOPANE_LEFT_PRESSED, buttonScalar);
                Image notPressedImage = createImage(INFOPANE_LEFT, buttonScalar);
                buttonLeft = createButton(gameContainer, notPressedImage, notPressedImage, pressedImage, pressedImage, pressedImage, 0, getY());
                buttonLeft.setLocation(SCREEN_WIDTH / 2 - getButtonMiddle().getWidth() / 2 - buttonLeft.getWidth() - SPACING_MIDDLE + CENTER_OFFSET, getY());
                // buttonLeft.setState(Button.DISABLED);
                buttonLeft.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        // TODO - implement action
                    }
                });
            }
            return buttonLeft;
        }

        private Button getButtonMiddle() throws SlickException {
            if (buttonMiddle == null) {
                Image pressedImage = createImage(INFOPANE_MIDDLE_PRESSED, buttonScalar);
                Image notPressedImage = createImage(INFOPANE_MIDDLE, buttonScalar);
                buttonMiddle = createButton(gameContainer, notPressedImage, notPressedImage, pressedImage, pressedImage, pressedImage, 0, getY());
                buttonMiddle.setLocation(SCREEN_WIDTH / 2 - getButtonMiddle().getWidth() / 2 + CENTER_OFFSET, getY());
                buttonMiddle.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        if (isPopUpVisible()) {
                            return;
                        }
                        try {
                            if (getExchangeCardsPopUp().isAcceptingInput()) {
                                getExchangeCardsPopUp().setAcceptingInput(false);
                            }
                        }
                        catch (SlickException e) {
                            handleException(e);
                        }
                        actionEndTurn();
                    }
                });
            }
            return buttonMiddle;
        }

        private Button getButtonRight() throws SlickException {
            if (buttonRight == null) {
                Image pressedImage = createImage(INFOPANE_RIGHT_PRESSED, buttonScalar);
                Image notPressedImage = createImage(INFOPANE_RIGHT, buttonScalar);
                buttonRight = createButton(gameContainer, notPressedImage, notPressedImage, pressedImage, pressedImage, pressedImage, 0, getY());
                buttonRight.setLocation(SCREEN_WIDTH / 2 + getButtonMiddle().getWidth() / 2 + SPACING_MIDDLE + CENTER_OFFSET, getY());
                buttonRight.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        if (isPopUpVisible()) {
                            return;
                        }
                        try {
                            actionShowMenu();
                        }
                        catch (SlickException e) {
                            handleException(e);
                        }
                        currentPopUp.consume();
                    }
                });
            }
            return buttonRight;
        }

        ExchangeCardsPopUp getExchangeCardsPopUp() throws SlickException {
            if (exhangePopUp == null) {
                exhangePopUp = new ExchangeCardsPopUp(gameContainer, buttonScalar);
                exhangePopUp.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        if (isPopUpVisible()) {
                            return;
                        }
                        try {
                            getExchangeCardsPopUp().setAcceptingInput(false);
                            actionExchangeCards();
                        }
                        catch (SlickException e) {
                            handleException(e);
                        }
                    }
                });
            }
            return exhangePopUp;
        }

        class ExchangeCardsPopUp extends AbstractComponent {

            private final Image background;
            private final Rectangle area;

            public ExchangeCardsPopUp(GameContainer gameContainer, float scalar) throws SlickException {
                super(gameContainer);
                background = createImage(MAINGAME_EXCHANGE_POPUP, scalar);
                area = new Rectangle(getX(), getY(), getWidth(), getHeight());
            }

            @Override
            public int getHeight() {
                return background.getHeight();
            }

            @Override
            public int getWidth() {
                return background.getWidth();
            }

            @Override
            public int getX() {
                return SCREEN_WIDTH - getWidth() - 10;
            }

            @Override
            public int getY() {
                return SCREEN_HEIGHT - INFOPANE_HEIGHT - getHeight();
            }

            @Override
            @SuppressWarnings("hiding")
            public void render(GUIContext container, Graphics g) throws SlickException {
                if (!isAcceptingInput()) {
                    return;
                }
                background.draw(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.black);

                String unitCount = "0";
                if (stateGame.game.canExchangeCards()) {
                    Card[] cards = getPlayerUI().getSelectedCards();
                    if (stateGame.game.canExchangeCards(cards)) {
                        unitCount = "" + stateGame.game.getExchangeCardsValue(cards);
                    }
                }
                drawCenteredString(g, unitCount, getX() + getWidth() / 3, getY() + getHeight() / 2);
            }

            @Override
            public void setLocation(int x, int y) {
                // Do nothing
            }

            @Override
            public void mousePressed(int button, int mx, int my) {
                if (button == 0 && isAcceptingInput() && area.contains(mx, my)) {
                    notifyListeners();
                }
            }
        }

        private Image getAttackImg() {
            return (isAttackEnabled) ? attackNotPressed : attackPressed;
        }

        private Image getMovementImg() {
            return (isMovementEnabled) ? movementNotPressed : movementPressed;
        }

        private Image getConscriptionImg() {
            return (isConscriptionEnabled) ? conscriptionNotPressed : conscriptionPressed;
        }

        @Override
        public void setLocation(int x, int y) {
            // Do nothing
        }

    }       

    private InfoPane getInfoPane() throws SlickException {
        if (infoPane == null) {
            infoPane = new InfoPane(gameContainer);
        }
        return infoPane;
    }
    


    //   ********* Menu description starts here *********

    @Override
    public void keyPressed(int key, char c) {
        if ((key == Input.KEY_RCONTROL) || (key == Input.KEY_LCONTROL)) {
            controlDown = true;
        }

        if (key == Input.KEY_ESCAPE) {
            if (!isPopUpVisible()) {
                try {
                    actionShowMenu();
                }
                catch (SlickException e) {
                    handleException(e);
                }
                currentPopUp.consume();
            }
            else {
                currentPopUp.setAcceptingInput(false);
            }
        }
    }

    @Override
    public void keyReleased(int key, char c){
        if ((key == Input.KEY_RCONTROL) || (key == Input.KEY_LCONTROL)){
            controlDown = false;
        }
    }

    class MenuPopup extends PopUp {

        // spacing between the pop up buttons
        private static final int POPUP_SPACING = 40;

        private static final int BUTTON_RESUMEGAME = 0;
        private static final int BUTTON_SAVE = 1;
        // private static final int BUTTON_REPLAY = 2;
        private static final int BUTTON_LOAD = 2;
        private static final int BUTTON_SETTINGS = 3;
        private static final int BUTTON_END = 4;

        private String[][] buttonImageFilenames = { 
                { INGAMEMENU_RESUME, INGAMEMENU_RESUME_PRESSED, INGAMEMENU_RESUME_PRESSED },
                { INGAMEMENU_SAVE, INGAMEMENU_SAVE_PRESSED, INGAMEMENU_SAVE_PRESSED }, 
                { INGAMEMENU_LOAD, INGAMEMENU_LOAD_PRESSED, INGAMEMENU_LOAD_PRESSED },
                { INGAMEMENU_SETTINGS, INGAMEMENU_SETTINGS_PRESSED, INGAMEMENU_SETTINGS_PRESSED },
                { INGAMEMENU_END, INGAMEMENU_END_PRESSED, INGAMEMENU_END_PRESSED } };

        private Button buttonMenuResumeGame;
        private Button buttonMenuSave;
        // MouseOverArea buttonMenuReplay;
        private Button buttonMenuLoad;
        private Button buttonMenuSettings;
        private Button buttonMenuEnd;
        private SettingsPopUp settingsPopUp;
        private LoadGamePopUp loadGamePopUp;
        private SaveGamePopUp saveGamePopUp;

        public MenuPopup(GameContainer gameContainer) throws SlickException {
            super(gameContainer, INGAMEMENU_MENU, SCALAR, true);

            addComponent(getButtonMenuResumeGame());
            addComponent(getButtonMenuSave());
            addComponent(getButtonMenuLoad());
            addComponent(getButtonMenuSettings());
            addComponent(getButtonMenuEnd());
            getSettingsPopUp();
            getLoadGamePopUp();
            getSaveGamePopUp();
        }

        private Button getButtonMenuResumeGame() throws SlickException {
            if (buttonMenuResumeGame == null) {
                buttonMenuResumeGame = createButton(gameContainer, 
                        createImage(buttonImageFilenames[BUTTON_RESUMEGAME][INACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_RESUMEGAME][ACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_RESUMEGAME][PRESSED], SCALAR), 0, 0);
                buttonMenuResumeGame.setLocation(getX() + getWidth() / 2 - buttonMenuResumeGame.getWidth() / 2, getY() + 90);
                buttonMenuResumeGame.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        MenuPopup.this.setAcceptingInput(false);
                    }
                });
            }
            return buttonMenuResumeGame;
        }

        private Button getButtonMenuSave() throws SlickException {
            if (buttonMenuSave == null) {
                buttonMenuSave = createButton(gameContainer, 
                        createImage(buttonImageFilenames[BUTTON_SAVE][INACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_SAVE][ACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_SAVE][PRESSED], SCALAR), getX() + MENU_WIDTH / 2, getY() + 4 * MENU_HEIGHT / 13);
                buttonMenuSave.setLocation(getX() + getWidth() / 2 - buttonMenuSave.getWidth() / 2, getButtonMenuResumeGame().getHeight()
                        + getButtonMenuResumeGame().getY() + POPUP_SPACING);
                buttonMenuSave.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        try {
                            showPopUp(getSaveGamePopUp());
                        }
                        catch (SlickException e) {
                            handleException(e);
                        }

                        MenuPopup.this.setAcceptingInput(false);
                    }
                });
            }
            return buttonMenuSave;
        }

        /*
         * private MouseOverArea getButtonMenuReplay() throws SlickException {
         * if (buttonMenuReplay == null) { buttonMenuReplay =
         * createButton(gameContainer,
         * createImage(buttonImageFilenames[BUTTON_REPLAY
         * ][INACTIVE]).getScaledCopy(scalar),
         * createImage(buttonImageFilenames[BUTTON_REPLAY
         * ][ACTIVE]).getScaledCopy(scalar),
         * createImage(buttonImageFilenames[BUTTON_REPLAY
         * ][PRESSED]).getScaledCopy(scalar), getX() + MENU_WIDTH/2, getY()+
         * 6*MENU_HEIGHT/13 ); buttonMenuReplay.addListener(new
         * ComponentListener() { public void
         * componentActivated(AbstractComponent source) {
         * stateGame.enterState(GameStateReplay.STATE_ID); }}); } return
         * buttonMenuReplay; }
         */

        private Button getButtonMenuLoad() throws SlickException {
            if (buttonMenuLoad == null) {
                buttonMenuLoad = createButton(gameContainer, 
                        createImage(buttonImageFilenames[BUTTON_LOAD][INACTIVE], SCALAR),
                        createImage(buttonImageFilenames[BUTTON_LOAD][ACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_LOAD][PRESSED], SCALAR), getX() + MENU_WIDTH / 2, getY() + 8 * MENU_HEIGHT / 13);
                buttonMenuLoad.setLocation(getX() + getWidth() / 2 - buttonMenuLoad.getWidth() / 2, getButtonMenuSave().getHeight()
                        + getButtonMenuSave().getY() + POPUP_SPACING);
                buttonMenuLoad.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        try {
                            showPopUp(getLoadGamePopUp());
                        }
                        catch (SlickException e) {
                            handleException(e);
                        }

                        MenuPopup.this.setAcceptingInput(false);
                    }
                });
            }
            return buttonMenuLoad;
        }

        private Button getButtonMenuSettings() throws SlickException {
            if (buttonMenuSettings == null) {
                buttonMenuSettings = createButton(gameContainer, 
                        createImage(buttonImageFilenames[BUTTON_SETTINGS][INACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_SETTINGS][ACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_SETTINGS][PRESSED], SCALAR), getX() + MENU_WIDTH / 2, getY() + 10 * MENU_HEIGHT / 13);
                buttonMenuSettings.setLocation(getX() + getWidth() / 2 - buttonMenuSettings.getWidth() / 2, getButtonMenuLoad().getHeight()
                        + getButtonMenuLoad().getY() + POPUP_SPACING);
                buttonMenuSettings.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        try {
                            showPopUp(getSettingsPopUp());
                        }
                        catch (SlickException ex) {
                            handleException(ex);
                        }
                        MenuPopup.this.setAcceptingInput(false);
                    }
                });
            }
            return buttonMenuSettings;
        }

        PopUp getSettingsPopUp() throws SlickException {
            if (settingsPopUp == null) {
                settingsPopUp = new SettingsPopUp(gameContainer, stateGame, SCALAR, true);
                settingsPopUp.setAcceptingInput(false);
                settingsPopUp.addPopUpListener(new PopUpListener() {
                    public void popUpClosed(Result result) {
                        showPopUp(MenuPopup.this);
                    }
                });
            }
            return settingsPopUp;
        }

        LoadGamePopUp getLoadGamePopUp() throws SlickException {
            if (loadGamePopUp == null) {
                loadGamePopUp = new LoadGamePopUp(gameContainer, stateGame, SCALAR, true);
                loadGamePopUp.setAcceptingInput(false);
                loadGamePopUp.addPopUpListener(new PopUpListener() {
                    public void popUpClosed(Result result) {
                        if (result == Result.Cancel) {
                            showPopUp(MenuPopup.this);
                        }
                    }
                });
            }
            return loadGamePopUp;
        }

        SaveGamePopUp getSaveGamePopUp() throws SlickException {
            if (saveGamePopUp == null) {
                saveGamePopUp = new SaveGamePopUp(gameContainer, stateGame, SCALAR);
                saveGamePopUp.setAcceptingInput(false);
                saveGamePopUp.addPopUpListener(new PopUpListener() {
                    public void popUpClosed(Result result) {
                        if (result == Result.Cancel) {
                            showPopUp(MenuPopup.this);
                        }
                    }
                });

            }
            return saveGamePopUp;
        }

        private Button getButtonMenuEnd() throws SlickException {
            if (buttonMenuEnd == null) {
                buttonMenuEnd = createButton(gameContainer, 
                        createImage(buttonImageFilenames[BUTTON_END][INACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_END][ACTIVE], SCALAR), 
                        createImage(buttonImageFilenames[BUTTON_END][PRESSED], SCALAR), 0, 0);
                buttonMenuEnd.setLocation(getX() + getWidth() / 2 - buttonMenuEnd.getWidth() / 2, getButtonMenuSettings().getY()
                        + getButtonMenuSettings().getHeight() + POPUP_SPACING);
                buttonMenuEnd.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        setAcceptingInput(false);
                        stateGame.enterState(GameStateMainMenu.STATE_ID);
                    }
                });
            }
            return buttonMenuEnd;
        }

        @Override
        public void render(GUIContext context, Graphics g) throws SlickException {
            if (!isAcceptingInput()) {
                return;
            }

            super.render(container, g);
            
            getButtonMenuResumeGame().render(context, g);
            getButtonMenuSave().render(context, g);
            // getButtonMenuReplay().render(context, g);
            getButtonMenuLoad().render(context, g);
            getButtonMenuSettings().render(context, g);
            getButtonMenuEnd().render(context, g);
        }

    }

    private MenuPopup getMenuPopup() throws SlickException {
        if (menuPopup == null) {
            menuPopup = new MenuPopup(gameContainer);
        }
        return menuPopup;
    }    
    
    
    /******* Game Over PopUp starts here *******/
    
    class GameOverPopUp extends PopUp {

        private static final int BUTTON_NEWGAME = 0;
        private static final int BUTTON_EXIT = 1;
        //  private static final int BUTTON_REPLAY = 2;
        
        private String[][] buttonImageFilenames = {
            { GAMEOVER_NEWGAME, GAMEOVER_NEWGAME_PRESSED, GAMEOVER_NEWGAME_PRESSED },
            { GAMEOVER_EXIT, GAMEOVER_EXIT_PRESSED, GAMEOVER_EXIT_PRESSED }
        };

        private Button buttonNewGame;
        private Button buttonOurExit;
        private MiniReplay replay;
        //private MouseOverArea buttonReplay;

        public GameOverPopUp(GameContainer gameContainer, float scalar) throws SlickException {
            super(gameContainer, GAMEOVER_MENU, scalar, true);

            addComponent(getButtonEndNewGame());
            addComponent(getButtonExit());
            replay = new MiniReplay(container, getX() + getWidth() / 5, getY() + getHeight()/8, getWidth() * 3 / 5, stateGame.game, stateGame.worldUI);
            addComponent(replay);
        }

/*
         private MouseOverArea getButtonReplay() throws SlickException {
            if (buttonReplay == null) {
                buttonReplay = createButton(gameContainer,
                        createImage(buttonImageFilenames[BUTTON_REPLAY][INACTIVE]).getScaledCopy(scalar),
                        createImage(buttonImageFilenames[BUTTON_REPLAY][ACTIVE]).getScaledCopy(scalar),
                        createImage(buttonImageFilenames[BUTTON_REPLAY][PRESSED]).getScaledCopy(scalar), 400, getY() + GAMEOVER_HEIGHT - 90);
                buttonReplay.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        stateGame.enterState(GameStateReplay.STATE_ID);
                    }});
            }
            return buttonReplay;
        } */

        private Button getButtonEndNewGame() throws SlickException {
            if (this.buttonNewGame == null) {
                this.buttonNewGame = createButton(gameContainer,
                        createImage(buttonImageFilenames[BUTTON_NEWGAME][INACTIVE], scalar),
                        createImage(buttonImageFilenames[BUTTON_NEWGAME][ACTIVE], scalar),
                        createImage(buttonImageFilenames[BUTTON_NEWGAME][PRESSED], scalar), 400, getY() + GAMEOVER_HEIGHT - 140);
                this.buttonNewGame.setLocation(getX() + getWidth()/2 - this.buttonNewGame.getWidth()/2, getY() + getHeight() - 210);
                this.buttonNewGame.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        stateGame.enterState(GameStatePlayerMenu.STATE_ID);
                    }});
            }
            return this.buttonNewGame;
        }

        private Button getButtonExit() throws SlickException {
            if (buttonOurExit == null) {
                buttonOurExit = createButton(gameContainer,
                        createImage(buttonImageFilenames[BUTTON_EXIT][INACTIVE], scalar),
                        createImage(buttonImageFilenames[BUTTON_EXIT][ACTIVE], scalar),
                        createImage(buttonImageFilenames[BUTTON_EXIT][PRESSED], scalar), 400, getY() + GAMEOVER_HEIGHT - 40);
                buttonOurExit.setLocation(getX() + getWidth()/2 - buttonOurExit.getWidth()/2, getButtonEndNewGame().getY() + getButtonEndNewGame().getHeight() + 40);
                buttonOurExit.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        gameContainer.exit();
                    }});
            }
            return buttonOurExit;
        }

        @Override
        public void render(GUIContext context, Graphics g) throws SlickException {
            if (!isAcceptingInput()) {
                return;
            }

            super.render(container, g);
            replay.render(context, g);            
            g.setColor(Color.black);
            drawCenteredString(g, GAMEOVER_FONT, "Congratulations", SCREEN_WIDTH/2, getButtonEndNewGame().getY() - 75);
            String name = getTruncatedName(stateGame.game.getCurrentPlayer().getName(), GAMEOVER_FONT, getWidth() - 150);
            drawCenteredString(g, GAMEOVER_FONT, name, SCREEN_WIDTH/2, getButtonEndNewGame().getY() - 45);
            getButtonEndNewGame().render(context, g);
            getButtonExit().render(context, g);
//                getButtonReplay().render(context, g);  TODO - needed ?
        }
    }
    
    private GameOverPopUp getGameOverPopUp() throws SlickException {
        if (gameOverPopUp == null) {
            gameOverPopUp = new GameOverPopUp(gameContainer, SCALAR);
            gameOverPopUp.setAcceptingInput(false);
        }
        return gameOverPopUp;
    }

    // ** Movement Pop-up starts here **//
    class MovementUnitCountPopUp extends UnitCountPopUp {

        public MovementUnitCountPopUp(GameContainer container, GameUI stateGame, float scalar) throws SlickException {
            super(container, stateGame, scalar);
        }

        @Override
        public boolean doAction() {
            for (TerritoryUI t : getSources()) {
                if (!stateGame.game.canMoveUnits(t.getTerritory(), getTarget().getTerritory(), getUnitCount())) {
                    return false;
                }
            }
            for (TerritoryUI t : getSources()) {
                stateGame.game.moveUnits(t.getTerritory(), getTarget().getTerritory(), getUnitCount());
            }
            return true;
        }

        @Override
        public int maxUnits() {
            int max = 0;
            if (getTarget() != null) {
                for (TerritoryUI source : getSources()) {
                    max += stateGame.game.maxUnitsToMove(source.getTerritory(), getTarget().getTerritory());
                }
            }
            return max;
        }
    }
    
    private MovementUnitCountPopUp getMovementUnitCountPopUp() throws SlickException {
        if (movementUnitCountPopUp == null) {
            movementUnitCountPopUp = new MovementUnitCountPopUp(gameContainer, stateGame, SCALAR);
            movementUnitCountPopUp.setAcceptingInput(false);
        }
        return movementUnitCountPopUp;
    }

    class DistributeUnitCountPopUp extends UnitCountPopUp {
        
        public DistributeUnitCountPopUp(GameContainer container, GameUI stateGame, float scalar) throws SlickException {
            super(container, stateGame, scalar);
        }

        public void setLocation(TerritoryUI territory) {
            int newX = Math.min(SCREEN_WIDTH - getWidth(), Math.max(0, territory.getX() - getWidth() / 2));
            int newY = Math.max(0, territory.getY() - 60);
            super.setLocation(newX, newY);
        }

        @Override
        public boolean doAction() {
            for (TerritoryUI t : getSources()) {
                if (!stateGame.game.canDistributeFreeUnits(t.getTerritory(), getUnitCount())) {
                    return false;
                }
            }
            for (TerritoryUI t : getSources()) {
                stateGame.game.distributeFreeUnits(t.getTerritory(), getUnitCount());
            }
            return true;
        }

        @Override
        public int maxUnits() {
            if (stateGame.game.getCurrentTurnPhase() == TurnPhase.SetupUnitDeployment) {
                return stateGame.game.getCurrentPlayer().getRemainingDeployCount();
            }
            if (stateGame.game.getCurrentTurnPhase() == TurnPhase.TurnUnitDeployment
                    || stateGame.game.getCurrentTurnPhase() == TurnPhase.TurnConscriptUnitDeployment) {
                return stateGame.game.getCurrentPlayer().getFreeUnitCount();
            }
            return 0;
        }
    }

    private DistributeUnitCountPopUp getDistributeUnitCountPopUp() throws SlickException {
        if (distributeUnitCountPopUp == null) {
            distributeUnitCountPopUp = new DistributeUnitCountPopUp(gameContainer, stateGame, SCALAR);
            distributeUnitCountPopUp.setAcceptingInput(false);
        }
        return distributeUnitCountPopUp;
    }

    // ** Attack Pop-up starts here **//
    class AttackUnitCountPopUp extends UnitCountPopUp {

        public AttackUnitCountPopUp(GameContainer container, GameUI stateGame, float scalar) throws SlickException {
            super(container, stateGame, scalar);
        }

        @Override
        public boolean doAction() {
            ArrayList<Territory> sources = new ArrayList<Territory>();
            for (TerritoryUI s : getSources()) {
                sources.add(s.getTerritory());
            }

            ArrayList<Integer> numberOfUnits = new ArrayList<Integer>();
            numberOfUnits.add(getUnitCount());

            if (!stateGame.game.canAttack(sources, getTarget().getTerritory(), numberOfUnits)) {
                return false;
            }
            try {
                getAttackRecordPopUp().setRecord(sources, getTarget().getTerritory(), numberOfUnits);
                showPopUp(getAttackRecordPopUp());
            }
            catch (SlickException ex) {
                handleException(ex);
            }
            return true;
        }

        @Override
        public int maxUnits() {
            int maxUnits = 0;
            ArrayList<Territory> territories = new ArrayList<Territory>();
            for (TerritoryUI current : getSources()) {
                territories.add(current.getTerritory());
            }

            for (int unitsFromTerritory : stateGame.game.maxUnitsToAttack(territories, getTarget().getTerritory())) {
                maxUnits = maxUnits + unitsFromTerritory;
            }
            return maxUnits;
        }
    }

    private AttackUnitCountPopUp getAttackUnitCountPopUp() throws SlickException {
        if (attackUnitCountPopUp == null) {
            attackUnitCountPopUp = new AttackUnitCountPopUp(gameContainer, stateGame, SCALAR);
            attackUnitCountPopUp.setAcceptingInput(false);
        }
        return attackUnitCountPopUp;
    }

    class AttackRecordPopUp extends PopUp {

        private static final int ATTACKER = 0;
        private static final int MIDDLE = 1;
        private static final int DEFENDER = 2;
        private static final int X = 0;
        private static final int Y = 1;
        
        private int[][] column = new int[3][2];
        private List<Image> dice;
        private int[] diceX = new int[5];
        private int diceY;

        private ToggleButton buttonSingleAttack;
        private ToggleButton buttonFullAttack;
        private Button buttonRetreat;
        private Button buttonRollDie;
        private Image winImg;

        ArrayList<Territory> sources;
        Territory target;
        ArrayList<Integer> numberOfUnits;
        AttackRecord attackRecord;

        private String attacker;
        private String defender;
        private int attackingUnits;
        private int defendingUnits;
        private boolean singleAttack = true;
        boolean actionDone = false;


        public AttackRecordPopUp(GameContainer gameContainer, float scalar) throws SlickException {
            super(gameContainer, ATTACKRECORD_BACKGROUND, scalar, true);

            column[ATTACKER][X] = getX() + getWidth() / 5;
            column[ATTACKER][Y] = getY() + 3 * getHeight() / 8;
            column[MIDDLE][X] = getX() + getWidth() / 2;
            column[MIDDLE][Y] = getY();
            column[DEFENDER][X] = getX() + 4 * getWidth() / 5;
            column[DEFENDER][Y] = getY() + 3 * getHeight() / 8;
            diceY = getY() + getHeight() - getDiceImage(1).getHeight() - 10;
            diceX[0] = getX() + 10;
            diceX[1] = diceX[0] + getDiceImage(1).getWidth() - 5;
            diceX[2] = diceX[1] + getDiceImage(1).getWidth() - 5;
            diceX[4] = getX() + getWidth() - getDiceImage(1).getWidth() - 30;
            diceX[3] = diceX[4] - getDiceImage(1).getWidth() + 5;

            addComponent(getButtonSingleAttack());
            addComponent(getButtonFullAttack());
            addComponent(getButtonRetreat());
            addComponent(getButtonRollDie());
            
            setSingleAttack(true);
        }

        public void setRecord(ArrayList<Territory> sources, Territory target, ArrayList<Integer> numberOfUnits) {
            this.sources = sources;
            this.target = target;
            attacker = sources.get(0).getOwner().getName();
            attackingUnits = 0;
            for (int i : numberOfUnits) {
                attackingUnits += i;
            }
            defender = target.getOwner().getName();
            defendingUnits = target.getUnitCount();
            this.numberOfUnits = numberOfUnits;
            actionDone = false;
            setSingleAttack(true);
            attackRecord = null;

        }

        @Override
        @SuppressWarnings("hiding")
        public void render(GUIContext container, Graphics g) throws SlickException {
            if (!isAcceptingInput()) {
                return;
            }

            super.render(container, g);

            getButtonSingleAttack().render(container, g);
            getButtonFullAttack().render(container, g);
            getButtonRetreat().render(container, g);
            getButtonRollDie().render(container, g);

            g.setColor(Color.white);

            String name = getTruncatedName(attacker, ATTACKRECORD_FONT, 120);
            drawCenteredString(g, ATTACKRECORD_FONT, name, column[ATTACKER][X], column[ATTACKER][Y]);
            name = getTruncatedName(defender, ATTACKRECORD_FONT, 120);
            drawCenteredString(g, ATTACKRECORD_FONT, name, column[DEFENDER][X], column[DEFENDER][Y]);

            if (attackRecord != null) {
                drawCenteredString(g, ATTACKRECORD_FONT, String.valueOf(attackRecord.getAttackersAfterRound(attackRecord.getRounds() - 1)),
                        column[ATTACKER][X], column[ATTACKER][Y] + 15);
                drawCenteredString(g, ATTACKRECORD_FONT, String.valueOf(attackRecord.getDefendersAfterRound(attackRecord.getRounds() - 1)),
                        column[DEFENDER][X], column[DEFENDER][Y] + 15);
                
                if (actionDone) {
                    int winner = attackRecord.isSuccess() ? ATTACKER : DEFENDER;
                    getWinImg().draw(column[winner][X] - getWinImg().getWidth()/2, getY() + getHeight() - 80);
                }

                int d = 0;
                for (int roll : attackRecord.getAttackRolls(attackRecord.getRounds() - 1)) {
                    getDiceImage(roll).draw(diceX[d], diceY);
                    d++;
                }
                d = 3;
                for (int roll : attackRecord.getDefenceRolls(attackRecord.getRounds() - 1)) {
                    getDiceImage(roll).draw(diceX[d], diceY);
                    d++;
                }
            }
            else {
                // Initial units
                drawCenteredString(g, ATTACKRECORD_FONT, String.valueOf(attackingUnits), column[ATTACKER][X], column[ATTACKER][Y] + 15);
                drawCenteredString(g, ATTACKRECORD_FONT, String.valueOf(defendingUnits), column[DEFENDER][X], column[DEFENDER][Y] + 15);
            }
        }

        private ToggleButton getButtonSingleAttack() throws SlickException {
            if (buttonSingleAttack == null) {
                buttonSingleAttack = createToggleButton(gameContainer,
                        createImage(ATTACKRECORD_SINGLEATTACK_PRESSED, SCALAR),
                        createImage(ATTACKRECORD_SINGLEATTACK, SCALAR), 0, 0);
                buttonSingleAttack.setLocation(column[MIDDLE][X] - buttonSingleAttack.getWidth() / 2, column[MIDDLE][Y] + 15);
                buttonSingleAttack.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        setSingleAttack(true);
                    }
                });
            }
            return buttonSingleAttack;
        }

        private ToggleButton getButtonFullAttack() throws SlickException {
            if (buttonFullAttack == null) {
                buttonFullAttack = createToggleButton(gameContainer, 
                        createImage(ATTACKRECORD_FULLATTACK_PRESSED, SCALAR), 
                        createImage(ATTACKRECORD_FULLATTACK, SCALAR), 0, 0);
                buttonFullAttack.setLocation(column[MIDDLE][X] - buttonFullAttack.getWidth() / 2, getButtonSingleAttack().getY()
                        + getButtonSingleAttack().getHeight());
                buttonFullAttack.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        setSingleAttack(false);
                    }
                });
            }
            return buttonFullAttack;
        }

        private Button getButtonRetreat() throws SlickException {
            if (buttonRetreat == null) {
                buttonRetreat = createButton(gameContainer,
                        createImage(ATTACKRECORD_RETREAT, SCALAR), 
                        createImage(ATTACKRECORD_RETREAT_PRESSED, SCALAR), 0, 0);
                buttonRetreat.setLocation(column[MIDDLE][X] - buttonRetreat.getWidth() / 2, getButtonFullAttack().getY() + getButtonFullAttack().getHeight());
                buttonRetreat.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        if (!actionDone) {
                            if (attackRecord != null) {
                                attackRecord.withdraw(numberOfUnits);
                                close(Result.Ok);
                            }
                        }
                    }
                });
            }
            return buttonRetreat;
        }

        private Button getButtonRollDie() throws SlickException {
            if (buttonRollDie == null) {
                buttonRollDie = createButton(gameContainer,
                        createImage(ATTACKRECORD_ROLLDIE, scalar), 
                        createImage(ATTACKRECORD_ROLLDIE_PRESSED, scalar), 0, 0);
                buttonRollDie.setLocation(column[MIDDLE][X] - buttonRollDie.getWidth() / 2, getHeight() + getY() - buttonRollDie.getHeight() - 7);
                buttonRollDie.addListener(new ComponentListener() {
                    public void componentActivated(AbstractComponent source) {
                        if (!actionDone) {
                            handleAttack();
                            consume();
                        }
                    }
                });
            }
            return buttonRollDie;
        }

        void setSingleAttack(boolean single) {
            singleAttack = single;

            buttonSingleAttack.setValue(singleAttack);
            buttonFullAttack.setValue(!singleAttack);
        }

        void handleAttack() {
            if (singleAttack) {
                attackRecord = stateGame.game.attack(sources, target, numberOfUnits, true, false);
                attackingUnits = attackRecord.getAttackersAfterRound(0);
                defendingUnits = attackRecord.getDefendersAfterRound(0);
                if (attackRecord.isWithdrawn()) {
                    numberOfUnits = new ArrayList<Integer>(attackRecord.returnedUnits());
                    actionDone = !stateGame.game.canAttack(sources, target, numberOfUnits);
                }
                else {
                    actionDone = true;
                }
            }
            else {
                attackRecord = stateGame.game.attack(sources, target, numberOfUnits, false, false);
                actionDone = true;
            }
        }

        private Image getWinImg() throws SlickException {
            if (winImg == null) {
                winImg = createImage(ATTACKRECORD_WIN, scalar); // TODO - need a better image
            }
            return winImg;
        }

        private Image getDiceImage(int roll) throws SlickException {
            if (dice == null) {
                String[] diceImageFiles = { DICE_1, DICE_2, DICE_3, DICE_4, DICE_5, DICE_6 };
                dice = new ArrayList<Image>();
                for (int i = 0; i < 6; i++) {
                    dice.add(createImage(diceImageFiles[i], scalar / 6));
                }
            }
            return dice.get(roll - 1);
        }

        @Override
        @SuppressWarnings("hiding")
        public void mousePressed(int button, int x, int y) {
            if (isAcceptingInput() && actionDone) {
                // Close on a click anywhere on screen
                close(Result.Ok);
            }
        }

    }

    AttackRecordPopUp getAttackRecordPopUp() throws SlickException {
        if (attackRecordPopUp == null) {
            attackRecordPopUp = new AttackRecordPopUp(gameContainer, SCALAR);
            attackRecordPopUp.setAcceptingInput(false);
        }
        return attackRecordPopUp;
    }
    
    void showPopUp(PopUp newPopUp) {
        if (isPopUpVisible()) {
            currentPopUp.setAcceptingInput(false);
        }
        currentPopUp = newPopUp;
        currentPopUp.setAcceptingInput(true);
    }
    
    boolean isPopUpVisible() {
    	return (currentPopUp != null) && currentPopUp.isAcceptingInput();
    }

}