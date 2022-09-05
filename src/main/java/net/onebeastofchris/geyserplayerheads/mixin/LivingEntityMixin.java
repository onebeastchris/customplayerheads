package net.onebeastofchris.geyserplayerheads.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Credit for the following code goes to DakotaPride
 * <a href="https://github.com/DakotaPride/Incantation-Fabric/blob/master/src/main/java/net/dakotapride/incantation/mixin/LivingEntityMixin.java">...</a>
 * **/

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
//	@Shadow protected abstract void dropInventory();

//	@Shadow protected abstract void drop(DamageSource source);

//	private final LivingEntity livingEntity = (LivingEntity) (Object) this;
//	private final LivingEntity attacker = livingEntity.getAttacker();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	private void dropHead() {
		dropItem(Items.PLAYER_HEAD);
	}

	@Inject(method = "dropLoot", at = @At("TAIL"))
	private void gph$dropHead(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		dropHead();
	}

}
