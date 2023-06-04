package net.onebeastofchris.geyserplayerheads.register;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.onebeastofchris.geyserplayerheads.events.ServerPlayerEvents;

public class EventRegister {
    public static void registerJoinEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEvents.onPlayerJoin(handler);
        });
    }

    //public static void registerLeaveEvent() {
    //    ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
    //        //maybe clear the cache here? not used for now.
    //        ServerPlayerEvents.onPlayerLeave(handler, server);
    //    });
    //}
}