package net.onebeastofchris.geyserplayerheads.utils;

public class ConfigOptions {
    public boolean showLore;
    public boolean dropNonPlayerKillHeads;
    public boolean commandEnabled;
    public int commandPermissionLevel;
    public boolean debug;
    public boolean includeFloodgatePrefixInNames;

    public boolean fakePlayerDropHeads;

    public ConfigOptions(boolean pLore, boolean pPlayerKillHeads, boolean pCommand, int pLevel, boolean pDebug, boolean pIncludeFloodgatePrefixInNames, boolean pFakePlayerDropHeads) {
        this.showLore = pLore;
        this.dropNonPlayerKillHeads = pPlayerKillHeads;
        this.commandEnabled = pCommand;
        this.commandPermissionLevel = pLevel;
        this.debug = pDebug;
        this.includeFloodgatePrefixInNames = pIncludeFloodgatePrefixInNames;
        this.fakePlayerDropHeads = pFakePlayerDropHeads;
    }
}
