package net.onebeastofchris.geyserplayerheads.utils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommandRegister {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }
}
