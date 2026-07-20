package top.alazeprt.sls;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.multiplayer.ServerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.alazeprt.sls.config.SLSConfig;
import top.alazeprt.sls.util.HttpUtil;
import top.alazeprt.sls.util.ServerOrder;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerListSyncClient implements ClientModInitializer {

    public static final List<ServerData> serverInfos = new CopyOnWriteArrayList<>();

    public static final List<ServerData> lastServerInfo = new ArrayList<>();

    public static boolean updateData = false;

    private static volatile boolean running = true;

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListSyncClient.class);

    public static Thread updateThread = new Thread(() -> {
        final Logger threadLogger = LoggerFactory.getLogger("ServerList-Sync-Thread");
        while (running) {
            try {
                Thread.sleep(1000L * SLSConfig.updatePeriod);
            } catch (InterruptedException e) {
                break;
            }
            if (!running) break;
            threadLogger.info("Downloading server information ...");
            JsonObject result = HttpUtil.get();
            if (result.get("error") == null) {
                try {
                    ServerListSync.serverInfosJson.clear();
                    for (JsonElement element : result.getAsJsonArray("servers")) {
                        JsonObject obj = element.getAsJsonObject();
                        if (obj.has("ip") && obj.has("name")
                                && !obj.get("ip").isJsonNull() && !obj.get("name").isJsonNull()) {
                            ServerListSync.serverInfosJson.add(element);
                            continue;
                        }
                        threadLogger.warn("Error occurred while parsing server information: {} ; ip or name is empty", new Gson().toJson(element));
                    }
                    serverInfos.clear();
                    updateServerInfos();
                } catch (Exception e) {
                    threadLogger.error("Error occurred while parsing server information: {} ; Exception: {}", new Gson().toJson(result), e);
                }
            } else {
                threadLogger.error("Error occurred while downloading server information: {}", new Gson().toJson(result));
            }
        }
        threadLogger.info("ServerList-Sync-Thread stopped.");
    }, "ServerList-Sync-Thread");

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        updateThread.setDaemon(true);
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            running = false;
            updateThread.interrupt();
        });
    }

    public static synchronized void updateServerInfos() {
        List<ServerData> tempList = new ArrayList<>();
        for (JsonElement element : ServerListSync.serverInfosJson) {
            tempList.add(new ServerData(element.getAsJsonObject().get("name").getAsString(),
                    element.getAsJsonObject().get("ip").getAsString(), ServerData.Type.OTHER));
        }
        if (SLSConfig.order.equals(ServerOrder.REVERSE)) Collections.reverse(tempList);
        if (SLSConfig.order.equals(ServerOrder.ALPHABETICAL)) {
            tempList.sort(Comparator.comparing(o -> o.name));
        }
        serverInfos.clear();
        serverInfos.addAll(tempList);
        if (!updateData) {
            updateThread.start();
        }
        updateData = true;
    }
}