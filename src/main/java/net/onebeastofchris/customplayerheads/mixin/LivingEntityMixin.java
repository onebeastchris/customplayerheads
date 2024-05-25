package net.onebeastofchris.customplayerheads.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.onebeastofchris.customplayerheads.CustomPlayerHeads;
import net.onebeastofchris.customplayerheads.utils.TextureUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Credit for parts of the following code go to DakotaPride
 * <a href="https://github.com/DakotaPride/Incantation-Fabric/blob/master/src/main/java/net/dakotapride/incantation/mixin/LivingEntityMixin.java">...</a>
 **/
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Unique
    private final LivingEntity livingEntity = (LivingEntity) (Object) this;

    @Inject(method = "dropLoot", at = @At("TAIL"))
    private void gph$dropHead(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if (this.livingEntity instanceof ServerPlayerEntity player && CustomPlayerHeads.config.isShouldDropHeadsOnDeath()) {
            ItemStack skull = Items.PLAYER_HEAD.getDefaultStack();

            skull.set(DataComponentTypes.PROFILE, new ProfileComponent(player.getGameProfile()));
            skull.set(DataComponentTypes.CUSTOM_NAME, TextureUtils.customNameComponent(player.getNameForScoreboard()));

            if (source.getAttacker() instanceof ServerPlayerEntity killer) {
                if (CustomPlayerHeads.config.isShowLore()) {
                    skull.set(DataComponentTypes.LORE, TextureUtils.getLoreComponent(killer));
                }
                this.dropStack(skull);
                return;
            }

            // case: player killed by non-player
            if (CustomPlayerHeads.config.isDropNonPlayerKillHeads()) {
                this.dropStack(skull);
            }
        }
    }

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
}