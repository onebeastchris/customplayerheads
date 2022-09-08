package net.onebeastofchris.geyserplayerheads;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

import static java.net.http.HttpClient.newHttpClient;

public class onlineStuff {

    private long xuid;
    private String textureID;
    private String encoded;
    public onlineStuff(String pUsername){
            xuid = getXuid(pUsername);
            textureID = getTextureId(xuid);
            String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/"+ getTextureID() + "\"}}}";
            encoded = Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
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
}
