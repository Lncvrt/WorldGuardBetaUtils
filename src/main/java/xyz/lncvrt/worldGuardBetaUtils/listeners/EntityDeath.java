package xyz.lncvrt.worldGuardBetaUtils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import xyz.lncvrt.worldGuardBetaUtils.WorldGuardBetaUtils;

public class EntityDeath extends EntityListener {
    private final WorldGuardBetaUtils plugin;

    public EntityDeath(WorldGuardBetaUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            plugin.playerRegions.remove((Player) event.getEntity());
        }
    }
}
