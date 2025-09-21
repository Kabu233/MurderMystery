package cn.thegoodboys.murdermystery.utils.menu.buttons;

import cn.thegoodboys.murdermystery.utils.item.ItemBuilder;
import cn.thegoodboys.murdermystery.utils.menu.AbstractMenu;
import cn.thegoodboys.murdermystery.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Back extends Button {


    private final AbstractMenu backAbstractMenu;
    private final List<String> lores;

    public Back(AbstractMenu backAbstractMenu, String... lores) {
        this.backAbstractMenu = backAbstractMenu;
        this.lores = Arrays.stream(lores).collect(Collectors.toList());
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.ARROW);
        itemBuilder.name(lores.get(0));
        lores.remove(0);
        itemBuilder.lore(lores);
        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        backAbstractMenu.OpenMenu(player);
    }

}
