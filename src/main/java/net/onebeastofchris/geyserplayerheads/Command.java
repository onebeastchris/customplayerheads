package net.onebeastofchris.geyserplayerheads;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class Command {
    public static Command register(CommandDispatcher<ServerCommandSource> dispatcher) { // You can also return a LiteralCommandNode for use with possible redirects
        return dispatcher.register(literal("giveMeDiamond")
                .executes(ctx -> giveDiamond(ctx)));
    }

    public int giveDiamond(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        final ServerCommandSource source = ctx.getSource();

        final PlayerEntity self = source.getPlayer(); // If not a player than the command ends

        if(!((ServerPlayerEntity) self).getInventory().insertStack(new ItemStack(Items.DIAMOND))){
            throw new SimpleCommandExceptionType(Text.translatable("inventory.isfull")).create();
        }

        return 1;
    }
}