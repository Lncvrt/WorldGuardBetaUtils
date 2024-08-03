package xyz.lncvrt.worldGuardBetaUtils;

import com.google.gson.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lncvrt.worldGuardBetaUtils.listeners.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class WorldGuardBetaUtils extends JavaPlugin implements Listener {
    private final PlayerMove playerMoveListener = new PlayerMove(this);
    private final PlayerQuit playerQuitListener = new PlayerQuit(this);
    private final PlayerJoin playerJoinListener = new PlayerJoin(this);
    private final PlayerTeleport playerTeleportListener = new PlayerTeleport(this);
    private final EntityDeath entityDeathListener = new EntityDeath(this);
    private final PlayerRespawn playerRespawnListener = new PlayerRespawn(this);
    public static WorldGuardPlugin worldGuard;
    public static JsonArray data = new JsonArray();
    public final Map<Player, Set<String>> playerRegions = new HashMap<>();

    @Override
    public void onEnable() {
        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (worldGuard == null || worldEdit == null) {
            Bukkit.getLogger().warning("Required plugins not found! Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        File dataFile = new File(getDataFolder(), "data.json");
        if (!dataFile.exists()) {
            try {
                getDataFolder().mkdirs();
                JsonArray jsonArray = new JsonArray();
                String jsonString = new Gson().toJson(jsonArray);

                try (Writer writer = new OutputStreamWriter(Files.newOutputStream(dataFile.toPath()), StandardCharsets.UTF_8)) {
                    writer.write(jsonString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(dataFile.toPath())) {
            data = JsonParser.parseReader(reader).getAsJsonArray();
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, playerMoveListener, Event.Priority.Low, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, playerQuitListener, Event.Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, playerJoinListener, Event.Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_TELEPORT, playerTeleportListener, Event.Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, entityDeathListener, Event.Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_RESPAWN, playerRespawnListener, Event.Priority.Normal, this);
    }

    @Override
    public void onDisable() {
    }
}
