package net.onebeastofchris.geyserplayerheads.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static java.net.http.HttpClient.newHttpClient;

/**
 * Credit for the following code goes to DakotaPride
 * <a href="https://github.com/DakotaPride/Incantation-Fabric/blob/master/src/main/java/net/dakotapride/incantation/mixin/LivingEntityMixin.java">...</a>
 * **/

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Inject(method = "dropLoot", at = @At("TAIL"))
	private void gph$dropHead(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (this.livingEntity instanceof PlayerEntity player) {
			String playerName = String.valueOf(player.getEntityName());
			String playerDeathPos = String.valueOf(player.getBlockPos());
			GeyserPlayerHeads.LOGGER.info(playerName + " died at " + playerDeathPos);

			if (!playerName.contains(".")) {
					String textureID = gph$getTextureId(gph$getXuid("YeloBotanist"));
					String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/"+ textureID + "\"}}}";
					String encoded = Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
					//dropItem(Items.PLAYER_HEAD.getDefaultStack().setNbt("SkullOwner", ););
					GeyserPlayerHeads.LOGGER.info(encoded);
			} else {
				dropItem(Items.PLAYER_HEAD.getDefaultStack().setCustomName(Text.of("Steve")).getItem());
			}
		}
	}
//	@Shadow protected abstract void dropInventory();

//	@Shadow protected abstract void drop(DamageSource source);

	private final LivingEntity livingEntity = (LivingEntity) (Object) this;
//	private final LivingEntity attacker = livingEntity.getAttacker();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	public long gph$getXuid(String pUsername){
		var xuid = gph$webRequest("https://api.geysermc.org/v2/xbox/xuid/"+pUsername);
		if (xuid.has("xuid")){
			return xuid.get("xuid").getAsLong();
		} else if (xuid.has("message")){
			return 0;
		}
		return -1;
	}
	public String gph$getTextureId(long pXuid) {
		if (pXuid > 0) {
			JsonObject getJson = (gph$webRequest("https://api.geysermc.org/v2/skin/"+pXuid));
			return getJson.get("texture_id").getAsString();
		} else return null;
		// if message gets returned = player wasn't found
	}

	public JsonObject gph$webRequest(String pUrl){
		var client = newHttpClient();
		var request = HttpRequest.newBuilder()
				.uri(URI.create(pUrl))
				.GET()
				.build();
		HttpResponse<String> resp;
		try {
			resp = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		return new Gson().fromJson(resp.body(), JsonObject.class);
	}

}
