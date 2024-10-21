package top.alazeprt.sls;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.alazeprt.sls.config.SLSConfig;
import top.alazeprt.sls.util.HttpUtil;
import top.alazeprt.sls.util.ServerOrder;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerListSyncClient implements ClientModInitializer {

    public static final List<ServerInfo> serverInfos = new ArrayList<>();

    public static final List<ServerInfo> lastServerInfo = new ArrayList<>();

    public static boolean updateData = false;

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListSyncClient.class);

    public static Thread updateThread = new Thread(() -> {
        final Logger threadLogger = LoggerFactory.getLogger("ServerList-Sync-Thread");
        while (true) {
            try {
                Thread.sleep(1000L * SLSConfig.updatePeriod);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threadLogger.info("Downloading server information ...");
            JsonObject result = HttpUtil.get();
            if (result.get("error") == null) {
                try {
                    ServerListSync.serverInfosJson.clear();
                    for (JsonElement element : result.getAsJsonArray("servers")) {
                        ServerListSync.serverInfosJson.add(element);
                    }
                    synchronized (serverInfos) { serverInfos.clear(); }
                    updateServerInfos();
                } catch (Exception e) {
                    threadLogger.error("Error occurred while parsing server information: {} ; Exception: {}", new Gson().toJson(result), e);
                }
            } else {
                threadLogger.error("Error occurred while downloading server information: {}", new Gson().toJson(result));
            }
        }
    }, "ServerList-Sync-Thread");

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    public static synchronized void updateServerInfos() {
        for (JsonElement element : ServerListSync.serverInfosJson) {
            serverInfos.add(new ServerInfo(element.getAsJsonObject().get("name").getAsString(),
                    element.getAsJsonObject().get("address").getAsString(), false));
        }
        if (SLSConfig.order.equals(ServerOrder.REVERSE)) Collections.reverse(serverInfos);
        if (SLSConfig.order.equals(ServerOrder.ALPHABETICAL)) {
            serverInfos.sort(Comparator.comparing(o -> o.name));
        }
        if (!updateData) {
            updateThread.start();
        }
        updateData = true;
    }
}