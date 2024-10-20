package top.alazeprt.sls.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

    public static void load() throws IOException {
        try {
            JsonObject content = gson.fromJson(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8),
                    JsonObject.class);
            address = content.get("address").getAsString();
        } catch (Exception e) {
            initialize();
        }
    }

    private static void initialize() throws IOException {
        Files.createDirectories(configFile.getParentFile().toPath());
        Files.writeString(configFile.toPath(), "{\"address\":\"http://localhost:8080/\"}", StandardCharsets.UTF_8);
    }
}
