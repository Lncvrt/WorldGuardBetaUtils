package xyz.lncvrt.worldGuardBetaUtils.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import xyz.lncvrt.worldGuardBetaUtils.WorldGuardBetaUtils;

import static org.bukkit.Bukkit.getServer;

public class RegionUtil {
    public void handleRegionChange(Player player, String region, String action) {
        JsonArray regionArray = WorldGuardBetaUtils.data;
        for (JsonElement element : regionArray) {
            JsonObject regionConfig = element.getAsJsonObject();
            String regionName = regionConfig.getAsJsonPrimitive("regionName").getAsString();
            if (regionName.equals(region) && regionConfig.has(action)) {
                JsonObject actionConfig = regionConfig.getAsJsonObject(action);
                JsonArray actions = actionConfig.getAsJsonArray("actions");

                for (JsonElement actionElement : actions) {
                    JsonObject actionObject = actionElement.getAsJsonObject();
                    String type = actionObject.get("type").getAsString();
                    String permission = actionObject.has("permission") ? actionObject.get("permission").getAsString() : null;

                    if (permission == null || player.hasPermission(permission)) {
                        if ("command".equals(type)) {
                            String command = ChatColor.translateAlternateColorCodes('&', actionObject.get("command").getAsString().replace("%name%", player.getName()).replace("%displayname%", player.getDisplayName()));
                            String executor = actionObject.has("executor") ? actionObject.get("executor").getAsString() : "player";
                            if ("console".equals(executor)) {
                                getServer().dispatchCommand((((CraftServer)getServer()).getServer()).console, command);
                            } else if ("broadcast".equals(executor)) {
                                getServer().broadcastMessage(command);
                            } else {
                                getServer().dispatchCommand(player, command);
                            }
                        } else if ("message".equals(type)) {
                            String message = ChatColor.translateAlternateColorCodes('&', actionObject.get("message").getAsString().replace("%name%", player.getName()).replace("%displayname%", player.getDisplayName()));
                            String recipient = actionObject.has("recipient") ? actionObject.get("recipient").getAsString() : "player";
                            if ("broadcast".equals(recipient)) {
                                getServer().broadcastMessage(message);
                            } else {
                                player.sendMessage(message);
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
