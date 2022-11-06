package net.onebeastofchris.geyserplayerheads;

import com.google.gson.JsonObject;

import java.util.Base64;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;
import net.onebeastofchris.geyserplayerheads.utils.FloodgateUser;

public class TextureApplier {
    private final String encoded;
    private static boolean validSkin;

    public TextureApplier(String pPlayerName, UUID uuid, long pxuid, boolean isBedrock) {
        String playerName;
        if (pPlayerName.startsWith(".") || FloodgateUser.isFloodgatePlayer(uuid) || isBedrock) {
            String a = pPlayerName.replace(FloodgateUser.FloodgatePrefix(), "");
            playerName = a.toLowerCase();
            if (pxuid < 0) {
                pxuid = getXuid(playerName);
            }
            String textureID = TextureIdLookup(pxuid);
            encoded = setEncodedTextureID(textureID);
            GeyserPlayerHeads.debugLog("TextureApplier: " + playerName + " " + textureID + " " + encoded);
        } else {
            playerName = pPlayerName.toLowerCase();
            encoded = null;
        }
    }

    public static long getXuid(String pPlayerName) {
        var xuidJson = new ServerRequest().webRequest("https://api.geysermc.org/v2/xbox/xuid/" + pPlayerName.replace(" ", "_"));
        try {
            return xuidJson.get("xuid").getAsLong();
        } catch (Exception e) {
            if (xuidJson.get("message").getAsString().equals("gamertag is empty or longer than 16 chars")) {
                return -2;
            } else if (xuidJson.get("message").getAsString().equals("Unable to find user in our cache. Please try specifying their Floodgate UUID instead")) {
                return -3;
            } else {
                GeyserPlayerHeads.debugLog("GetXuid: " + e.getMessage());
                return -1;
            }
        }
    }

    public String TextureIdLookup(long playerXUID) {
        GeyserPlayerHeads.debugLog("TextureIdLookup: " + playerXUID);
        if (playerXUID > 0) {
            JsonObject getJson = new ServerRequest().webRequest("https://api.geysermc.org/v2/skin/" + playerXUID);
            try {
                validSkin = true;
                return getJson.get("texture_id").getAsString();
            } catch (Exception e) {
                GeyserPlayerHeads.debugLog("TextureIdLookup: " + e.getMessage());
                validSkin = false;
                return null;
            }
        } else {
            validSkin = false;
            return null;
        }
    }

    public NbtCompound getBedrockNbt(Entity pAttacker, String shownName) {
        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();
        NbtCompound c2 = new NbtCompound();
        NbtList nl = new NbtList();
        NbtCompound c3 = new NbtCompound();
        NbtCompound n1 = new NbtCompound();
        NbtList n2 = new NbtList();

        try {
            c3.putString("Value", getEncoded());
            nl.add(c3);
            c2.put("textures", nl);
            c1.put("Properties", c2);
            c1.putIntArray("Id", new int[]{1, 1, 1, 1});
            c.put("SkullOwner", c1);
            n1.putString("Name", getJsonText(getNameWithPrefix(shownName) + "'s head"));
            if(!getAttacker(pAttacker).isBlank() && GeyserPlayerHeads.config.showLore){
                    n2.add(NbtString.of(getJsonText("killed by " + getAttacker(pAttacker))));
                    n1.put("Lore", n2);
                }
            c.put("display", n1);
            return c;
        }
        catch(Exception e){
            GeyserPlayerHeads.debugLog("getBedrockNbt: " + e.getMessage());
            return null;
        }
    }

    public NbtCompound getJavaNbt(Entity pAttacker, String shownName) {
        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();
        NbtList c2 = new NbtList();

        c1.putString("Name", getJsonText(shownName + "'s head"));
        if (!getAttacker(pAttacker).isBlank() && GeyserPlayerHeads.config.showLore) {
            c2.add(NbtString.of(getJsonText("killed by " + getAttacker(pAttacker))));
            c1.put("Lore", c2);
        }
        c.putString("SkullOwner", shownName);
        c.put("display", c1);
        return c;
    }

    public String getAttacker(Entity pEntity) {
        if (pEntity instanceof PlayerEntity player) {
            if (FloodgateUser.isFloodgatePlayer(player.getUuid()) || player.getEntityName().startsWith(".")) {
                if (GeyserPlayerHeads.config.includeFloodgatePrefixInNames) {
                    return FloodgateUser.FloodgatePrefix() + player.getEntityName();
                } else {
                    return player.getEntityName();
                }
            } else {
                return player.getEntityName();
            }
        } else return "";
    }

    public String getJsonText(String a) {
        String b;
        b = Text.Serializer.toJson(Text.literal(a).styled(style -> style.withItalic(false)));
        return b;
    }

    public static boolean isValidSkin() {
        return validSkin;
    }

    public static String setEncodedTextureID(String pTID) {
        try {
            String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/" + pTID + "\"}}}";
            return Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
        } catch (Exception e) {
            GeyserPlayerHeads.debugLog("setEncodedTextureID: pTID is null" + e.getMessage());
            return null;
        }
    }

    public String getNameWithPrefix(String pPlayerName) {
        String a = pPlayerName.replace(FloodgateUser.FloodgatePrefix(), "");
        if (GeyserPlayerHeads.config.includeFloodgatePrefixInNames) {
            return FloodgateUser.FloodgatePrefix() + a;
        } else {
            return a;
        }
    }

    public String getEncoded() {
        return this.encoded;
    }
}
