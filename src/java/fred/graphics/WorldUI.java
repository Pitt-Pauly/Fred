package fred.graphics;

import static fred.graphics.GameUI.INFOPANE_HEIGHT;
import static fred.graphics.GameUI.SCREEN_HEIGHT;
import static fred.graphics.GameUI.SCREEN_WIDTH;
import static fred.graphics.ImagePaths.WORLD_DATA_MAP;
import static fred.graphics.ImagePaths.WORLD_MAP;
import static fred.graphics.UIUtils.handleException;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import fred.model.GameInfo;
import fred.model.Player;

public class WorldUI extends AbstractComponent {

    private final List<TerritoryUI> nList;
    Image mapImage;
    Image dataImage;

    private final int x, y, width, height;

    public WorldUI(GUIContext container, List<TerritoryUI> nodes) throws SlickException {
        super(container);
        if (nodes == null) {
            throw new NullPointerException();
        }
        nList = nodes;
        x = 0;
        y = 0;
        height = SCREEN_HEIGHT; // but getHeight needs to return this - INFOPANE_HEIGHT
        width = SCREEN_WIDTH;
        rescale();
    }
    
    //For mini-replay
    public WorldUI(WorldUI clone, int x, int y, int width) throws SlickException {
        super(clone.container);
        List<TerritoryUI>nodes = new ArrayList<TerritoryUI> ();
        for (TerritoryUI node: clone.getNodes()) {
        	nodes.add(new TerritoryUI(node));
        }
        nList = nodes;
        this.x = x;
		this.y = y;
		this.width = width;
		this.height = (width * SCREEN_HEIGHT / SCREEN_WIDTH);
        rescale();
    }

    public List<TerritoryUI> getSelectedSources() {
        ArrayList<TerritoryUI> territories = new ArrayList<TerritoryUI>();
        for (TerritoryUI n : nList) {
            if (n.isSelectedAsSource()) {
                territories.add(n);
            }
        }
        return territories;
    }

    public TerritoryUI getSelectedTarget() {
        for (TerritoryUI n : nList) {
            if (n.isSelectedAsTarget()) {
                return n;
            }
        }
        return null;
    }

    Image getMapImage() throws SlickException {
        if (mapImage == null) {
            mapImage = new Image(WORLD_MAP);
        }
        return mapImage;
    }

    Image getDataMapImage() throws SlickException {
        if (dataImage == null) {
            try {
                dataImage = new Image(WORLD_DATA_MAP);
            }
            catch (RuntimeException ex) {
                handleException(ex);
            }
        }
        return dataImage;
    }

    public boolean selectTargetOn(int mX, int mY, Player player, boolean onlySamePlayer, boolean onlyDifferentPlayer) {
        if (!getSelectedSources().isEmpty()) {
            deselectCurrentTarget();
            TerritoryUI n = getTerritoryUIOn(mX, mY);
            if (n != null && ((n.getTerritory().getOwner() == player) || !onlySamePlayer) && ((n.getTerritory().getOwner() != player) || !onlyDifferentPlayer)) {
                n.setSelectedAsTarget();
                return true;
            }
        }
        return false;
    }

    public TerritoryUI selectSourceOn(int mX, int mY, Player thisPlayer, boolean controlDown) {
        TerritoryUI n = getTerritoryUIOn(mX, mY);
        if (n != null && n.getTerritory().getOwner() == thisPlayer) {
            if (controlDown && n.isSelectedAsSource()) {
                n.setUnselected();
                return null;
            }
            n.setSelectedAsSource();
            return n;
        }
        return null;
    }

    // Is encapsulation worth it? Audience should decide.
    List<TerritoryUI> getNodes() {
        return nList;
    }

    private TerritoryUI getTerritoryUIOn(int mX, int mY) {
        if (mX < 0 || mX > SCREEN_WIDTH) {
            return null;
        }
        if (mY < 0 || mY > SCREEN_HEIGHT - INFOPANE_HEIGHT) {
            return null;
        }

        try {
            Color pixelColour = getColour(mX, mY);
            if (pixelColour != null) {
                for (TerritoryUI n : nList) {
                    if (n.getColourRGB().equals(pixelColour)) {
                        return n;
                    }
                }
            }
        }
        catch (SlickException e) {
            handleException(e);
        }
        for (TerritoryUI n : nList) {
            if (n.inBoundaries(mX, mY)) {
                return n;
            }
        }
        return null;
    }

    @SuppressWarnings("hiding")
    private Color getColour(int x, int y) throws SlickException {
        Image data = getDataMapImage();
        if (data == null) {
            return null;
        }
        return getDataMapImage().getColor(x * data.getWidth() / SCREEN_WIDTH, y * data.getHeight() / (SCREEN_HEIGHT));
    }

    public void deselectAllNodes() {
        for (TerritoryUI n : nList) {
            n.setUnselected();
        }
    }

    private void deselectCurrentTarget() {
        for (TerritoryUI n : nList) {
            if (n.isSelectedAsTarget()) {
                n.setUnselected();
            }
        }
    }
    
    @SuppressWarnings("hiding")
    public void selectHoveredOn(int x, int y) {
        TerritoryUI hover = getTerritoryUIOn(x, y);

        for (TerritoryUI n : getNodes()) {
            n.setHoveredOver(n == hover);
        }
    }

    private void rescale() throws SlickException {
        for (TerritoryUI n : nList) {
            n.setX(x + ((n.mapx * width) / getMapImage().getWidth()));
            n.setY(y + ((n.mapy * height) / getMapImage().getHeight()));
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
        return height * SCREEN_HEIGHT / (SCREEN_HEIGHT + INFOPANE_HEIGHT); // Hacky
    }

    @Override
    public void render(GUIContext context, Graphics g) throws SlickException {
        getMapImage().draw(x, y, width, height);
        
        int scale = x == 0 ? 1 : 3;

        for (TerritoryUI n : nList) {
            if (!n.isHoveredOver() && !n.isSelectedAsSource() && !n.isSelectedAsTarget()) {
                n.draw(g, scale);
            }
        }

        for (TerritoryUI n : nList) {
            if (n.isSelectedAsSource() || n.isSelectedAsTarget()) {
                if (!n.isHoveredOver()) {
                	n.draw(g, scale);
                }
            }
        }

        for (TerritoryUI n : nList) {
            if (n.isHoveredOver()) {
            	n.draw(g, scale);
            }
        }
    }

    @Override
    public void setLocation(int x, int y) {
        // Do nothing
    }

    public void setGameInfo(GameInfo game) {
        for (TerritoryUI n : nList) {
            n.setGame(game);
        }
    }
    
    @Override
    public void setFocus(boolean focus) {
        // Cannot be focused
    }
}
