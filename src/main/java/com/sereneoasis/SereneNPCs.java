package com.sereneoasis;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.sereneoasis.chat.ChatConfiguration;
import com.sereneoasis.command.SerenityCommand;
import com.sereneoasis.config.FileManager;
import com.sereneoasis.listeners.SereneListener;
import com.sereneoasis.npc.random.types.NPCMaster;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/***
 * The main class of the plugin
 */
public class SereneNPCs extends JavaPlugin {

    public static SereneNPCs plugin;

    private static FileManager fileManager;

    /***
     * Returns the class used to manage files
     * @return the file manager
     */
    public static FileManager getFileManager() {
        return fileManager;
    }


    //Used to keep our NPCs to be accessed in other classes
    private final Set<NPCMaster> npcs = new HashSet<>();

    private HashMap<UUID, ChestGui> uuidChestGuiHashMap = new HashMap<>();




    public void addNPC(NPCMaster npcMaster){
        npcs.add(npcMaster);
    }
    public Set<NPCMaster> getNpcs() {
        return npcs;
    }

    private static Economy econ = null;

    @Override
    public void onEnable() {

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().log(Level.INFO, "WorldGenerator was enabled successfully.");
        plugin = this;
        fileManager = new FileManager();
        this.getServer().getPluginManager().registerEvents(new SereneListener(), this);
        this.getCommand("SereneNPCs").setExecutor(new SerenityCommand());

        new ChatConfiguration();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "SereneNPCs was disabled successfully.");
    }


}