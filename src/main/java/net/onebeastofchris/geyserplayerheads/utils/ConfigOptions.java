package net.onebeastofchris.geyserplayerheads.utils;

public class ConfigOptions {
    public boolean showLore;
    public boolean dropNonPlayerKillHeads;
    public boolean commandEnabled;
    public int commandPermissionLevel;

    public ConfigOptions(boolean pLore, boolean pPlayerKillHeads, boolean pCommand, int pLevel){
        this.showLore = pLore;
        this.dropNonPlayerKillHeads = pPlayerKillHeads;
        this.commandEnabled = pCommand;
        this.commandPermissionLevel = pLevel;
    }
}
