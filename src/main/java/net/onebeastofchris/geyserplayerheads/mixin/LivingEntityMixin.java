package net.onebeastofchris.geyserplayerheads.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import net.onebeastofchris.geyserplayerheads.TextureApplier;
import net.onebeastofchris.geyserplayerheads.utils.FloodgateUser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Credit for parts of the following  code to DakotaPride
 * <a href="https://github.com/DakotaPride/Incantation-Fabric/blob/master/src/main/java/net/dakotapride/incantation/mixin/LivingEntityMixin.java">...</a>
 * **/

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Inject(method = "dropLoot", at = @At("TAIL"))
	private void gph$dropHead(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (this.livingEntity instanceof PlayerEntity player) {
			String playerName = player.getEntityName();
			GeyserPlayerHeads.getLogger().info(playerName + " died at " + player.getBlockPos().toString());
				if (playerName.startsWith(".") || FloodgateUser.isFloodgatePlayer(player.getUuid())) {
					String clearName = playerName.replace(".", "");
					TextureApplier c = new TextureApplier(clearName);
					var head = Items.PLAYER_HEAD.getDefaultStack();
					head.setNbt(c.getBedrockNbt());
					dropStack(head);
				} else {
					TextureApplier d = new TextureApplier(playerName);
					var head = Items.PLAYER_HEAD.getDefaultStack();
					head.setNbt(d.getJavaNbt());
					GeyserPlayerHeads.getLogger().info(d.getJavaNbt().asString());
					dropStack(head);

			}
		}
	}

	private String gph$getAttacker(LivingEntity pEntity){
		if (pEntity instanceof PlayerEntity player){
			return player.getEntityName();
		} else return null;
	}

//	@Shadow protected abstract void drop(DamageSource source);
	private final LivingEntity livingEntity = (LivingEntity) (Object) this;
	private final LivingEntity attacker = livingEntity.getAttacker();
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

}
