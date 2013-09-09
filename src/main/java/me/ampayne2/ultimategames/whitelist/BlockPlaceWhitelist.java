package me.ampayne2.ultimategames.whitelist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class BlockPlaceWhitelist extends Whitelist {

    private UltimateGames ultimateGames;
    private Map<Game, Set<Material>> blocks;
    private Map<Game, Boolean> useAsBlacklist = new HashMap<Game, Boolean>();

    /**
     * The Block Place Whitelist.
     * @param ultimateGames The UltimateGames instance.
     */
    public BlockPlaceWhitelist(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    public void reload() {
        blocks = new HashMap<Game, Set<Material>>();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
            if (gameConfig.contains("BlockPlaceWhitelist")) {
                List<String> materialNames = gameConfig.getStringList("BlockPlaceWhitelist");
                Set<Material> materials = new HashSet<Material>();
                for (String materialName : materialNames) {
                    materials.add(Material.valueOf(materialName));
                }
                blocks.put(game, materials);
                Boolean blacklist = gameConfig.getBoolean("DefaultSettings.Use-Whitelist-As-Blacklist", false);
                useAsBlacklist.put(game, blacklist);
            }
        }
    }

    /**
     * Checks if a certain material can be placed in a certain game.
     * @param game The game.
     * @param material The material.
     * @return True if the game has a whitelist and the material is whitelisted, else false.
     */
    public boolean canPlaceMaterial(Game game, Material material) {
        return blocks.containsKey(game) && (useAsBlacklist.get(game) ^ blocks.get(game).contains(material));
    }

}