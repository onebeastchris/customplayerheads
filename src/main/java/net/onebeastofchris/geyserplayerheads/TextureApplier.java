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
    private final String textureID;
    private final String encoded;
    private final String playerName;

    public TextureApplier(String pPlayerName, UUID uuid) {
        if (pPlayerName.startsWith(".") || FloodgateUser.isFloodgatePlayer(uuid)) {
            playerName = pPlayerName.replace(FloodgateUser.FloodgatePrefix(), "");
            long xuid = getXuid(playerName);
            textureID = getTextureId(xuid);
            String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/" + getTextureID() + "\"}}}";
            encoded = Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
        } else {
            playerName = pPlayerName;
            textureID = null;
            encoded = null;
        }
    }

    public static long getXuid(String playerName) {
        var xuid = new ServerRequest().webRequest("https://api.geysermc.org/v2/xbox/xuid/" + playerName);
        if (xuid.has("xuid")) {
            return xuid.get("xuid").getAsLong();
        } else if (xuid.has("message")) {
            return 0;
        }
        return -1;
    }

    public String getTextureId(long playerXUID) {
        if (playerXUID > 0) {
            JsonObject getJson = new ServerRequest().webRequest("https://api.geysermc.org/v2/skin/" + playerXUID);
            return getJson.get("texture_id").getAsString();
        } else return null;
        // if message gets returned = player wasn't found
    }

    public NbtCompound getBedrockNbt(Entity pAttacker) {
        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();
        NbtCompound c2 = new NbtCompound();
        NbtList nl = new NbtList();
        NbtCompound c3 = new NbtCompound();
        NbtCompound n1 = new NbtCompound();
        NbtList n2 = new NbtList();

        c3.putString("Value", getEncoded());
        nl.add(c3);
        c2.put("textures", nl);
        c1.put("Properties", c2);
        c1.putIntArray("Id", new int[]{1, 1, 1, 1});
        c.put("SkullOwner", c1);
        n1.putString("Name", getJsonText(getNameWithPrefix() + "'s head"));
        if (!getAttacker(pAttacker).isBlank()) {
            n2.add(NbtString.of(getJsonText("killed by " + getAttacker(pAttacker))));
            n1.put("Lore", n2);
        }
        c.put("display", n1);
        //GeyserPlayerHeads.getLogger().info(c.asString());
        return c;
    }

    public NbtCompound getJavaNbt(Entity pAttacker) {
        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();
        NbtList c2 = new NbtList();

        c1.putString("Name", getJsonText(getPlayerName() + "'s head"));
        if (!getAttacker(pAttacker).isBlank()) {
            c2.add(NbtString.of(getJsonText("killed by " + getAttacker(pAttacker))));
            c1.put("Lore", c2);
        }
        c.putString("SkullOwner", getPlayerName());
        c.put("display", c1);

        //GeyserPlayerHeads.getLogger().info(c.asString());
        return c;
    }

    public String getAttacker(Entity pEntity) {
        String pReturn = "";
        if (pEntity instanceof PlayerEntity player) {
            return player.getEntityName();
        } else return pReturn;
    }

    public String getJsonText(String a) {
        String b;
        b = Text.Serializer.toJson(Text.literal(a).styled(style -> style.withItalic(false)));
        return b;
    }

    public String getTextureID() {
        return this.textureID;
    }

    public String getNameWithPrefix() {
        return FloodgateUser.FloodgatePrefix() + this.playerName;
    }

    public String getEncoded() {
        return this.encoded;
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
