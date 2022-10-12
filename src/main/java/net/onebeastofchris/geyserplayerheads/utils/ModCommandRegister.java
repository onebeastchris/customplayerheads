package net.onebeastofchris.geyserplayerheads.utils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.onebeastofchris.geyserplayerheads.getSkull;

public class ModCommandRegister {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> getSkull.register(dispatcher));
    }
}
