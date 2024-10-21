package top.alazeprt.sls;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.alazeprt.sls.config.SLSConfig;
import top.alazeprt.sls.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerListSync implements ModInitializer {
	public static final String MOD_ID = "serverlistsync";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final List<JsonElement> serverInfosJson = new ArrayList<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Loading configuration ...");
        try {
            SLSConfig.load();
        } catch (IOException e) {
            LOGGER.error("Error occurred while loading configuration: {}", e.toString());
        }
        LOGGER.info("Downloading server information ...");
		JsonObject result = HttpUtil.get();
		LOGGER.info(new Gson().toJson(result));
		if (result.get("error") == null) {
			try {
				for (JsonElement element : result.getAsJsonArray("servers")) {
					serverInfosJson.add(element);
				}
			} catch (Exception e) {
				LOGGER.error("Error occurred while parsing server information: {} ; Exception: {}", new Gson().toJson(result), e);
			}
		} else {
            LOGGER.error("Error occurred while downloading server information: {}", new Gson().toJson(result));
		}
		LOGGER.info("ServerListSync Mod Enabled!");
	}
}