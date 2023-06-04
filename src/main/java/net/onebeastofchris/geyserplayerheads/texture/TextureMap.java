package net.onebeastofchris.geyserplayerheads.texture;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.onebeastofchris.geyserplayerheads.utils.FloodgateUtil;
import net.onebeastofchris.geyserplayerheads.utils.HeadHolder;
import net.onebeastofchris.geyserplayerheads.utils.PlayerUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TextureMap {
    private static final Cache<UUID, HeadHolder> texture_cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build();

    public static HeadHolder getEntry(UUID uuid) {
        return texture_cache.getIfPresent(uuid);
    }

    public static void addEntry(String playerName, UUID uuid, boolean isBedrock) {
        texture_cache.put(uuid, new HeadHolder(playerName, uuid, isBedrock));
        if (isBedrock) {
            PlayerUtils.bedrockUUIDmap.put(playerName.toLowerCase().replace(FloodgateUtil.FloodgatePrefix(), ""), uuid);
        } else {
            PlayerUtils.javaUUIDmap.put(playerName, uuid);
        }
    }

    public static void nukeEmAll() {
        texture_cache.invalidateAll();
        PlayerUtils.javaUUIDmap.clear();
        PlayerUtils.bedrockUUIDmap.clear();
    }
}