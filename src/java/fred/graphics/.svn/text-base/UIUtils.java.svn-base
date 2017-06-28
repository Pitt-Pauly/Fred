package fred.graphics;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class UIUtils {

    public static TextField createTextField(GUIContext container, Font font, int centreX, int centreY, int width, int height) {
        // TODO Pitt - to disable the font change, comment out font changes here
//        TextField result = new TextField(container, container.getDefaultFont(), centreX - width / 2, centreY - height / 2, width, height);
        TextField result = new TextField(container, font, centreX - width / 2, centreY - height / 2, width, height);
        return result;
    }

    public static ToggleButton createToggleButton(GUIContext container, Image trueImage, Image falseImage, int centreX, int centreY) {
        return createToggleButton(container, trueImage, falseImage, trueImage, falseImage, trueImage, falseImage, centreX, centreY);
    }

    public static ToggleButton createToggleButton(GUIContext container, Image normalTrueImage, Image normalFalseImage, Image mouseOverTrueImage,
            Image mouseOverFalseImage, Image mouseDownTrueImage, Image mouseDownFalseImage, int centreX, int centreY) {
        ToggleButton result = new ToggleButton(container, normalTrueImage, normalFalseImage, centreX - normalTrueImage.getWidth() / 2, centreY
                - normalTrueImage.getHeight() / 2);
        result.setMouseOverImages(mouseOverTrueImage, mouseOverFalseImage);
        result.setMouseDownImages(mouseDownTrueImage, mouseDownFalseImage);
        return result;
    }

    public static Image createImage(String imageFilename) throws SlickException {
        return new Image(imageFilename);
    }

    public static Image createImage(String imageFilename, float scalar) throws SlickException {
        return new Image(imageFilename).getScaledCopy(scalar);
    }

    public static Button createButton(GUIContext container, Image normalImage, Image mouseOverImage, Image mouseDownImage, Image disabledImage,
            Image hiddenImage, int centreX, int centreY) {
        Button result = new Button(container, normalImage, disabledImage, hiddenImage, 
                centreX - normalImage.getWidth() / 2, centreY - normalImage.getHeight() / 2);
        result.setMouseOverImage(mouseOverImage);
        result.setMouseDownImage(mouseDownImage);
        return result;
    }

    public static Button createButton(GUIContext container, Image normalImage, Image mouseOverImage, Image mouseDownImage, int centreX, int centreY) {
        Button result = new Button(container, normalImage, centreX - normalImage.getWidth() / 2, centreY - normalImage.getHeight() / 2);
        result.setMouseOverImage(mouseOverImage);
        result.setMouseDownImage(mouseDownImage);
        return result;
    }

    public static Button createButton(GUIContext container, Image normalImage, Image mouseDownImage, int centreX, int centreY) {
        Button result = new Button(container, normalImage, centreX - normalImage.getWidth() / 2, centreY - normalImage.getHeight() / 2);
        result.setMouseOverImage(normalImage);
        result.setMouseDownImage(mouseDownImage);
        return result;
    }

    public static Button createButton(GUIContext container, Image normalImage, int centreX, int centreY) {
        return new Button(container, normalImage, centreX - normalImage.getWidth() / 2, centreY - normalImage.getHeight() / 2);
    }

    public static void handleException(Exception ex) {
        System.err.println(ex);
        ex.printStackTrace();
    }

    public static void drawCenteredString(Graphics g, String text, int x, int y) {
        int width = (g.getFont().getWidth(text))/2;
        int height = (g.getFont().getHeight(text))/2;
        g.drawString(text, x - width, y - height);
    }

    public static void drawCenteredString(Graphics g, UnicodeFont font, String text, int x, int y) {
        // TODO Pitt - to disable the font change, comment out font changes here

        Font oldFont = g.getFont();
        g.setFont(font);
        drawCenteredString(g, text, x, y);
        g.setFont(oldFont);
    }

    public static void drawString(Graphics g, UnicodeFont font, String text, int x, int y) {
        // TODO Pitt - to disable the font change, comment out font changes here
        Font oldFont = g.getFont();
        g.setFont(font);
        g.drawString(text, x, y);
        g.setFont(oldFont);
    }

    public static void drawCenteredStrings(Graphics g, String[] text, int x, int y) {
        int height = g.getFont().getHeight(text[0]);
        int totalHeight = height * text.length;
        int currentY = y - totalHeight / 2 + height / 2;
        for (String word: text) {
            int width = g.getFont().getWidth(word);
            g.drawString(word, x - width / 2, currentY);
            currentY += height;
        }
    }
    
    public static void drawCenteredStrings(Graphics g, UnicodeFont font, String[] words, int x, int y) {
        // TODO Pitt - to disable the font change, comment out font changes here
        Font oldFont = g.getFont();
        g.setFont(font);
        drawCenteredStrings(g, words, x, y);
        g.setFont(oldFont);
    }

    @SuppressWarnings("unchecked")
    public static UnicodeFont createFont(String name, boolean bold, boolean italic, int size) {
        java.awt.Font awtFont = new java.awt.Font(name, java.awt.Font.PLAIN, size);
        UnicodeFont font = new UnicodeFont(awtFont, size, bold, italic);
        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        try {
            font.loadGlyphs();
        }
        catch (SlickException e) {
            handleException(e);
        }
        return font;
    }

    public static String getTruncatedName(String name, Font font, int maxLength) {
        if (font.getWidth(name) < maxLength) {
            return name;
        }
        String truncatedName = name;
        while (truncatedName.length() > 0) {
            if (font.getWidth(truncatedName + "...") < maxLength) {
                return truncatedName + "...";
            }
            truncatedName = truncatedName.substring(0, truncatedName.length() - 1);
        }
        return "";
    }
    


}
