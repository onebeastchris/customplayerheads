package net.onebeastofchris.geyserplayerheads.utils;

import lombok.Data;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import net.onebeastofchris.geyserplayerheads.texture.TextureUtils;

import java.util.UUID;
@Data
public class HeadHolder {
    //head without lore
    private ItemStack head = new ItemStack(Items.PLAYER_HEAD);
    private boolean isBedrock;
    private String playerName;
    private UUID uuid;


    public HeadHolder(String playerName, UUID uuid, boolean isBedrock) {
        this.playerName = playerName.replace(FloodgateUtil.FloodgatePrefix(), "");
        this.uuid = uuid;
        this.isBedrock = isBedrock;

        String lookupID;
        if (isBedrock) {
            String xuid = String.valueOf(uuid.getLeastSignificantBits());
            lookupID = xuid.replace("-", "");

            if (GeyserPlayerHeads.config.isShowFloodgatePrefix()) {
                this.playerName = FloodgateUtil.FloodgatePrefix() + playerName;
            }
        } else {
            lookupID = uuid.toString().replace("-", "");
        }
        String textureID = PlayerUtils.getTextureID(lookupID, isBedrock);
        if (textureID != null) {
            String encoded = TextureUtils.getEncodedTextureID(textureID);
            head.setNbt(TextureUtils.getNbt(encoded, playerName));
        } else {
            GeyserPlayerHeads.getLogger().error("Failed to get texture ID for " + playerName + " (" + uuid + ")");
            head = null;
        }
    }

}