package net.onebeastofchris.geyserplayerheads.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.onebeastofchris.geyserplayerheads.GeyserPlayerHeads;
import net.onebeastofchris.geyserplayerheads.TextureApplier;

public class UsernameValidation {

    //credit for the following validation function goes to PaperMC, specifically to Spottedleaf
    //https://github.com/PaperMC/Paper/commit/4bf2aef74578ae3352df70f94e017fa3cf06c4e1#diff-1abb91afa2bfaa192351ca227d61fbd5b6c4949eadf67a8b4e67bdefcf71c136
    public static boolean validateJavaUsername(String in) {
        if (in == null || in.isEmpty() || in.length() > 16) {
            return false;
        }
        for (int i = 0, len = in.length(); i < len; ++i) {
            char c = in.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_')) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isBedrockPlayer(PlayerEntity player) {
        return player.getDisplayName().toString().startsWith(".") || FloodgateUser.isFloodgatePlayer(player.getUuid());
    }

    public static long onlineBedrockPlayerLookup(String string, PlayerEntity self) {
        long xuid;
        String playerName = string.replace(FloodgateUser.FloodgatePrefix(), "");

        xuid = TextureApplier.getXuid(playerName);
        GeyserPlayerHeads.debugLog("XUID: " + xuid);
        if (xuid > 0) {
            return xuid;
        } else if (xuid == -2) {
            self.sendMessage(Text.literal("Invalid Bedrock player name.").formatted(Formatting.RED));
        } else if (xuid == -3) {
            self.sendMessage(Text.literal("That Bedrock player either does not exist, or has not joined a Geyser server yet.").formatted(Formatting.RED));
        } else {
            self.sendMessage(Text.literal("An unknown error occurred. Or were you spamming symbols?").formatted(Formatting.RED));
        }
        return -1;
    }

    public static boolean isRealPlayer(PlayerEntity player){
        if (GeyserPlayerHeads.config.fakePlayersDropHeads) {
            return true;
        } else return player.getClass() == ServerPlayerEntity.class;

    }
}
