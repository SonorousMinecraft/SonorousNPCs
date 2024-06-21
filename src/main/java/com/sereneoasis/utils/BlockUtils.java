package com.sereneoasis.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class BlockUtils {
    
    public static Set<Block> getBlocksAroundPoint(Location location, int diameter){
        Set<Block>blocks = new HashSet<>();
        for (double x = -diameter/2; x < diameter/2 ; x++){
            for (double y = -diameter/2; y < diameter/2 ; y++){
                for (double z = -diameter/2; z < diameter/2 ; z++){
                    blocks.add(location.clone().add(x,y,z).getBlock());
                }
            }
        }
        return blocks;
    }

    public static Set<Block> getCircleAroundPoint(Location location, int diameter){
        Set<Block>blocks = new HashSet<>();
        for (double x = -diameter/2; x < diameter/2 ; x++){
            for (double z = -diameter/2; z < diameter/2 ; z++){
                    blocks.add(location.clone().add(x,0,z).getBlock());
                }
        }
        return blocks;
    }

    public static Set<Block> getAirSphereBlocksAroundPoint(Location location, int diameter){
        Set<Block>blocks = new HashSet<>();
        for (double x = -diameter/2; x < diameter/2 ; x++){
            for (double y = -diameter/2; y < diameter/2 ; y++){
                if ( ((x * x) + (y*y)) < ((double) diameter /2) * ((double) diameter /2)) {

                    for (double z = -diameter / 2; z < diameter / 2; z++) {
                        if ( ((z * z) + (y*y)) < ((double) diameter /2) * ((double) diameter /2)) {
                            if ( ((x * x) + (z*z)) < ((double) diameter /2) * ((double) diameter /2)) {

                                Block b = location.clone().add(x, y, z).getBlock();
                                if (b.getType().isAir()) {
                                    blocks.add(b);
                                }
                            }
                        }
                    }
                }
            }
        }
        return blocks;
    }

    public static Set<Block> getAirBlocksAroundPoint(Location location, int diameter){
        Set<Block>blocks = new HashSet<>();
        for (double x = -diameter/2; x < diameter/2 ; x++){
            for (double y = -diameter/2; y < diameter/2 ; y++){
                for (double z = -diameter/2; z < diameter/2 ; z++){
                    Block b = location.clone().add(x,y,z).getBlock();
                    if (b.getType().isAir()) {
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }

    public static Set<Block> getAirCircleAroundPoint(Location location, int diameter){
        Set<Block>blocks = new HashSet<>();
        for (double x = (double) -diameter /2; x < (double) diameter /2 ; x++){
            for (double z = (double) -diameter /2; z < (double) diameter /2 ; z++){
                if ( ((x * x) + (z*z)) < ((double) diameter /2) * ((double) diameter /2)) {
                    Block b = location.clone().add(x, 0, z).getBlock();
                    if (b.getType().isAir()) {
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }

    public static Set<Block> getAirHollowCircleAroundPoint(Location location, int diameter){
        Set<Block>blocks = new HashSet<>();

        for (double x = (double) -diameter /2 -1; x < (double) diameter /2 + 1 ; x++){
            for (double z = (double) -diameter /2 -1; z < (double) diameter /2 + 1 ; z++){
                if ( ((x * x) + (z*z)) < ((double) diameter /2) * ((double) diameter /2) &&
                        (((x+1) * (x+1)) + ((z+1)*(z+1))) > ((double) diameter /2) * ((double) diameter /2)){

                    Block b = location.clone().add(x, 0, z).getBlock();

                    if (b.getType().isAir()) {
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }
}
