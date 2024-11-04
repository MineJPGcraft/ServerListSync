package top.alazeprt.sls.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import top.alazeprt.sls.util.ServerOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SLSConfig {
    private static final File configFile = new File("config", "serverlistsync.json");
    private static final Gson gson = new Gson();

    public static String address = "https://ghp.ci/https://raw.githubusercontent.com/MineJPGcraft/ServerListSync/refs/heads/master/serverlistsync.json";
    public static ServerOrder order = ServerOrder.RANDOM;
    public static int updatePeriod = 60;

    public static void load() throws IOException {
        try {
            JsonObject content = gson.fromJson(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8),
                    JsonObject.class);
            address = content.get("address").getAsString();
            order = ServerOrder.parse(content.get("order").getAsString());
            updatePeriod = content.get("updatePeriod").getAsInt();
        } catch (Exception e) {
            initialize();
        }
    }

    public static void save() throws IOException {
        Files.createDirectories(configFile.getParentFile().toPath());
        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        map.put("order", order.name().toLowerCase());
        map.put("updatePeriod", updatePeriod);
        Files.writeString(configFile.toPath(), gson.toJson(map), StandardCharsets.UTF_8);
    }

    private static void initialize() throws IOException {
        Files.createDirectories(configFile.getParentFile().toPath());
        Files.writeString(configFile.toPath(), "{\"address\":\"https://ghp.ci/https://raw.githubusercontent.com/MineJPGcraft/ServerListSync/refs/heads/master/serverlistsync.json\",\"order\":\"random\",\"updatePeriod\":60}", StandardCharsets.UTF_8);
    }
}
