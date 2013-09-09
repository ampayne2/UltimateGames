package me.ampayne2.ultimategames.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ampayne2.ultimategames.arenas.Arena;

public class ArenaPlayer {
    
    private String playerName;
    private Arena arena;
    private boolean editing = false;
    
    /**
     * Represents a player in an arena.
     * @param playerName The player's name.
     * @param arena The arena.
     */
    public ArenaPlayer(String playerName, Arena arena) {
        this.playerName = playerName;
        this.arena = arena;
    }
    
    /**
     * Gets the player.
     * @return The player.
     */
    public Player getPlayer() {
        return Bukkit.getPlayerExact(playerName);
    }
    
    /**
     * Gets a player's name.
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Gets the player's arena.
     * @return The player's arena.
     */
    public Arena getArena() {
        return arena;
    }
    
    /**
     * Checks if the player is in editing mode.
     * @return True if the player is in editing mode, else false.
     */
    public boolean isEditing() {
        return editing;
    }
    
    /**
     * Sets the player's editing mode.
     * @param editing If the player should be in editing mode.
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

}