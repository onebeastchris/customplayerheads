package net.onebeastofchris.geyserplayerheads.texture;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import net.onebeastofchris.geyserplayerheads.utils.FloodgateUtil;
import net.onebeastofchris.geyserplayerheads.utils.HeadHolder;

import java.util.Base64;
import java.util.UUID;

public class TextureUtils {
    public static NbtCompound getNbt(String texturevalue, String shownname) {
        NbtCompound nbtCompound = new NbtCompound();
        NbtCompound skullownertag = new NbtCompound();
        NbtCompound texturetag = new NbtCompound();
        NbtList texturelist = new NbtList();
        NbtCompound valuetag = new NbtCompound();
        NbtCompound displaytag = new NbtCompound();

        valuetag.putString("Value", texturevalue);
        texturelist.add(valuetag);
        texturetag.put("textures", texturelist);
        skullownertag.put("Properties", texturetag);
        skullownertag.putIntArray("Id", new int[]{1, 1, 1, 1});
        nbtCompound.put("SkullOwner", skullownertag);
        displaytag.putString("Name", getItalicJsonText(shownname));
        nbtCompound.put("display", displaytag);

        return nbtCompound;
    }

    public static ItemStack addLore(ItemStack itemStack, PlayerEntity player) {
        NbtCompound nbt = itemStack.getNbt();

        assert nbt != null;
        NbtCompound display = nbt.getCompound("display");
        NbtList loreList = new NbtList();
        loreList.add(NbtString.of(getItalicJsonText(getAttacker(player))));
        display.put("Lore", loreList);
        nbt.put("display", display);
        itemStack.setNbt(nbt);

        return itemStack;
    }

    private static String getAttacker(PlayerEntity player) {
        String attackerName = player.getEntityName();
        if (GeyserPlayerHeads.config.isShowFloodgatePrefix()) {
            if (FloodgateUtil.isBedrockPlayer(player.getUuid(), player.getEntityName())) {
                return FloodgateUtil.FloodgatePrefix() + attackerName;
            }
        }
        return attackerName;
    }
    public static String getItalicJsonText(String a) {
        return Text.Serializer.toJson(Text.literal(a).styled(style -> style.withItalic(true)));
    }
    public static String getEncodedTextureID(String textureID) {
        try {
            String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/" + textureID + "\"}}}";
            return Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
        } catch (Exception e) {
            GeyserPlayerHeads.debugLog("Error while encoding textureID" + e.getMessage());
            return null;
        }
    }
    public static ItemStack getSkull(UUID uuid, String name, boolean isBedrock) {
        HeadHolder holder = TextureMap.getEntry(uuid);
        if (holder != null) {
            GeyserPlayerHeads.debugLog("Found head for " + name + " in cache");
            return holder.getHead();
        } else {
            TextureMap.addEntry(name, uuid, isBedrock);
            GeyserPlayerHeads.debugLog("Added head for " + name + " to cache");
            GeyserPlayerHeads.debugLog(TextureMap.getEntry(uuid).toString());
            return TextureMap.getEntry(uuid).getHead();
        }
    }
}