package net.onebeastofchris.geyserplayerheads.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import net.onebeastofchris.geyserplayerheads.texture.TextureUtils;
import net.onebeastofchris.geyserplayerheads.utils.FloodgateUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Credit for parts of the following code go to DakotaPride
 * <a href="https://github.com/DakotaPride/Incantation-Fabric/blob/master/src/main/java/net/dakotapride/incantation/mixin/LivingEntityMixin.java">...</a>
 **/

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow protected abstract void drop(DamageSource source);

    @Inject(method = "dropLoot", at = @At("TAIL"))
    private void gph$dropHead(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if (this.livingEntity instanceof ServerPlayerEntity player && GeyserPlayerHeads.config.isShouldDropHeadsOnDeath()) {
            boolean isBedrock = FloodgateUtil.isBedrockPlayer(player.getUuid(), player.getEntityName());
            ItemStack skull = TextureUtils.getSkull(player.getUuid(), player.getEntityName(), isBedrock);

            if (skull != null) {
                ItemStack head = null;
                if (source.getAttacker() instanceof PlayerEntity killer) {
                    if (GeyserPlayerHeads.config.isShowLore()) {
                        // case: player killed by player, with lore
                        head = TextureUtils.addLore(skull, killer);
                    } else {
                        // case: player killed by player, no lore
                        head = skull;
                    }
                // case: player killed by non-player
                } else if (GeyserPlayerHeads.config.isDropNonPlayerKillHeads()) {
                    head = TextureUtils.addLore(skull, null);
                }

                // drop the head if exists
                if (head != null) {
                    this.dropStack(head);
                }
            }
        }
    }

    private final LivingEntity livingEntity = (LivingEntity) (Object) this;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
}