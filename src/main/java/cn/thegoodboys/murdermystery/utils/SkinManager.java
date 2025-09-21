package cn.thegoodboys.murdermystery.utils;

import com.mojang.authlib.properties.Property;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SkinManager {

    public static String[] getSkinFormApi(String skinName) {
        String value = "eyJ0aW1lc3RhbXAiOjE1MzMzNDI3NTE3MTksInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM2NWVkMjgyOWM4M2UxMTlhODBkZmIyMjIxNjQ0M2U4NzhlZjEwNjQ5YzRhMzU0Zjc0YmY0NWFkMDZiYzFhNyJ9fX0=";
        String signature = "gvRz+z/AM/p9iJeifKTCEjIVoQbQq0OHPstiFFdX9cE6uxw+C0X0JuwoxHVEUkvKlzPQJGVPLh5VSQY4NL3wCEkyOwoN3FDWIewjzhwtmyM4BlzbHDRT6C/4ICFw3azLZi6f7EkPNcL9O8yks+ebyLxXEgJEFSmY7nMsjdRuLOVu7X9UoSbKLwNR8Rua9LEKtdhpVcQx+rLD9T4VRZctfhunPDVnXfQTqvq4gC1lb6nQPIwSnVCdH5eY4bnOO1n/vV7enOO0mMgjtQLxFFM1OKoBRDxh70pNgAmVUxunUA1xrfG+pZF5HM6nMh1FDKD7NTNrZ7O4EWpawT2Q8+EVLgdcMYNMlIW1xb4pOMceDpaAlOw3LOfROGK6cz4OpevuXU3WaXb97cuq8B0SrlVJI7xnL3sgEzyf+1MIQ6O0NBo7SAa0vVXEVu+Y65bawZh0HYpwngkTkMJR8wulNQ2HcKzSClZKsqP7M/qneHCJ4lnQsGFXj5LEp1EkJZ7WGTcIo5L4Fo7hl0FiGtTdesBEBc/OQOB2dIZfOZFInRaTMALuaB3VrAg+atYtTeU9twl0qmrq/0L5FbszkTh6xqpnGmlNWuRx1yKuG8+lD3f0sejHIPXC/Akw3YQjWFUTs3e8h18zD6mtZxu4CKFc6hVsnMsGr0tEIa/eASkUGJSNy0s=";
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(SkinManager.getResponse("https://api.mojang.com/users/profiles/minecraft/" + skinName));
            JSONObject json = (JSONObject)obj;
            String uuid = (String)json.get("id");
            Object obj2 = parser.parse(SkinManager.getResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false"));
            JSONObject json2 = (JSONObject)obj2;
            Object props = ((JSONArray)json2.get("properties")).get(0);
            JSONObject propsObj = (JSONObject)props;
            value = (String)propsObj.get("value");
            signature = (String)propsObj.get("signature");
        } catch (ParseException ignored) {
        }
        return new String[]{value, signature};
    }

    public static String[] getSkin(Player player) {
        Property property = ((CraftPlayer) player).getProfile().getProperties().get("textures").iterator().next();
        return new String[]{property.getValue(), property.getSignature()};
    }

    private static String getResponse(String url) {
        StringBuilder receive = new StringBuilder();
        try {
            URL url1 = new URL(url);
            HttpURLConnection http = (HttpURLConnection)url1.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0");
            InputStream im = http.getInputStream();
            InputStreamReader imr = new InputStreamReader(im, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(imr);
            String theline;
            while ((theline = br.readLine()) != null) {
                receive.append(theline).append("\r\n");
            }
            br.close();
            imr.close();
            im.close();
        }
        catch (IOException e) {
            return null;
        }
        return receive.toString();
    }
}
