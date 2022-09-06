package net.onebeastofchris.geyserplayerheads;

import com.google.gson.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static java.net.http.HttpClient.newHttpClient;

public class GeyserPlayerHeads implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("gph");

	public GeyserPlayerHeads() {
		setXuidurl("https://api.geysermc.org/v2/xbox/xuid/");
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("GeyserPlayerHeads starting now");
	}

	private String xuidurl;
	public void setXuidurl(String pURL){
		this.xuidurl = pURL;
	}

	public int getXuid(String pUsername){
		var xuid = webRequest("https://api.geysermc.org/v2/xbox/xuid/"+pUsername);
		if (xuid.has("xuid")){
			return xuid.getAsInt();
		} else if (xuid.has("message")){
			return 0;
		}
		return -1;
	}
	public JsonObject getTextureId(int pXuid) {
		if (pXuid <= 0) {
				JsonObject getJson = (webRequest("https://api.geysermc.org/v2/skin/"+pXuid));
				//return texture_id aus getSkinJson
				JsonObject getSkinJson = getJson.getAsJsonObject("texture_id");
				return getSkinJson;
			}
			// if message gets returned = player wasn't found
			return null;
		}

	public JsonObject webRequest(String pUrl){
		var client = newHttpClient();
		var request = HttpRequest.newBuilder()
				.uri(URI.create(pUrl))
				.GET()
				.build();
		HttpResponse<String> resp;
		try {
			resp = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		return new Gson().fromJson(resp.body(), JsonObject.class);
	}
}



