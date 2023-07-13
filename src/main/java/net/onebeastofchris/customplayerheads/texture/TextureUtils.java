package net.onebeastofchris.customplayerheads.texture;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.onebeastofchris.customplayerheads.CustomPlayerHeads;
import net.onebeastofchris.customplayerheads.utils.FloodgateUtil;

import java.util.Base64;
import java.util.UUID;

public class TextureUtils {
    public static NbtCompound nbtFromTextureValue(UUID id, String texturevalue, String shownname) {
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
        skullownertag.putUuid("Id", id);
        nbtCompound.put("SkullOwner", skullownertag);
        displaytag.putString("Name", getItalicJsonText(shownname));
        nbtCompound.put("display", displaytag);

        return nbtCompound;
    }

    public static NbtCompound nbtFromProfile(GameProfile profile) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.put("SkullOwner", NbtHelper.writeGameProfile(nbtCompound, profile));

        NbtCompound displaytag = new NbtCompound();
        displaytag.putString("Name", getItalicJsonText(nameFromProfile(profile)));
        nbtCompound.put("display", displaytag);

        return nbtCompound;
    }

    private static String nameFromProfile(GameProfile profile) {
        if (CustomPlayerHeads.config.isShowFloodgatePrefix()) {
            return FloodgateUtil.FloodgatePrefix() + profile.getName();
        }
        return profile.getName();
    }

    public static NbtCompound addLore(NbtCompound nbt, PlayerEntity player) {
        NbtCompound display = nbt.getCompound("display");

        NbtList loreList = new NbtList();
        loreList.add(NbtString.of(getItalicJsonText(getAttacker(player))));

        display.put("Lore", loreList);
        nbt.put("display", display);

        return nbt;
    }

    private static String getAttacker(PlayerEntity player) {
        String attackerName = player.getEntityName();
        if (CustomPlayerHeads.config.isShowFloodgatePrefix()) {
            if (FloodgateUtil.isBedrockPlayer(player.getUuid(), player.getEntityName())) {
                return FloodgateUtil.FloodgatePrefix() + attackerName;
            }
        }
        return "Killed by " + attackerName;
    }
    public static String getItalicJsonText(String string) {
        return Text.Serializer.toJson(Text.literal(string).styled(style -> style.withItalic(true)));
    }

    public static String getEncodedTextureID(String textureID) {
        try {
            String toBeEncoded = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/" + textureID + "\"}}}";
            return Base64.getEncoder().encodeToString(toBeEncoded.getBytes());
        } catch (Exception e) {
            CustomPlayerHeads.debugLog("Error while encoding textureID" + e.getMessage());
            return null;
        }
    }
}