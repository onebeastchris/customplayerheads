package net.onebeastofchris.geyserplayerheads.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import net.onebeastofchris.geyserplayerheads.TextureApplier;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;

public class PlayerJoinEvent {

    private static final HashMap<UUID, TextureApplier> textureMap = new HashMap<>();

    public static void onSpawn(World world, Entity entity) {

        if (world.isClient) {
            return;
        }

        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        // Start new thread, so we don't lock up main thread if we get a bad server response.
        Executors.newSingleThreadExecutor().execute(() ->
                addToMap(player.getUuid(), player.getEntityName(), -1, false));
    }

    public static HashMap<UUID, TextureApplier> getTextureID() {
        return textureMap;
    }

    public static void addToMap(UUID uuid, String playerName, long pXUID, boolean isBedrock) {
        if ((!PlayerJoinEvent.getTextureID().containsKey(uuid))) {
            GeyserPlayerHeads.debugLog("PlayerJoinEvent::addToMap: " + playerName + " added to textureMap");
            textureMap.put(uuid, new TextureApplier(playerName, uuid, pXUID, isBedrock));
        } else {
            GeyserPlayerHeads.debugLog("PlayerJoinEvent::addToMap: " + playerName + " already in textureMap");
        }
    }
}

