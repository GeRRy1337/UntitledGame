package mapGenerator.tileset;

import javax.swing.ImageIcon;

public class Images {
    public static ImageIcon getImageIcon(Tiles tile){
        String temp[] = tile.name().split("_");
        String name = "";
        for(String s:temp){
            name += s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
        }
        return new ImageIcon("Images/"+name+".png");
    }
    
    public static String getPath(Tiles tile){
        String temp[] = tile.name().split("_");
        String name = "";
        for(String s:temp){
            name += s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
        }
        return ("Images/"+name+".png");
    }
    
}
