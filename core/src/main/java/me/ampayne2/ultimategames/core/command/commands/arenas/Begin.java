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
package me.ampayne2.ultimategames.core.command.commands.arenas;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.command.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that begins an arena.
 */
public class Begin extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the Begin command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public Begin(UG ultimateGames) {
        super(ultimateGames, "begin", "Begins an arena.", "/ug arena begin <arena> <game>", new Permission("ultimategames.arena.begin", PermissionDefault.OP), 2, false);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String arenaName = args[0];
        String gameName = args[1];
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.GAME_DOESNTEXIST);
            return;
        } else if (!ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ARENA_DOESNTEXIST);
            return;
        }
        Arena arena = ultimateGames.getArenaManager().getArena(arenaName, gameName);
        if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
            ultimateGames.getCountdownManager().createStartingCountdown(arena, 5);
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ARENA_FORCESTART, arenaName, gameName);
        }
    }
}
