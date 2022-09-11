package net.onebeastofchris.geyserplayerheads;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.onebeastofchris.geyserplayerheads.events.PlayerJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeyserPlayerHeads implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	private static Logger logger;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerEvents();
		logger = LoggerFactory.getLogger("gph");
		logger.info("GeyserPlayerHeads starting now");
	}

	private void registerEvents() {
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerWorld world) -> PlayerJoinEvent.onSpawn(world, entity));
	}

	public static Logger getLogger() {
		return logger;
	}
}

