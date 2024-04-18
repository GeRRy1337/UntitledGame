package game.model;

import java.util.HashMap;
import java.util.Map;
import mapGenerator.run.Generator;
import mapGenerator.tileset.Tile;

public class Model {
    private Map<Tuple<Integer, Integer>, Tile[][]> chunks;
    private Tuple<Integer, Integer> currentChunk;
    
    public Model(){
        chunks = new HashMap<>();
        currentChunk = new Tuple(0,0);
        chunks.put(currentChunk, Generator.run());
    }
    
    public Tile[][] getCurrentTiles(){
        //TODO: dont give out private values, deepcopy
        return chunks.get(currentChunk);
    }
}
