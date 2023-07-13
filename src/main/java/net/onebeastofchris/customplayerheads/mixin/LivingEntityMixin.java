package net.onebeastofchris.customplayerheads.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.onebeastofchris.customplayerheads.CustomPlayerHeads;
import net.onebeastofchris.customplayerheads.texture.TextureUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executors;

/**
 * Credit for parts of the following code go to DakotaPride
 * <a href="https://github.com/DakotaPride/Incantation-Fabric/blob/master/src/main/java/net/dakotapride/incantation/mixin/LivingEntityMixin.java">...</a>
 **/

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow protected abstract void drop(DamageSource source);

    @Shadow protected abstract int computeFallDamage(float fallDistance, float damageMultiplier);

    @Shadow protected abstract void playBlockFallSound();

    @Inject(method = "dropLoot", at = @At("TAIL"))
    private void gph$dropHead(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if (this.livingEntity instanceof ServerPlayerEntity player && CustomPlayerHeads.config.isShouldDropHeadsOnDeath()) {

            Executors.newSingleThreadExecutor().execute(() -> {
                ItemStack skull = Items.PLAYER_HEAD.getDefaultStack();

            /*
            String temp = "";
            for (Property prop : player.getGameProfile().getProperties().values()) {
                CustomPlayerHeads.getLogger().error(prop.getName() + " " + prop.getValue());
                if (prop.getName().equals("textures")) {
                    temp = prop.getValue();
                    break;
                }
            }

            NbtCompound compound = TextureUtils.nbtFromTextureValue(player.getUuid(), temp , player.getEntityName());
            */
                NbtCompound compound = TextureUtils.nbtFromProfile(player.getGameProfile());
                skull.setNbt(compound);

                if (source.getAttacker() instanceof ServerPlayerEntity killer) {
                    if (CustomPlayerHeads.config.isShowLore()) {
                        // case: player killed by player, with lore
                        skull.setNbt(TextureUtils.addLore(compound, killer));
                    }
                    this.dropStack(skull);
                    return;
                }

                // case: player killed by non-player
                if (CustomPlayerHeads.config.isDropNonPlayerKillHeads()) {
                    this.dropStack(skull);
                }
            });
        }
    }

    private final LivingEntity livingEntity = (LivingEntity) (Object) this;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
}