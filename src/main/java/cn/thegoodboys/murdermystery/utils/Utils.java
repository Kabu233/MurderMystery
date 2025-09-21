package cn.thegoodboys.murdermystery.utils;

import cn.thegoodboys.murdermystery.utils.menu.ButtonListener;
import cn.thegoodboys.murdermystery.utils.menu.MenuUpdateTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils {

    @Getter
    private static JavaPlugin javaPlugin;

    public static void register(JavaPlugin javaPlugin) {
        Utils.javaPlugin = javaPlugin;
        runMenuTask();
    }

    private static void runMenuTask() {
        Runnable MenuUpdateTask = new MenuUpdateTask();
        Bukkit.getScheduler().runTaskTimer(Utils.getJavaPlugin(), MenuUpdateTask, 20, 20);
        Bukkit.getPluginManager().registerEvents(new ButtonListener(), Utils.getJavaPlugin());
    }

}
