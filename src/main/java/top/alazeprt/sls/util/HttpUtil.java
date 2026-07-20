package top.alazeprt.sls.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static top.alazeprt.sls.config.SLSConfig.address;

public class HttpUtil {

    public static JsonObject get() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(address))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return new Gson().fromJson("{\"error\":{\"code\":\"" + response.statusCode() + "\"}}", JsonObject.class);
            }
            return new Gson().fromJson(response.body(), JsonObject.class);
        } catch (IOException e) {
            return new Gson().fromJson("{\"error\":{\"message\":\"" + e + "\"}}", JsonObject.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Gson().fromJson("{\"error\":{\"message\":\"" + e + "\"}}", JsonObject.class);
        }
    }
}
