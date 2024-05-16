package net.onebeastofchris.customplayerheads.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.onebeastofchris.customplayerheads.CustomPlayerHeads;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.http.HttpClient.newHttpClient;

public class WebUtil {

    public JsonObject webRequest(String url) {
        CustomPlayerHeads.debugLog("webRequest: " + url);
        HttpClient client = newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            CustomPlayerHeads.getLogger().error("Error while sending request to " + url);
            throw new RuntimeException(e);
        }
        CustomPlayerHeads.debugLog("webRequest: " + new Gson().fromJson(resp.body(), JsonObject.class).toString());
        CustomPlayerHeads.debugLog("webRequest: " + resp.body());

        return new Gson().fromJson(resp.body(), JsonObject.class);
    }
}