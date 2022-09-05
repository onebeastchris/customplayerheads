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
	public static final Logger LOGGER = LoggerFactory.getLogger("geyserplayerheads is loading");

	public GeyserPlayerHeads() {
		setXuidurl("https://api.geysermc.org/v2/xbox/xuid/");
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}

	private String username = "Steve";
	private JsonObject textureid;
	private String xuidurl;
	public void setXuidurl(String pURL){
		this.xuidurl = pURL;
	}
	public JsonObject getTextureId() {


		// get xuid as json object and convert xuid into Floodgate uuid
		JsonObject getXuid;
		getXuid = webRequest("https://api.geysermc.org/v2/xbox/xuid/"+username);
		int xuid =  getXuid.getAsInt();

		if (getXuid.get("xuid") != null) {
			if (getXuid.has("xuid")) {
				//get texture_id
				JsonObject getJson = (webRequest("https://api.geysermc.org/v2/skin/"+xuid));
				//return texture_id aus getSkinJson
				JsonObject getSkinJson = getJson.getAsJsonObject("texture_id");
			}
			// if message gets returned = player wasn't found
			return null;
		}
		return textureid;
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



