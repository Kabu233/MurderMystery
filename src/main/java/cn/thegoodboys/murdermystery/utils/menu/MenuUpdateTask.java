package cn.thegoodboys.murdermystery.utils.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuUpdateTask implements Runnable {
    public MenuUpdateTask() {
        run();
    }

    @Override
    public void run() {
        AbstractMenu.currentlyOpenedMenus.forEach((key, value) -> {
            final Player player = Bukkit.getPlayer(key);

            if (player != null) {
                if (value.isAutoUpdate()) {
                    value.OpenMenu(player);
                }
            }
        });
    }
}
