package net.onebeastofchris.customplayerheads;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.onebeastofchris.customplayerheads.command.SkullCommand;
import net.onebeastofchris.customplayerheads.utils.CPHConfig;
import net.onebeastofchris.customplayerheads.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomPlayerHeads implements ModInitializer {
    @Getter
    private static Logger logger;
    @Getter
    private static WebUtil webUtil;
    private final Path configPath = Paths.get("customplayerheads.conf");
    public static CPHConfig config;

    @Override
    public void onInitialize() {

        logger = LoggerFactory.getLogger("customplayerheads");
        logger.info("CustomPlayerHeads starting now");

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(FabricLoader.getInstance().getConfigDir().resolve(configPath))
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

        if (config.isCommandEnabled()) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> SkullCommand.register(dispatcher));
            logger.debug("/getskull command enabled!");
        }

        webUtil = new WebUtil();
    }

    public static void debugLog(String message) {
        if (config.isDebug()) {
            logger.info(message);
        }
    }
}