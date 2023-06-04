package net.onebeastofchris.geyserplayerheads.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.http.HttpClient.newHttpClient;

public class WebUtil {

    public JsonObject webRequest(String url) {
        GeyserPlayerHeads.debugLog("webRequest: " + url);
        var client = newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            GeyserPlayerHeads.getLogger().error("Error while sending request to " + url);
            throw new RuntimeException(e);
        }
        GeyserPlayerHeads.debugLog("webRequest: " + new Gson().fromJson(resp.body(), JsonObject.class).toString());
        GeyserPlayerHeads.debugLog("webRequest: " + resp.body());

        return new Gson().fromJson(resp.body(), JsonObject.class);
    }
}