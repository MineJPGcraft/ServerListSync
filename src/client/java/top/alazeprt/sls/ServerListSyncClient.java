package top.alazeprt.sls;

import com.google.gson.JsonElement;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.alazeprt.sls.config.SLSConfig;
import top.alazeprt.sls.util.ServerOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServerListSyncClient implements ClientModInitializer {

    public static final List<ServerInfo> serverInfos = new ArrayList<>();

    public static boolean updateData = false;

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListSyncClient.class);

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    public static void updateServerInfos() {
        updateData = true;
        for (JsonElement element : ServerListSync.serverInfosJson) {
            serverInfos.add(new ServerInfo(element.getAsJsonObject().get("name").getAsString(),
                    element.getAsJsonObject().get("address").getAsString(), ServerInfo.ServerType.OTHER));
        }
        if (SLSConfig.order.equals(ServerOrder.REVERSE)) Collections.reverse(serverInfos);
        if (SLSConfig.order.equals(ServerOrder.ALPHABETICAL)) {
            serverInfos.sort(Comparator.comparing(o -> o.name));
        }
        serverInfos.forEach(serverInfo -> LOGGER.info("Client: {} {}", serverInfo.name, serverInfo.address));
    }
}