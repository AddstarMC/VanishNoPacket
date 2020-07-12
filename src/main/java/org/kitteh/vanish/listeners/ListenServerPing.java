package org.kitteh.vanish.listeners;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.kitteh.vanish.VanishManager;

public final class ListenServerPing implements Listener {
    private final VanishManager manager;

    public ListenServerPing(VanishManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void ping(ServerListPingEvent event) {
        if (event == null) {
            return; // Pre-API server
        }
        final Set<UUID> invisiblePlayers = this.manager.getVanishedPlayersUUID();
        final Iterator<Player> players;
        try {
            players = event.iterator();
        } catch (final UnsupportedOperationException e) {
            return;
            // NOOP
        }
        Player player;
        while (players.hasNext()) {
            player = players.next();
            if (invisiblePlayers.contains(player.getUniqueId())) {
                players.remove();
            }
        }
    }
}
