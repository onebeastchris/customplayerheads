package net.onebeastofchris.geyserplayerheads;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.onebeastofchris.geyserplayerheads.events.PlayerJoinEvent;
import net.onebeastofchris.geyserplayerheads.utils.ConfigOptions;
import net.onebeastofchris.geyserplayerheads.utils.ModCommandRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class GeyserPlayerHeads implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	private static Logger logger;
	public static ConfigOptions config = new ConfigOptions(true, true, false, 2);
	public Gson GsonConfigFile = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
	Path configPath = Paths.get("config/geyserplayerheads.json");

	public void dataWriter() {
		try{
			if (configPath.toFile().exists()) {
				config = GsonConfigFile.fromJson(new String(Files.readAllBytes(configPath)), ConfigOptions.class);
			} else {
				Files.write(configPath, Collections.singleton(GsonConfigFile.toJson(config)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger = LoggerFactory.getLogger("gph");
		logger.info("GeyserPlayerHeads starting now");
		registerEvents();
		dataWriter();
		/*if (GeyserPlayerHeads.config.commandEnabled) {
			ModCommandRegister.registerCommand();
			logger.info("/getskull command enabled!");
			}
		*/
		if (!FabricLoader.getInstance().isModLoaded("floodgate")){
			logger.info("Floodgate is not installed! We will check the bedrock . prefix.");
		}
	}

	private void registerEvents() {
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerWorld world) -> PlayerJoinEvent.onSpawn(world, entity));
	}

	public static Logger getLogger() {
		return logger;
	}
}

