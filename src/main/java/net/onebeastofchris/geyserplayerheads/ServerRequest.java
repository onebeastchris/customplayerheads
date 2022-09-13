package net.onebeastofchris.geyserplayerheads;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.http.HttpClient.newHttpClient;

public class ServerRequest {

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
}
