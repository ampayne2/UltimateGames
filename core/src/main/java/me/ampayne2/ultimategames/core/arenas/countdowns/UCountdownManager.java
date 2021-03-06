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
package me.ampayne2.ultimategames.core.arenas.countdowns;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.arenas.countdowns.CountdownManager;
import me.ampayne2.ultimategames.api.arenas.countdowns.EndingCountdown;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.arenas.UArena;

import java.util.HashMap;
import java.util.Map;

public class UCountdownManager implements CountdownManager {
    private final UG ultimateGames;
    private Map<Arena, StartingCountdown> starting = new HashMap<>();
    private Map<Arena, EndingCountdown> ending = new HashMap<>();

    /**
     * Creates a new Countdown Manager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UCountdownManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean hasStartingCountdown(Arena arena) {
        return starting.containsKey(arena);
    }

    @Override
    public boolean hasEndingCountdown(Arena arena) {
        return ending.containsKey(arena);
    }

    /**
     * Gets an arena's starting countdown.
     *
     * @param arena The arena.
     * @return The arena's starting countdown.
     */
    public StartingCountdown getStartingCountdown(Arena arena) {
        return starting.get(arena);
    }

    @Override
    public EndingCountdown getEndingCountdown(Arena arena) {
        return ending.get(arena);
    }

    @Override
    public void createStartingCountdown(Arena arena, Integer seconds) {
        if (arena.getGame().getGamePlugin().isStartPossible(arena) && !starting.containsKey(arena)) {
            StartingCountdown countdown = new StartingCountdown(ultimateGames, arena, seconds);
            countdown.start();
            starting.put(arena, countdown);
            ultimateGames.getMessenger().debug("Created starting countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }

    public void stopStartingCountdown(Arena arena, boolean cancelled) {
        if (starting.containsKey(arena)) {
            starting.get(arena).stop();
            starting.remove(arena);
            if (cancelled) {
                ((UArena) arena).setStatus(ArenaStatus.OPEN);
            }
            ultimateGames.getMessenger().debug("Stopped starting countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }

    @Override
    public void stopStartingCountdown(Arena arena) {
        stopStartingCountdown(arena, true);
    }

    @Override
    public void createEndingCountdown(Arena arena, Integer seconds, Boolean expDisplay) {
        if (!ending.containsKey(arena)) {
            EndingCountdown countdown = new EndingCountdown(ultimateGames, arena, seconds, expDisplay);
            countdown.start();
            ending.put(arena, countdown);
            ultimateGames.getMessenger().debug("Created ending countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }

    @Override
    public void stopEndingCountdown(Arena arena) {
        if (ending.containsKey(arena)) {
            ending.get(arena).stop();
            ending.remove(arena);
            ultimateGames.getMessenger().debug("Stopped ending countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }
}
