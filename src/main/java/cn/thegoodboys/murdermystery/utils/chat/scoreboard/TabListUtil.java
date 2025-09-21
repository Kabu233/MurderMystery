package cn.thegoodboys.murdermystery.utils.chat.scoreboard;

import cn.thegoodboys.murdermystery.utils.chat.TypeConversionUtil;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class TabListUtil {

    @SneakyThrows
    public static void setHeaderFooter(Player player, List<String> header, List<String> footer) {
        ChatComponentText headerComponent = new ChatComponentText(TypeConversionUtil.StringList(header));
        ChatComponentText footerComponent = new ChatComponentText(TypeConversionUtil.StringList(footer));

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(headerComponent);
        Field footerField = packet.getClass().getDeclaredField("b");
        footerField.setAccessible(true);
        footerField.set(packet, footerComponent);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
