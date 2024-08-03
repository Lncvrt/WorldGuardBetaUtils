package xyz.lncvrt.worldGuardBetaUtils.listeners;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.lncvrt.worldGuardBetaUtils.WorldGuardBetaUtils;
import xyz.lncvrt.worldGuardBetaUtils.utils.RegionUtil;

import java.util.HashSet;
import java.util.Set;

public class PlayerMove extends PlayerListener {
    private final WorldGuardBetaUtils plugin;

    public PlayerMove(WorldGuardBetaUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Vector playerLocation = BukkitUtil.toVector(player.getLocation());
        RegionManager regionManager = WorldGuardBetaUtils.worldGuard.getRegionManager(player.getLocation().getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(playerLocation);

        Set<String> currentRegions = new HashSet<>();
        for (ProtectedRegion region : set) {
            currentRegions.add(region.getId());
        }

        Set<String> previousRegions = plugin.playerRegions.getOrDefault(player, new HashSet<>());

        for (String region : currentRegions) {
            if (!previousRegions.contains(region)) {
                new RegionUtil().handleRegionChange(player, region, "enter");
            }
        }

        for (String region : previousRegions) {
            if (!currentRegions.contains(region)) {
                new RegionUtil().handleRegionChange(player, region, "exit");
            }
        }

        plugin.playerRegions.put(player, currentRegions);
    }
}
