package net.onebeastofchris.geyserplayerheads.register;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.onebeastofchris.geyserplayerheads.command.SkullCommand;

public class CommandRegister {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> SkullCommand.register(dispatcher));
    }
}