package xyz.lncvrt.worldGuardBetaUtils.listeners;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lncvrt.worldGuardBetaUtils.WorldGuardBetaUtils;

public class PlayerQuit extends PlayerListener {
    private final WorldGuardBetaUtils plugin;

    public PlayerQuit(WorldGuardBetaUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.playerRegions.remove(event.getPlayer());
    }
}
