package com.sereneoasis.npc.storyline;

import com.sereneoasis.config.ConfigFile;
import net.minecraft.core.BlockPos;
import org.bukkit.configuration.file.FileConfiguration;

public class StorylineLocations {

    private FileConfiguration fileConfiguration;

    public StorylineLocations() {
        fileConfiguration = new ConfigFile("StorylineLocations").getConfig();
    }

    public BlockPos getBlockPos(String name) {
        int x = fileConfiguration.getInt(name + ".X");
        int y = fileConfiguration.getInt(name + ".Y");
        int z = fileConfiguration.getInt(name + ".Z");
        BlockPos blockPos = new BlockPos(x, y, z);
        return blockPos;
    }
}
