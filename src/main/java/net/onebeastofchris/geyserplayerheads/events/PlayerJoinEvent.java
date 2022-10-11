package net.onebeastofchris.geyserplayerheads.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
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
                textureMap.put(player.getUuid(), new TextureApplier(player.getEntityName(), player.getUuid())));
    }

    public static HashMap<UUID, TextureApplier> getTextureID() {
        return textureMap;
    }

    public static void addToMap(UUID uuid, String playerName) {
        if ((!PlayerJoinEvent.getTextureID().containsKey(uuid))) {
            Executors.newSingleThreadExecutor().execute(() ->
                    textureMap.put(uuid, new TextureApplier(playerName, uuid)));
        }
    }
}

