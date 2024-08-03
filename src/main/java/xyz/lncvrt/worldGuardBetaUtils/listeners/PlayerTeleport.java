package xyz.lncvrt.worldGuardBetaUtils.listeners;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;
import xyz.lncvrt.worldGuardBetaUtils.WorldGuardBetaUtils;
import xyz.lncvrt.worldGuardBetaUtils.utils.RegionUtil;

import java.util.HashSet;
import java.util.Set;

public class PlayerTeleport extends PlayerListener {
    private final WorldGuardBetaUtils plugin;

    public PlayerTeleport(WorldGuardBetaUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Vector newLocation = BukkitUtil.toVector(event.getTo());
        RegionManager regionManager = WorldGuardBetaUtils.worldGuard.getRegionManager(event.getTo().getWorld());
        ApplicableRegionSet newRegions = regionManager.getApplicableRegions(newLocation);

        Set<String> newRegionIds = new HashSet<>();
        for (ProtectedRegion region : newRegions) {
            newRegionIds.add(region.getId());
        }

        Set<String> previousRegionIds = plugin.playerRegions.getOrDefault(player, new HashSet<>());

        for (String regionId : newRegionIds) {
            if (!previousRegionIds.contains(regionId)) {
                new RegionUtil().handleRegionChange(player, regionId, "enter");
            }
        }

        for (String regionId : previousRegionIds) {
            if (!newRegionIds.contains(regionId)) {
                new RegionUtil().handleRegionChange(player, regionId, "exit");
            }
        }

        plugin.playerRegions.put(player, newRegionIds);
    }
}
