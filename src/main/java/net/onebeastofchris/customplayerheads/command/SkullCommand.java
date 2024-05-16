package net.onebeastofchris.customplayerheads.command;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.onebeastofchris.customplayerheads.CustomPlayerHeads;
import net.onebeastofchris.customplayerheads.utils.PlayerUtils;
import net.onebeastofchris.customplayerheads.utils.TextureUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SkullCommand {
    private static int getPermLevel() {
        return CustomPlayerHeads.config.getCommandPermissionLevel();
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("getskull")
                        .requires(source -> source.hasPermissionLevel(getPermLevel()))
                        .then(
                                argument("JavaPlayer", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            PlayerManager playerManager = context.getSource().getServer().getPlayerManager();
                                            return CommandSource.suggestMatching(
                                                    playerManager.getPlayerList().stream()
                                                            .filter(player -> !PlayerUtils.isBedrockPlayer(player))
                                                            .map(PlayerEntity::getNameForScoreboard)
                                                    , builder);
                                        })
                                        .executes(SkullCommand::jRun)
                        )
                        .then(literal("bedrock")
                                .then(
                                        (argument("BedrockPlayer", StringArgumentType.greedyString()))
                                                .suggests((context, builder) -> {
                                                    PlayerManager playerManager = context.getSource().getServer().getPlayerManager();
                                                    return CommandSource.suggestMatching(
                                                            playerManager.getPlayerList().stream()
                                                                    .filter(PlayerUtils::isBedrockPlayer)
                                                                    .map(PlayerEntity::getNameForScoreboard)
                                                            , builder);
                                                })
                                                .executes(SkullCommand::bRun)
                                ).executes((context -> {
                                    context.getSource().sendError(Text.of("You must specify a Bedrock player name."));
                                    return 0;
                                }))
                        )
                        .executes(context -> {
                            noArgs(context);
                            return 1;
                        })
        );
    }

    public static void noArgs(CommandContext<ServerCommandSource> ctx) {
        final ServerCommandSource source = ctx.getSource();
        if (source.getEntity() instanceof PlayerEntity self) {
            self.sendMessage(Text.literal("Usage: /getskull <JavaPlayer> or /getskull bedrock <BedrockPlayer>").formatted(Formatting.RED));
        } else {
            source.sendError(Text.of("You must be a player to use this command."));
        }
    }

    private static int jRun(CommandContext<ServerCommandSource> context) {
        final PlayerEntity self = context.getSource().getPlayer();
        if (self == null) {
            context.getSource().sendError(Text.of("You must be a player to use this command."));
            return 0;
        } else {
            try (ExecutorService service = Executors.newSingleThreadExecutor()) {
                service.execute(() -> getSkull(self, StringArgumentType.getString(context, "JavaPlayer"), false));
            }
            return 1;
        }
    }

    private static int bRun(CommandContext<ServerCommandSource> context) {
        final PlayerEntity self = context.getSource().getPlayer();
        if (self == null) {
            context.getSource().sendError(Text.of("You must be a player to use this command."));
            return 0;
        } else {
            try (ExecutorService service = Executors.newSingleThreadExecutor()) {
                service.execute(() -> getSkull(self, StringArgumentType.getString(context, "BedrockPlayer"), true));
            }
            return 1;
        }
    }

    public static void getSkull(PlayerEntity self, String target, boolean isBedrock) {
        String lookup;
        ItemStack head = Items.PLAYER_HEAD.getDefaultStack();

        try {
            if (isBedrock) {
                lookup = PlayerUtils.xuidLookup(target, self);
                if (lookup == null) {
                    return; // already warned
                }
                UUID uuid = PlayerUtils.uuidFromXuid(lookup);

                PropertyMap textures = PlayerUtils.getTextures(lookup, true);
                head.set(DataComponentTypes.PROFILE, new ProfileComponent(Optional.of(target), Optional.of(uuid), textures));
            } else {
                UUID uuid = PlayerUtils.fromUuidString(Objects.requireNonNull(PlayerUtils.getJavaUUID(target)));
                head.set(DataComponentTypes.PROFILE, new ProfileComponent(new GameProfile(uuid, target))); // let java server fetch texture?
            }
        } catch (Exception e) {
            if (isBedrock) {
                self.sendMessage(Text.literal("Failed to get the skin file of the Bedrock player. Ask " + target + " to join a Geyser + Floodgate server. ").formatted(Formatting.RED));
            } else {
                self.sendMessage(Text.literal("Failed to get the skin file of the Java player.").formatted(Formatting.RED));
            }
            CustomPlayerHeads.getLogger().debug(e.getMessage(), e);
            return;
        }

        head.set(DataComponentTypes.CUSTOM_NAME, TextureUtils.customNameComponent(target));

        self.getInventory().insertStack(head);
        self.sendMessage(Text.literal("Got the head of the player: " + target).formatted(Formatting.GREEN));
    }
}