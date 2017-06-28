package fred.datahandling;

import fred.model.Game;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.io.Serializable;

public class PackedGame implements Serializable {
	byte[] stuff;
	
	private static final long serialVersionUID = 1L;

	public PackedGame(Game game) {
	    if (game == null) {
	        throw new NullPointerException();
	    }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
	        ObjectOutputStream oo = new ObjectOutputStream(stream);
	        oo.writeObject(game);
	        oo.close();
        } catch (IOException ex) {
        	throw new IllegalStateException(ex.getMessage());
        }
    
        stuff = stream.toByteArray();
	}
	
	public Game unPack() {
		ByteArrayInputStream stream = new ByteArrayInputStream(stuff);
		Game game;
        try {
	        ObjectInputStream oi = new ObjectInputStream(stream);
	        game = (Game) oi.readObject();
	        oi.close();
        } catch (IOException ex) {
        	throw new IllegalStateException(ex.getMessage());
        } catch (ClassNotFoundException ey) {
        	throw new IllegalStateException("No game class");
        }
		return game;
	}
}
