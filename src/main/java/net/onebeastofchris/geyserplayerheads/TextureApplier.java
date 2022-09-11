package net.onebeastofchris.geyserplayerheads;
import com.google.gson.JsonObject;
import java.util.Base64;
import net.minecraft.nbt.*;

public class TextureApplier {
    private final String textureID;
    private final String encoded;
    private final String playerName;

    public TextureApplier(String pPlayerName){
        long xuid = getXuid(pPlayerName);
            playerName = pPlayerName;
            textureID = getTextureId(xuid);
            String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/"+ getTextureID() + "\"}}}";
            encoded = Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
    }

    public long getXuid(String playerName){
        var xuid = new ServerRequest().webRequest("https://api.geysermc.org/v2/xbox/xuid/" + playerName);
        if (xuid.has("xuid")){
            return xuid.get("xuid").getAsLong();
        } else if (xuid.has("message")){
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

    public NbtCompound getBedrockNbt(){
        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();
        NbtCompound c2 = new NbtCompound();
        NbtList nl = new NbtList();
        NbtCompound c3 = new NbtCompound();
        NbtCompound n = new NbtCompound();

        c3.putString("Value", getEncoded());
        nl.add(c3);
        c2.put("textures", nl);
        c1.put("Properties", c2);
        c1.putIntArray("Id", new int[] {1,1,1,1});
        c.put("SkullOwner", c1);
        n.putString("Name","[{\"text\":\"." + getPlayerName() + "'s head\",\"italic\":false}]");
        c.put("display", n);
        GeyserPlayerHeads.getLogger().info(c.asString());
        return c;
    }

    public NbtCompound getJavaNbt() {
        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();

        c1.putString("Name","[{\"text\":\"" + getPlayerName() + "'s head\",\"italic\":false}]");
        c.putString("SkullOwner", getPlayerName());
        c.put("display", c1);
        GeyserPlayerHeads.getLogger().info(c.asString());
        return c;
    }

    public String getTextureID(){
        return this.textureID;
    }
    public String getPlayerName(){
        return this.playerName;
    }
    public String getEncoded(){
        return this.encoded;
    }
}