package net.onebeastofchris.geyserplayerheads.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateUtil {
    public static boolean isFloodgatePresent() {
        return FabricLoader.getInstance().isModLoaded("floodgate");
    }

    public static boolean isBedrockPlayer(UUID uuid, String playerName) {
        //linked players should be treated as Java players
        if (!isFloodgatePresent()) {
            return fallback(uuid, playerName);
        } else if (FloodgateApi.getInstance() == null) {
            GeyserPlayerHeads.getLogger().info("Floodgate seems to be installed, but GeyserPlayerHeads cannot access it. Please report this as an issue on the GitHub page!");
            return fallback(uuid, playerName);
        } else
            return FloodgateApi.getInstance().isFloodgatePlayer(uuid) && !isLinked(uuid);
    }

    public static String FloodgatePrefix() {
        if (!isFloodgatePresent()) {
            return ".";
        } else if (FloodgateApi.getInstance() == null) {
            GeyserPlayerHeads.getLogger().info("Floodgate seems to be installed, but GeyserPlayerHeads cannot access it. Please report this as an issue on the GitHub page!");
            return ".";
        } else
            return FloodgateApi.getInstance().getPlayerPrefix();
    }

    public static boolean isLinked(UUID uuid) {
        //do I need this?
        if (!isFloodgatePresent()) {
            return false;
        } else if (FloodgateApi.getInstance() == null) {
            GeyserPlayerHeads.getLogger().error("Floodgate seems to be installed, but GeyserPlayerHeads cannot access it. Please report this as an issue on the GitHub page!");
            return false;
        } else {
            FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(uuid);
            return floodgatePlayer.isLinked();
        }
    }

    private static boolean fallback(UUID uuid, String playerName) {
        return uuid.version() == 0 || playerName.startsWith(FloodgatePrefix());
    }
}