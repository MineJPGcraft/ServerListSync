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

public class SLSConfig {
    private static final File configFile = new File("config", "serverlistsync.json");
    private static final Gson gson = new Gson();

    public static String address = "http://localhost:8080/";
    public static ServerOrder order = ServerOrder.DEFAULT;
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

    private static void initialize() throws IOException {
        Files.createDirectories(configFile.getParentFile().toPath());
        Files.writeString(configFile.toPath(), "{\"address\":\"http://localhost:8080/\",\"order\":\"default\",\"updatePeriod\":60}", StandardCharsets.UTF_8);
    }
}
