package top.alazeprt.sls;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.alazeprt.sls.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class ServerListSyncClient implements ClientModInitializer {

    public static final String MOD_ID = "serverlistsync";

    public static final List<ServerInfo> serverInfos = new ArrayList<>();

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        LOGGER.info("Downloading server information ...");
        JsonObject result = HttpUtil.get();
        if (!result.get("error").isJsonNull()) {
            for (JsonElement element : result.getAsJsonArray("servers")) {
                serverInfos.add(new ServerInfo(element.getAsJsonObject().get("name").getAsString(),
                        element.getAsJsonObject().get("address").getAsString(), ServerInfo.ServerType.OTHER));
            }
        }
    }
}