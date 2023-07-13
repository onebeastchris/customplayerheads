package net.onebeastofchris.customplayerheads;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.onebeastofchris.customplayerheads.command.SkullCommand;
import net.onebeastofchris.customplayerheads.utils.CPHConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CustomPlayerHeads implements ModInitializer {
    private static Logger logger;
    List<Path> oldConfigPaths = List.of(Paths.get("config/customplayerheads.json"), Paths.get("geyserplayerheads.json"));
    Path newConfigPath = Paths.get("customplayerheads.conf");

    public static CPHConfig config;

    @Override
    public void onInitialize() {

        logger = LoggerFactory.getLogger("gph");
        logger.info("CustomPlayerHeads starting now");

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(FabricLoader.getInstance().getConfigDir().resolve(newConfigPath))
                .defaultOptions(opts -> opts.header("CustomPlayerHeads Configuration"))
                .prettyPrinting(true)
                .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(CPHConfig.class);
            loader.save(node);
        } catch (ConfigurateException e) {
            getLogger().error("Error while loading config!" + e.getMessage());
            throw new RuntimeException(e);
        }

        //old configs check!
        for (Path oldConfigPath : oldConfigPaths) {
            if (oldConfigPath.toFile().exists()) {
                logger.warn("You have an old config file! Please delete it and use the new config file, which is located at /config/customplayerheads.conf");
            }
        }

        if (config.isCommandEnabled()) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> SkullCommand.register(dispatcher));
            logger.debug("/getskull command enabled!");
        }
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