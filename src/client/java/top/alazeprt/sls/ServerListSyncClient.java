package top.alazeprt.sls;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.ServerInfo;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ServerListSyncClient implements ClientModInitializer {

    public static final List<ServerInfo> serverInfos = new ArrayList<>();

    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean updateData = false;

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    public static void updateServerInfos() {
        updateData = true;
        for (JsonElement element : ServerListSync.serverInfosJson) {
            LOGGER.info("Loading element: " + new Gson().toJson(element));
            serverInfos.add(new ServerInfo(element.getAsJsonObject().get("name").getAsString(),
                    element.getAsJsonObject().get("address").getAsString(), ServerInfo.ServerType.OTHER));
        }
    }
}