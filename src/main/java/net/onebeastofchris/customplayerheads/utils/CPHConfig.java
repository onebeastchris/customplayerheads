package net.onebeastofchris.customplayerheads.utils;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;


@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public final class CPHConfig {

    @Comment("Should the heads drop on death?")
    private boolean shouldDropHeadsOnDeath = true;

    @Comment("Whether to show lore \"killed by <player>\" on heads dropped by players.")
    private boolean showLore = true;

    @Comment("Whether non-player-deaths drop heads. (e.g. mobs, explosions, fall damage, etc.)")
    private boolean dropNonPlayerKillHeads = false;

    @Comment("Whether to enable the /getskull command to allow player with the command permission level below to get a skull of a player.")
    private boolean commandEnabled = true;

    @Comment("The permission level required to use the /getskull command.")
    private int commandPermissionLevel = 2;

    @Comment("For Geyser/Floodgate users:" +
            "Whether to include the Floodgate prefix in the name of the head. (e.g. .steve instead of steve)")
    private boolean showFloodgatePrefix = true;

    @Comment("Whether to enable debug mode. This will log more information to the console.")
    private boolean debug = false;

    public boolean isShouldDropHeadsOnDeath() {
        return shouldDropHeadsOnDeath;
    }

    public boolean isShowLore() {
        return showLore;
    }

    public boolean isDropNonPlayerKillHeads() {
        return dropNonPlayerKillHeads;
    }

    public boolean isCommandEnabled() {
        return commandEnabled;
    }

    public int getCommandPermissionLevel() {
        return commandPermissionLevel;
    }

    public boolean isShowFloodgatePrefix() {
        return showFloodgatePrefix;
    }

    public boolean isDebug() {
        return debug;
    }


}