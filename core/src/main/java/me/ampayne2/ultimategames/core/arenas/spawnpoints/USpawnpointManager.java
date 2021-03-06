/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.arenas.spawnpoints;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.spawnpoints.PlayerSpawnPoint;
import me.ampayne2.ultimategames.api.arenas.spawnpoints.SpawnpointManager;
import me.ampayne2.ultimategames.api.arenas.spawnpoints.SpectatorSpawnPoint;
import me.ampayne2.ultimategames.api.config.ConfigType;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class USpawnpointManager implements SpawnpointManager {
    private final UG ultimateGames;
    private Map<Arena, List<PlayerSpawnPoint>> playerSpawnPoints = new HashMap<>();
    private Map<Arena, SpectatorSpawnPoint> spectatorSpawnPoints = new HashMap<>();

    /**
     * Creates a new Spawnpoint Manager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public USpawnpointManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean hasSpawnPointAtIndex(Arena arena, int index) {
        return playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= index;
    }

    /**
     * Adds a spawnpoint to the manager.
     *
     * @param spawnPoint The spawnpoint.
     */
    public void addSpawnPoint(PlayerSpawnPoint spawnPoint) {
        if (playerSpawnPoints.containsKey(spawnPoint.getArena())) {
            playerSpawnPoints.get(spawnPoint.getArena()).add(spawnPoint);
        } else {
            List<PlayerSpawnPoint> spawn = new ArrayList<>();
            spawn.add(spawnPoint);
            playerSpawnPoints.put(spawnPoint.getArena(), spawn);
        }
    }

    /**
     * Creates a new Spectator spawnpoint and adds it to the manager and config.
     *
     * @param arena    The spawnpoint's arena.
     * @param location The spawnpoint's location.
     */
    public void setSpectatorSpawnPoint(Arena arena, Location location) {
        List<String> newSpawnPoint = new ArrayList<>();
        newSpawnPoint.add(String.valueOf(location.getX()));
        newSpawnPoint.add(String.valueOf(location.getY()));
        newSpawnPoint.add(String.valueOf(location.getZ()));
        newSpawnPoint.add(String.valueOf(location.getPitch()));
        newSpawnPoint.add(String.valueOf(location.getYaw()));
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getConfig(ConfigType.ARENA);
        String path = "Arenas." + arena.getGame().getName() + "." + arena.getName() + ".SpectatorSpawnpoint";
        arenaConfig.set(path, newSpawnPoint);
        spectatorSpawnPoints.put(arena, new SpectatorSpawnPoint(arena, location));
    }

    /**
     * Creates a spawnpoint.
     * The spawnpoint is added to the manager and config.
     *
     * @param arena    The Arena.
     * @param location The location.
     * @param locked   If the spawnpoint prevents the player from moving off of it.
     * @return The spawnpoint created.
     */
    public PlayerSpawnPoint createSpawnPoint(Arena arena, Location location, Boolean locked) {
        List<String> newSpawnPoint = new ArrayList<>();
        newSpawnPoint.add(String.valueOf(location.getX()));
        newSpawnPoint.add(String.valueOf(location.getY()));
        newSpawnPoint.add(String.valueOf(location.getZ()));
        newSpawnPoint.add(String.valueOf(location.getPitch()));
        newSpawnPoint.add(String.valueOf(location.getYaw()));
        newSpawnPoint.add(String.valueOf(locked));
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getConfig(ConfigType.ARENA);
        String path = "Arenas." + arena.getGame().getName() + "." + arena.getName() + ".SpawnPoints";
        if (arenaConfig.contains(path)) {
            @SuppressWarnings("unchecked") List<List<String>> arenaSpawnPoints = (ArrayList<List<String>>) arenaConfig.getList(path);
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        } else {
            List<List<String>> arenaSpawnPoints = new ArrayList<>();
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        }
        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
        PlayerSpawnPoint spawnPoint = new PlayerSpawnPoint(ultimateGames, arena, location, locked);
        addSpawnPoint(spawnPoint);
        return spawnPoint;
    }

    @Override
    public PlayerSpawnPoint getSpawnPoint(Arena arena, int index) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= index) {
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public SpectatorSpawnPoint getSpectatorSpawnPoint(Arena arena) {
        if (spectatorSpawnPoints.containsKey(arena)) {
            return spectatorSpawnPoints.get(arena);
        } else {
            return null;
        }
    }

    @Override
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena) {
        if (playerSpawnPoints.containsKey(arena)) {
            Random generator = new Random();
            Integer index = generator.nextInt(playerSpawnPoints.get(arena).size());
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena, int minIndex) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() > minIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(playerSpawnPoints.get(arena).size() - minIndex) + minIndex;
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena, int minIndex, int maxIndex) {
        if (playerSpawnPoints.containsKey(arena) && minIndex < maxIndex && playerSpawnPoints.get(arena).size() > maxIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(maxIndex - minIndex + 1) + minIndex;
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public List<PlayerSpawnPoint> getDistributedSpawnPoints(Arena arena, int amount) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= amount) {
            List<PlayerSpawnPoint> distributedSpawnPoints = new ArrayList<>();
            Integer size = playerSpawnPoints.get(arena).size();
            Double multiple = (double) size / (double) amount;
            for (int i = 0; i < amount; i++) {
                Integer index = (int) Math.round(i * multiple);
                distributedSpawnPoints.add(playerSpawnPoints.get(arena).get(index));
            }
            return distributedSpawnPoints;
        }
        return new ArrayList<>();
    }

    @Override
    public List<PlayerSpawnPoint> getSpawnPointsOfArena(Arena arena) {
        if (playerSpawnPoints.containsKey(arena)) {
            return playerSpawnPoints.get(arena);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Removes the spawnpoint at a certain index.
     *
     * @param arena The arena.
     * @param index The index.
     */
    public void removeSpawnPoint(Arena arena, int index) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= index) {
            playerSpawnPoints.get(arena).remove(playerSpawnPoints.get(arena).get(index));
            // TODO: Remove spawnpoint from arena config.
        }
    }

    /**
     * Removes the spawnpoints at the indexes.
     *
     * @param arena   The arena.
     * @param indexes The indexes.
     */
    public void removeSpawnPoints(Arena arena, int... indexes) {
        if (playerSpawnPoints.containsKey(arena)) {
            List<PlayerSpawnPoint> remove = new ArrayList<>();
            for (Integer index : indexes) {
                if (playerSpawnPoints.get(arena).size() >= index) {
                    remove.add(playerSpawnPoints.get(arena).get(index));
                }
            }
            playerSpawnPoints.get(arena).removeAll(remove);
            // TODO: Remove spawnpoints from arena config.
        }
    }

    /**
     * Removes all the spawnpoints of an arena.
     *
     * @param arena The arena.
     */
    public void removeAllSpawnPoints(Arena arena) {
        if (playerSpawnPoints.containsKey(arena)) {
            playerSpawnPoints.remove(arena);
            // TODO: Remove spawnpoints from arena config.
        }
    }
}
