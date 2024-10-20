package top.alazeprt.sls.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static top.alazeprt.sls.config.SLSConfig.address;

public class HttpUtil {

    public static JsonObject get() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(address);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200) {
                return new Gson().fromJson("{\"error\":{\"code\":\"" + response.getStatusLine().getStatusCode() + "\"}}", JsonObject.class);
            }
            if (entity != null) {
                return new Gson().fromJson(EntityUtils.toString(entity), JsonObject.class);
            }
        } catch (IOException e) {
            return new Gson().fromJson("{\"error\":{\"message\":\"" + e + "\"}}", JsonObject.class);
        }
        return new Gson().fromJson("{\"error\":{\"message\":\"Unknown error\"}}", JsonObject.class);
    }
}
