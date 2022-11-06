package net.onebeastofchris.geyserplayerheads;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.onebeastofchris.geyserplayerheads.events.PlayerJoinEvent;
import net.onebeastofchris.geyserplayerheads.utils.UsernameValidation;

import java.util.UUID;
import java.util.concurrent.Executors;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class    getSkull {
    public static int getPermLevel() {
        return GeyserPlayerHeads.config.commandPermissionLevel;
    }

    public static LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(
                literal("getskull")
                        .requires(source -> source.hasPermissionLevel(getPermLevel()))
                        .then(
                                argument("JavaPlayer", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            PlayerManager playerManager = context.getSource().getServer().getPlayerManager();
                                            return CommandSource.suggestMatching(
                                                    playerManager.getPlayerList().stream()
                                                            .filter(player -> !UsernameValidation.isBedrockPlayer(player))
                                                            .map(PlayerEntity::getEntityName)
                                                    , builder);
                                        })
                                        .executes(getSkull::jRun)
                        )
                        .then(literal("bedrock")
                                .then(
                                        (argument("BedrockPlayer", StringArgumentType.greedyString()))
                                                .suggests((context, builder) -> {
                                                    PlayerManager playerManager = context.getSource().getServer().getPlayerManager();
                                                    return CommandSource.suggestMatching(
                                                            playerManager.getPlayerList().stream()
                                                                    .filter(UsernameValidation::isBedrockPlayer)
                                                                    .map(PlayerEntity::getEntityName)
                                                            , builder);
                                                })
                                                .executes(getSkull::bRun)
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
            return getJavaSkull(self, StringArgumentType.getString(context, "JavaPlayer"));
        }
    }

    private static int bRun(CommandContext<ServerCommandSource> context) {
        final PlayerEntity self = context.getSource().getPlayer();
        String bedrockPlayer = StringArgumentType.getString(context, "BedrockPlayer");
        if (self == null) {
            context.getSource().sendError(Text.of("You must be a player to use this command."));
            return 0;
        } else {
            PlayerManager playerManager = context.getSource().getServer().getPlayerManager();
            for (PlayerEntity player : playerManager.getPlayerList()) {
                if (player.getEntityName().equalsIgnoreCase(bedrockPlayer) && UsernameValidation.isBedrockPlayer(player)){
                    var head = new ItemStack(Items.PLAYER_HEAD);
                    head.setNbt(PlayerJoinEvent.getTextureID().get(player.getUuid()).getBedrockNbt(null, bedrockPlayer));
                    self.getInventory().insertStack(head);
                    self.sendMessage(Text.literal("Got the head of the Bedrock player " + bedrockPlayer).formatted(Formatting.GREEN));
                    return 1;
                }
                }
            }
            Executors.newSingleThreadExecutor().execute(() -> getBedrockSkull(self, bedrockPlayer));
            return 1;
        }

    public static int getJavaSkull(PlayerEntity self, String target) {
        if (UsernameValidation.validateJavaUsername(target)) {
            var head = new ItemStack(Items.PLAYER_HEAD);
            head.setNbt(getJavaNbt(target));
            self.getInventory().insertStack(head);
            self.sendMessage(Text.literal("Got the head of the Java player " + target).formatted(Formatting.GREEN));
            return 1;
        } else {
            self.sendMessage(Text.literal("Invalid Java player name").formatted(Formatting.RED));
            return 0;
        }
    }

    public static void getBedrockSkull(PlayerEntity self, String target) {
        long BedrockUserXuid = UsernameValidation.onlineBedrockPlayerLookup(target, self);
        if (BedrockUserXuid > 0) {
            UUID FloodgateUUID = new UUID(0, BedrockUserXuid);
            PlayerJoinEvent.addToMap(FloodgateUUID, target, BedrockUserXuid, true);
            PlayerJoinEvent.getTextureID().get(FloodgateUUID);
            if (!TextureApplier.isValidSkin()) {
                self.sendMessage(Text.literal("Failed to get the skin file of the Bedrock player. Ask " + target + " to join a Geyser + Floodgate server. " ).formatted(Formatting.RED));
                return;
            }
            var head = new ItemStack(Items.PLAYER_HEAD);
            head.setNbt(PlayerJoinEvent.getTextureID().get(FloodgateUUID).getBedrockNbt(null, target));
            self.getInventory().insertStack(head);
            self.sendMessage(Text.literal("Got the head of the Bedrock player " + target).formatted(Formatting.GREEN));
        }
    }

    public static NbtCompound getJavaNbt(String name) {

        NbtCompound c = new NbtCompound();
        NbtCompound c1 = new NbtCompound();

        c1.putString("Name", Text.Serializer.toJson(Text.literal(name + "'s head").styled(style -> style.withItalic(false))));
        c.putString("SkullOwner", name);
        c.put("display", c1);
        return c;
    }
}