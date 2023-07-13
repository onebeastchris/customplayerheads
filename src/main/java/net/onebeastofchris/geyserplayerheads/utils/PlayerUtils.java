package net.onebeastofchris.geyserplayerheads.utils;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;

import java.util.HashMap;

public class PlayerUtils {

    public static HashMap<String, String> javaUUIDmap = new HashMap<>();
    public static HashMap<String, String> bedrockXUIDmap = new HashMap<>();
    private static final String GEYSER_XUID_API = "https://api.geysermc.org/v2/xbox/xuid/";
    private static final String GEYSER_SKIN_API = "https://api.geysermc.org/v2/skin/";
    private static final String MOJANG_SESSIONSERVER = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String MOJANG_UUID = "https://api.mojang.com/users/profiles/minecraft/";

    public static boolean isBedrockPlayer(PlayerEntity player) {
        return FloodgateUtil.isBedrockPlayer(player.getUuid(), player.getEntityName());
    }

    public static String xuidLookup(String playername, PlayerEntity self) {
        String lowerCasePlayerName = playername.toLowerCase().replace(FloodgateUtil.FloodgatePrefix(), "");

        if (bedrockXUIDmap.get(lowerCasePlayerName) != null) {
            GeyserPlayerHeads.debugLog("Bedrock player" + playername + " is cached, using their cached XUID: " + bedrockXUIDmap.get(playername));
            return bedrockXUIDmap.get(lowerCasePlayerName);
        }

        var xuidJson = new WebUtil().webRequest(GEYSER_XUID_API + lowerCasePlayerName);
        try {
            return xuidJson.get("xuid").getAsString();
        } catch (Exception e) {
            switch (xuidJson.get("message").getAsString()) {
                case "gamertag is empty or longer than 16 chars" -> {
                    self.sendMessage(Text.literal("Invalid Bedrock player name.").formatted(Formatting.RED));
                    return null;
                }
                case "Unable to find user in our cache. Please try specifying their Floodgate UUID instead" -> {
                    self.sendMessage(Text.literal("That Bedrock player either does not exist, or has not joined a Geyser server yet.").formatted(Formatting.RED));
                    return null;
                }
                default -> {
                    GeyserPlayerHeads.debugLog("GetXuid: " + e.getMessage());
                    self.sendMessage(Text.literal("An error occurred while trying to get the XUID of that Bedrock player.").formatted(Formatting.RED));
                    return null;
                }
            }
        }
    }
    public static String getTextureID(String lookupID, boolean isBedrock) {
        if (isBedrock) {
            JsonObject getJson = new WebUtil().webRequest(GEYSER_SKIN_API + lookupID);
            try {
                return getJson.get("value").getAsString();
            } catch (Exception e) {
                GeyserPlayerHeads.debugLog("value (geyserapi): " + e.getMessage());
                return null;
            }
        } else {
            JsonObject getJson = new WebUtil().webRequest(MOJANG_SESSIONSERVER + lookupID);
            try {
                return getJson.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
            } catch (Exception e) {
                GeyserPlayerHeads.debugLog("TextureIdLookup: " + e.getMessage());
                return null;
            }
        }
    }

    public static String getJavaUUID(String playername) {
        String lowerCasePlayerName = playername.toLowerCase();

        // cache the UUIDs, so we don't have to make a web request every time
        if (javaUUIDmap.get(lowerCasePlayerName) != null) {
            return javaUUIDmap.get(lowerCasePlayerName);
        }
        JsonObject getJson = new WebUtil().webRequest(MOJANG_UUID + lowerCasePlayerName);

        var uuid = getJson.get("id");
        if (uuid == null) {
            return null;
        }
        return uuid.getAsString();
    }
}