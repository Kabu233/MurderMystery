package cn.thegoodboys.murdermystery.utils;

import cn.thegoodboys.murdermystery.MurderMystery;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BungeeUtil {
    public BungeeUtil(){
        MurderMystery.instance.getServer().getMessenger().registerOutgoingPluginChannel(MurderMystery.instance, "BungeeCord");
    }

    public static void send(String server, Player player) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(MurderMystery.instance, "BungeeCord", bytes.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
