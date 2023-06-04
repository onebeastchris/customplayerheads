package net.onebeastofchris.geyserplayerheads;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.onebeastofchris.geyserplayerheads.register.CommandRegister;
import net.onebeastofchris.geyserplayerheads.register.EventRegister;
import net.onebeastofchris.geyserplayerheads.utils.GPHConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GeyserPlayerHeads implements ModInitializer {
    private static Logger logger;

    Path oldConfigPath = Paths.get("config/geyserplayerheads.json");
    Path newConfigPath = Paths.get("geyserplayerheads.conf");

    public static GPHConfig config;

    @Override
    public void onInitialize() {

        logger = LoggerFactory.getLogger("gph");
        logger.info("GeyserPlayerHeads starting now");

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(FabricLoader.getInstance().getConfigDir().resolve(newConfigPath))
                .defaultOptions(opts -> opts.header("GeyserPlayerHeads Configuration"))
                .prettyPrinting(true)
                .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(GPHConfig.class);
            loader.save(node);
        } catch (ConfigurateException e) {
            getLogger().error("Error while loading config!" + e.getMessage());
            throw new RuntimeException(e);
        }
        //old config check!
        if (oldConfigPath.toFile().exists()) {
            logger.warn("You are using the old config file! Please delete it and use the new config file, which is located at config/geyserplayerheads.conf");
        }

        if (config.isCommandEnabled()) {
            CommandRegister.registerCommand();
            logger.info("/getskull command enabled!");
        }
        if (!FabricLoader.getInstance().isModLoaded("floodgate")) {
            logger.info("Floodgate is not installed! We will check the bedrock . prefix, or the UUID version.");
        }

        register();
    }
    private void register() {
        EventRegister.registerJoinEvent();
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void debugLog(String message) {
        if (config.isDebug()) {
            logger.info(message);
        }
    }
}