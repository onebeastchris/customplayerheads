package net.onebeastofchris.customplayerheads.utils;

import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.onebeastofchris.customplayerheads.CustomPlayerHeads;

import java.util.List;

public class TextureUtils {

    public static Text customNameComponent(String name) {
        return Text.literal(getHeadName(nameFromProfile(name)));
    }

    private static String nameFromProfile(String name) {
        if (!CustomPlayerHeads.config.isShowFloodgatePrefix()) {
            return name.replace(FloodgateUtil.FloodgatePrefix(), "");
        }
        return name;
    }

    public static LoreComponent getLoreComponent(PlayerEntity player) {
        return new LoreComponent(List.of(getAttacker(player)));
    }

    private static String getHeadName(String name) {
        return CustomPlayerHeads.config.getName().replace("%player%", name);
    }

    private static Text getAttacker(PlayerEntity player) {
        String attackerName = nameFromProfile(player.getGameProfile().getName());
        String lore = CustomPlayerHeads.config.getLore().replace("%player%", attackerName);
        return Text.literal(lore).styled(style -> style.withItalic(true));
    }
}