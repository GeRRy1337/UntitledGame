package mapGenerator.tileset;

import mapGenerator.tileset.*;
import java.util.ArrayList;
import java.util.List;

public class Tile {
    public ArrayList<Tiles> goodTiles = new ArrayList<> (List.of(Tiles.values()));
    public Tiles tile;
    public int entropy = Tiles.values().length;
    
}
