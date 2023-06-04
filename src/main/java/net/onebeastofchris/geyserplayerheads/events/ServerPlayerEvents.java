package net.onebeastofchris.geyserplayerheads.events;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import net.onebeastofchris.geyserplayerheads.texture.TextureMap;
import net.onebeastofchris.geyserplayerheads.utils.FloodgateUtil;

import java.util.concurrent.Executors;

public class ServerPlayerEvents {
    public static void onPlayerJoin(ServerPlayNetworkHandler handler) {
        ServerPlayerEntity player = handler.player;
        GeyserPlayerHeads.debugLog("Player " + player.getEntityName() + " joined the server, caching their head.");

        boolean isBedrock = FloodgateUtil.isBedrockPlayer(player.getUuid(), player.getEntityName());
        Executors.newSingleThreadExecutor().execute(() ->
                TextureMap.addEntry(player.getEntityName(), player.getUuid(), isBedrock)
        );
    }


    //public static void onPlayerLeave(ServerPlayNetworkHandler handler, MinecraftServer server) {
    //    ServerPlayerEntity player = handler.player;
    //    GeyserPlayerHeads.debugLog("Player " + player.getEntityName() + " left the server, removing their head from the cache.");
    //}
}