package mapGenerator.run;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import mapGenerator.tileset.RuleSet;
import mapGenerator.tileset.Tile;
import mapGenerator.tileset.TileType;
import mapGenerator.tileset.Tiles;

public class Generator {

    static Tile[][] tileMatrix = new Tile[50][50];
    static Stack<int[]> changed = new Stack<>();
    static final ArrayList<Tiles> values = new ArrayList<>(List.of(Tiles.values()));

    /*public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                tileMatrix[i][j] = new Tile();
            }
        }

        while (waveFunctionCollapse());
        printEntropy();
    }*/

    public static Tile[][] run(){
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                tileMatrix[i][j] = new Tile();
            }
        }

        while (waveFunctionCollapse());
        
        return tileMatrix;
    }
    
    public static void printMatrix() {
        for (Tile[] row : tileMatrix) {
            for (Tile tile : row) {
                System.out.print(tile.toString() + " ");
            }
            System.out.println("");
        }
    }

    public static void printEntropy() {
        for (Tile[] row : tileMatrix) {
            for (Tile i : row) {
                System.out.print(i.entropy + " ");
            }
            System.out.println("");
        }
    }

    public static boolean waveFunctionCollapse() {
        ArrayList<int[]> lowestEntropies = getLowestEntropies();
        if (lowestEntropies.isEmpty()) {
            return false;
        }
        Random random = new Random();
        
        int rand = random.nextInt(lowestEntropies.size());
        int[] coords = lowestEntropies.get(rand);

        Tile currTile = tileMatrix[coords[1]][coords[0]];
        ArrayList<Tiles> currGoods = currTile.goodTiles;

        int sumWeight = 0;
        for (Tiles t : currGoods) {
            sumWeight += RuleSet.getModifier(t);
        }

        rand =  random.nextInt(sumWeight);
        int idx = 0;
        while ((rand -= RuleSet.getModifier(currGoods.get(idx))) > 0) {
            idx++;
        }

        currTile.tile = currGoods.get(idx);

        Stack<int[]> stack = new Stack<>();
        stack.push(coords);
        changed.add(coords);
        updateEntropy(coords[0], coords[1]);
        while (!stack.isEmpty()) {
            int[] pair = stack.pop();

            //top
            int[] currNeighbour = new int[]{pair[0], pair[1] - 1};
            boolean updated = updateEntropy(currNeighbour[0], currNeighbour[1]);
            if (updated) {
                stack.add(currNeighbour);
                changed.add(currNeighbour);
            }
            
            //bottom
            currNeighbour = new int[]{pair[0], pair[1] + 1};
            updated = updateEntropy(currNeighbour[0], currNeighbour[1]);
            if (updated) {
                stack.add(currNeighbour);
                changed.add(currNeighbour);
            }

            //left
            currNeighbour = new int[]{pair[0] - 1, pair[1]};
            updated = updateEntropy(currNeighbour[0], currNeighbour[1]);
            if (updated) {
                stack.add(currNeighbour);
                changed.add(currNeighbour);
            }

            //right
            currNeighbour = new int[]{pair[0] + 1, pair[1]};
            updated = updateEntropy(currNeighbour[0], currNeighbour[1]);
            if (updated) {
                stack.add(currNeighbour);
                changed.add(currNeighbour);
            }
        }

        return true;
    }

    private static ArrayList<int[]> getLowestEntropies() {
        ArrayList<int[]> list = new ArrayList<>();
        int lowestEntropy = values.size();
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 50; x++) {
                int tileEntropy = tileMatrix[y][x].entropy;
                if (tileEntropy > 0) {
                    if (tileEntropy < lowestEntropy) {
                        list.clear();
                        lowestEntropy = tileEntropy;
                    }
                    if (tileEntropy == lowestEntropy) {
                        list.add(new int[]{x, y});
                    }
                }
            }
        }

        return list;
    }

    private static boolean updateEntropy(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        if (tileMatrix[y][x].tile != null) {
            if (tileMatrix[y][x].entropy != 0) {
                tileMatrix[y][x].entropy = 0;
                tileMatrix[y][x].goodTiles.clear();
                return true;
            }
        } else {
            ArrayList<Tiles> newGoodTiles = getGoodTiles(x, y);
            int newEntropy = newGoodTiles.size();
            if (newEntropy == 0) {
                return false;
            }
            if (tileMatrix[y][x].entropy != newEntropy) {
                tileMatrix[y][x].entropy = newEntropy;
                tileMatrix[y][x].goodTiles = newGoodTiles;
                return true;
            }
        }
        return false;
    }

    static boolean inBounds(int i, int j) {
        return i >= 0 && i < 50 && j >= 0 && j < 50;
    }

    /**
     * Returns the possible tiles for the coordinates
     *
     * @param i x coordinate of the tile
     * @param j y coordinate of the tile
     * @return an ArrayList of the possible tiles for the coordinates
     */
    private static ArrayList<Tiles> getGoodTiles(int i, int j) {
        HashSet<Tiles> topTiles = sideGoodTiles(i,j,"top");
        HashSet<Tiles> bottomTiles = sideGoodTiles(i,j,"bottom");
        HashSet<Tiles> leftTiles = sideGoodTiles(i,j,"left");
        HashSet<Tiles> rightTiles = sideGoodTiles(i,j,"right");
        
        ArrayList<Tiles> goodTiles = new ArrayList<>(values);
        if (!topTiles.isEmpty()) {
            goodTiles.retainAll(topTiles);
        }
        if (!bottomTiles.isEmpty()) {
            goodTiles.retainAll(bottomTiles);
        }
        if (!leftTiles.isEmpty()) {
            goodTiles.retainAll(leftTiles);
        }
        if (!rightTiles.isEmpty()) {
            goodTiles.retainAll(rightTiles);
        }
        return goodTiles;
    }
    
    private static HashSet<Tiles> sideGoodTiles(int x, int y, String side){
        HashSet<Tiles> goodTiles = new HashSet<>();
        String opposite = switch(side){
            case "top" -> "bottom";
            case "bottom" -> "top";
            case "left" -> "right";
            case "right" -> "left";
            default -> throw new IllegalArgumentException("Unknown side");
        };
        if(side.equals( "top")){
            y -= 1;
        } else if(side.equals( "bottom")){
            y += 1;
        } else if(side.equals( "left")){
            x -= 1;
        } else if(side.equals( "right")){
            x += 1;
        }
        
        if (Generator.inBounds(x, y)) {
            if (tileMatrix[y][x].tile == null) {
                for (Tiles tiles : tileMatrix[y][x].goodTiles) {
                    goodTiles.addAll(Generator.searchConnecting(side, tiles.getSide(opposite)));
                }
            } else {
                goodTiles.addAll(Generator.searchConnecting(side, tileMatrix[y][x].tile.getSide(opposite)));
            }
        }
        
        return goodTiles;
    }

    static ArrayList<Tiles> searchConnecting(String side, TileType tt) {
        ArrayList<Tiles> connecting = new ArrayList<>();

        for (Tiles currTile : values) {
            if (currTile.getSide(side).equals(tt)) {
                connecting.add(currTile);
            }
        }

        return connecting;
    }
}