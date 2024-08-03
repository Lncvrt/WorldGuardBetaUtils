package xyz.lncvrt.worldGuardBetaUtils.listeners;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import xyz.lncvrt.worldGuardBetaUtils.WorldGuardBetaUtils;
import xyz.lncvrt.worldGuardBetaUtils.utils.RegionUtil;

import java.util.HashSet;
import java.util.Set;

public class PlayerJoin extends PlayerListener {
    private final WorldGuardBetaUtils plugin;

    public PlayerJoin(WorldGuardBetaUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Vector playerLocation = BukkitUtil.toVector(player.getLocation());
        RegionManager regionManager = WorldGuardBetaUtils.worldGuard.getRegionManager(player.getLocation().getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(playerLocation);

        Set<String> currentRegions = new HashSet<>();
        for (ProtectedRegion region : set) {
            currentRegions.add(region.getId());
        }

        for (String region : currentRegions) {
            new RegionUtil().handleRegionChange(player, region, "enter");
        }

        plugin.playerRegions.put(player, currentRegions);
    }
}
