package top.alazeprt.sls;

import com.google.gson.JsonElement;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public class ServerListSyncClient implements ClientModInitializer {

    public static final List<ServerInfo> serverInfos = new ArrayList<>();

    public static boolean updateData = false;

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    public static void updateServerInfos() {
        updateData = true;
        for (JsonElement element : ServerListSync.serverInfosJson) {
            serverInfos.add(new ServerInfo(element.getAsJsonObject().get("name").getAsString(),
                    element.getAsJsonObject().get("address").getAsString(), false));
        }
    }
}