package fred.datahandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.newdawn.slick.Color;

import fred.graphics.TerritoryUI;
import fred.model.Game;
import fred.model.Territory;
import fred.model.TerritoryGroup;
import fred.model.World;

public class MapLoader {

    private List<TerritoryUI> territoryUIs;
    
    public World load(Reader inputdata, Game.Type gameType) {
        if (inputdata == null) {
            throw new NullPointerException();
        }

        BufferedReader reader = new BufferedReader(inputdata);

        Map<String, String[]> mapNodes = new TreeMap<String, String[]>();
        Map<String, Territory> mapTerritories = new TreeMap<String, Territory>();
        TerritoryGroup currentGroup = null;
        List<TerritoryGroup> territoryGroups = new ArrayList<TerritoryGroup>();
        territoryUIs = new ArrayList<TerritoryUI>();

        // Read File Line By Line
        String line;
        String mapName = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.length() != 0 && !line.startsWith("#")) {
                    if (mapName == null) {
                        mapName = line;
                    }
                    else if (line.startsWith("G|")) {
                        // one line represents one territory group - all
                        // following countries part of territory group
                        // 'G' Name BonusValue
                        // Ex: G|North America!2
                        String[] args = line.split("\\Q|\\E");
                        if (args.length < 3) {
                            throw new IllegalArgumentException("Input data contains illegal data.");
                        }
                        String name = args[1];
                        int bonusValue = Integer.parseInt(args[2]);
                        currentGroup = new TerritoryGroup(name, bonusValue);
                        territoryGroups.add(currentGroup);
                    }
                    else if (line.startsWith("C|")) {

                        // one line represents one territory
                        // 'C' ID Name PositionX PositionY <Nodes>
                        // Ex: C|0|Alaska|10|20|1|2
                        String[] args = line.split("\\Q|\\E");
                        if (args.length < 5) {
                            throw new IllegalArgumentException("Input data contains illegal data.");
                        }

                        // _x is used to set the different parameters
                        // dynamic and easy adding/removing map parameters
                        int _x = 1;

                        String id = args[_x++];
                        String name = args[_x++];
                        int pos_x = Integer.parseInt(args[_x++]);
                        int pos_y = Integer.parseInt(args[_x++]);


                        int hexRGB = Integer.parseInt(args[_x++], 16);
                        Color RGB = new Color(hexRGB);


                        String[] nodes = new String[args.length - _x];
                        System.arraycopy(args, _x, nodes, 0, nodes.length);
                        mapNodes.put(id, nodes);

                        Territory current = Territory.factory(name, gameType);
                        mapTerritories.put(id, current);

                        if (currentGroup != null) {
                            currentGroup.getTerritories().add(current);
                        }
                        
                        addTerritoryUI(name, pos_x, pos_y, RGB);
                    }
                    else {
                        throw new IllegalArgumentException("Input data contains illegal data.");
                    }
                }
            }

            if (mapTerritories.size() == 0) {
                throw new IllegalArgumentException("Input data contains no territories.");
            }

            // Compile the data
            for (String currentId : mapTerritories.keySet()) {
                Territory currentTerritory = mapTerritories.get(currentId);
                String[] neighbourIds = mapNodes.get(currentId);

                for (String neighbourId : neighbourIds) {
                    if (!mapTerritories.containsKey(neighbourId)) {
                        throw new IllegalArgumentException("Input data contains illegal data.");
                    }
                    currentTerritory.addNeighbour(mapTerritories.get(neighbourId));
                }
            }
            
            return new World(new ArrayList<Territory>(mapTerritories.values()), territoryGroups);
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        catch (NumberFormatException formate) {
            throw new IllegalArgumentException("Input data contains illegal data.");
        }

        throw new IllegalArgumentException("Input data contains illegal data.");
    }

    protected void addTerritoryUI(String name, int pos_x, int pos_y, Color RGB) {
        territoryUIs.add(new TerritoryUI(name, pos_x, pos_y, RGB));
    }

    public World load(String inputPath,Game.Type gameType) {
        if (inputPath == null) {
            throw new NullPointerException();
        }
        try {
            Reader reader = new FileReader(new File(inputPath));
            World result;
            try {
                result = load(reader, gameType);
            }
            finally {
                reader.close();
            }
            return result;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Invalid file", ex);
        }
    }

    public List<TerritoryUI> getTerritoryUIs() {
        if (territoryUIs == null) {
            throw new IllegalStateException("Should call load() first");
        }
        return territoryUIs;
    }
}
