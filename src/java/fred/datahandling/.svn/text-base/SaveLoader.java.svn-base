package fred.datahandling;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import fred.graphics.TerritoryUI;
import fred.middle.Middle;
import fred.middle.Replay;


public class SaveLoader {
	
	private MapLoader map = new MapLoader();
	
	public Middle load(String inputPath, String mapPath) {
		Middle result = load(inputPath);
		map.load(mapPath, result.getType());
		return result;		
	}
	
	public Middle load(String inputPath) {
        if (inputPath == null) {
            throw new NullPointerException();
        }
        try {
            InputStream iStream = new FileInputStream(inputPath);
            Middle result;
            try {
                result = load(iStream);
            }
            finally {
                iStream.close();
            }
            return result;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Invalid file", ex);
        }
	 }
	
	public Middle load(InputStream iStream) {
		Replay data;
        try {
	        ObjectInputStream oi = new ObjectInputStream(iStream);
	        data = (Replay) oi.readObject();
	        oi.close();
        } catch (IOException ex) {
        	throw new IllegalStateException(ex.getMessage());
        } catch (ClassNotFoundException ey) {
        	throw new IllegalStateException("No SaveData class");
        }
        
		return new Middle(data);
	}
	
	public void save(String outputPath, Replay replay) {
        if (outputPath == null) {
            throw new NullPointerException();
        }
        try {
            OutputStream uStream = new FileOutputStream(outputPath);
            try {
                save(uStream, replay);
            }
            finally {
                uStream.close();
            }
            return;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Invalid file", ex);
        }
	 }
		
	public void save(OutputStream weAllStream4iStream, Replay data) {
		try {
	        ObjectOutputStream oo = new ObjectOutputStream(weAllStream4iStream);
	        oo.writeObject(data);
	        oo.close();
	        return;
        } catch (IOException ex) {
        	throw new IllegalStateException(ex.getMessage());
        }
	}
	
	public static String getValidFilename(String filename) {
	    if (filename == null) {
	        throw new NullPointerException();
	    }
	    String result = filename.replaceAll("\\W*", "");
	    return result.length() > 0 ? result : null;
	}
	
	public List<TerritoryUI> getTerritoryUIs() {
        return map.getTerritoryUIs();
    }
}
