package net.onebeastofchris.geyserplayerheads;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.onebeastofchris.geyserplayerheads.events.PlayerJoinEvent;
import net.onebeastofchris.geyserplayerheads.utils.FloodgateUser;

import java.util.UUID;
import java.util.concurrent.Executors;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class getSkull {

        public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
            dispatcher.register(literal("getskull")
                    .requires(source -> source.hasPermissionLevel(2)) // Must be a game master to use the command. Command will not show up in tab completion or execute to non operators or any operator that is permission level 1.
                    .then(literal("bedrock"))
                            .then(argument("username", greedyString())
                                    .executes(ctx -> getBedrockSkull(ctx.getSource(), getString(ctx, "username")))) // You can deal with the arguments out here and pipe them into the command.
                            .executes(ctx -> noArgs(ctx))
            );
        }

        public static int getBedrockSkull(ServerCommandSource source, String message) throws CommandSyntaxException {
            final PlayerEntity self = source.getPlayer();
            self.sendMessage(Text.literal("winner winner chicken dinner"));
            var ref = new Object() {
                long xuid;
            };
            Executors.newSingleThreadExecutor().execute(() ->
                    ref.xuid = TextureApplier.getXuid(message.replace(FloodgateUser.FloodgatePrefix(), "")));
            if (ref.xuid > 0) {
                UUID a = new UUID(0, ref.xuid);
                PlayerJoinEvent.addToMap(a, message);
                var head = Items.PLAYER_HEAD.getDefaultStack();
                head.setNbt(PlayerJoinEvent.getTextureID().get(a).getBedrockNbt(null));
                if (!self.getInventory().insertStack(head)){
                    throw new SimpleCommandExceptionType(Text.translatable("inventory.isfull")).create();
                }
            } else if (ref.xuid == 0){
                self.sendMessage(Text.literal("Bedrock player not found in Geyser's Global API. Check for spelling, and make sure they have joined a Geyser server before."));
            } else self.sendMessage(Text.literal("Something went wrong..."));

            return 1; // Success
        }
        public static int noArgs(CommandContext<ServerCommandSource> ctx){
            final ServerCommandSource source = ctx.getSource();
            final PlayerEntity self = source.getPlayer(); // If not a player than the command ends
            self.sendMessage(Text.literal("No user specified!"));
            return 1;
        }
}