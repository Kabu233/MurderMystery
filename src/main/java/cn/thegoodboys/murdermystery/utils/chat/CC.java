package cn.thegoodboys.murdermystery.utils.chat;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CC {

    private static ConsoleCommandSender console = Bukkit.getConsoleSender();


    public static void send(CommandSender sender, String message, Object... objects) {
        message = String.format(message, objects);
        message = translate(message);
        sender.sendMessage(message);
    }

    public static void send(String message, Object... objects) {
        send(console, message, objects);
    }


    public static void broadcast(String message, String permission, Object... objects) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission))
                send(player, message, objects);
        }
    }


    public static void debug(String message, Object... objects) {
        message = String.format(message, objects);
        message = translate(message);
        console.sendMessage("§1[§bBedWars§c-§6Debug§1]§e" + message);
    }


    public static void sendError(String message, Throwable e) {
        if (message != null) console.sendMessage(translate(message));
        for (StackTraceElement element : e.getStackTrace()) {
            console.sendMessage(translate("&cAt " + element));
        }
        console.sendMessage(translate("&c发生了一个错误，请完整截图此信息并联系管理员！"));
    }

    public static void sendError(CommandSender sender, String message, Exception e) {
        if (message != null) console.sendMessage(translate(message));
        for (StackTraceElement element : e.getStackTrace()) {
            console.sendMessage(translate("&cAt " + element));
            sender.sendMessage(translate("&cAt " + element));
        }
        sender.sendMessage(translate("&c发生了一个错误，请完整截图此信息并联系管理员！"));
    }


    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) toReturn.add(translate(line));
        return toReturn;
    }

    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) if (line != null) toReturn.add(translate(line));
        return toReturn;
    }


}
