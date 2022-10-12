package net.onebeastofchris.geyserplayerheads.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class FloodgateUser {
    /**
     * Determines if a player is a bedrock player
     *
     * @param uuid the UUID to determine
     * @return true if the player is from floodgate
     */
    public static boolean isFloodgatePresent(){
        return FabricLoader.getInstance().isModLoaded("floodgate");
    }
    public static boolean isFloodgatePlayer(UUID uuid) {
        if (!isFloodgatePresent()) {
            //GeyserPlayerHeads.getLogger().info("Floodgate is not installed! We will check the bedrock . prefix.");
            return false;
        }
        if (FloodgateApi.getInstance() == null){
            GeyserPlayerHeads.getLogger().info("Floodgate seems to be installed, but GeyserPlayerHeads cannot access it. Please report this on the GitHub page!");
            return false;
        }
        return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
    }
    public static String FloodgatePrefix() {
        if (!isFloodgatePresent()) {
            //GeyserPlayerHeads.getLogger().info("Floodgate is not installed! We will check the bedrock . prefix.");
            return ".";
        }
        if (FloodgateApi.getInstance() == null){
            GeyserPlayerHeads.getLogger().info("Floodgate seems to be installed, but GeyserPlayerHeads cannot access it. Please report this on the GitHub page!");
            return ".";
        }
        return FloodgateApi.getInstance().getPlayerPrefix();
    }
}
