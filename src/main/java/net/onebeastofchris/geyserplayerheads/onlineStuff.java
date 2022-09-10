package net.onebeastofchris.geyserplayerheads;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import net.minecraft.nbt.*;
import net.minecraft.nbt.visitor.NbtOrderedStringFormatter;

import static java.net.http.HttpClient.newHttpClient;

public class onlineStuff {

    private long xuid;
    private String textureID;
    private String encoded;

    private String playername;
    public onlineStuff(String pUsername){
            setPlayername(pUsername);
            xuid = getXuid(pUsername);
            textureID = getTextureId(xuid);
            String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/"+ getTextureID() + "\"}}}";
            encoded = Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
    }
    public void setPlayername(String pName){
        this.playername = pName;
    }
    public String getPlayername(){
        return this.playername;
    }
    public void setXuid(long a){
        this.xuid = a;
    }
    public long getXuid(){
        return this.xuid;
    }
    public void setTextureID(String a){
        this.textureID = a;
    }
    public String getTextureID(){
        return this.textureID;
    }
    public void setEncoded(String a){
        this.encoded = a;
    }
    public String getEncoded(){
        return this.encoded;
    }
    public JsonObject webRequest(String pUrl) {
        //todo: async
        var client = newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(pUrl))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Gson().fromJson(resp.body(), JsonObject.class);
    }

    public long getXuid(String pUsername){
        var xuid = webRequest("https://api.geysermc.org/v2/xbox/xuid/"+pUsername);
        if (xuid.has("xuid")){
            return xuid.get("xuid").getAsLong();
        } else if (xuid.has("message")){
            return 0;
        }
        return -1;
    }
    public String getTextureId(long pXuid) {
        if (pXuid > 0) {
            JsonObject getJson = (webRequest("https://api.geysermc.org/v2/skin/"+pXuid));
            return getJson.get("texture_id").getAsString();
        } else return null;
        // if message gets returned = player wasn't found
    }
    public NbtCompound getBedrockNbt(){
        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();
        NbtCompound c2 = new NbtCompound();
        int[] intArray = new int[] {1,1,1,1};
        NbtList nl = new NbtList();
        NbtCompound c3 = new NbtCompound();

        c3.putString("Value", getEncoded());
        nl.add(c3);
        c2.put("textures", nl);
        c1.put("Properties", c2);
        c1.putIntArray("Id", intArray);
        c.put("SkullOwner", c1);
        GeyserPlayerHeads.LOGGER.info(c.asString());
        return c;
    }
    public NbtCompound getJavaNbt() {
        NbtCompound c = new NbtCompound();

        c.putString("SkullOwner", getPlayername());
        GeyserPlayerHeads.LOGGER.info(c.asString());
        return c;
    }
}

//{SkullOwner:{Id:[I;-12288,4481,213748,-8962],Properties:{textures:[{Value:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQyM2M0ZjQwNWQxYTA0NGExYTlmODNmZmNlNGYxOTljODkwOTk5Mzg1ZGNlNDQ1MWNjYTFiYWVkNGEzNjA2ZGMifX19}]}}} 1
//{SkullOwner:{Id:[I;1,1,1,1],                 Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZhZTA2ZTZjMjdiN2ZhNjI1Y2ZiODdjMTE3Y2RjZDVlYjAxODI2ZjZiOWQwNGJlN2Q2NzczZjYwYzE3NmM1MmYifX19"}]}}}
//{SkullOwner:{Id:[I;1,1,1,1],                 Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZhZTA2ZTZjMjdiN2ZhNjI1Y2ZiODdjMTE3Y2RjZDVlYjAxODI2ZjZiOWQwNGJlN2Q2NzczZjYwYzE3NmM1MmYifX19"}]}}}