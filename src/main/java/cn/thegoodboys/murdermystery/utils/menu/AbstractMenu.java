package cn.thegoodboys.murdermystery.utils.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class AbstractMenu {

    public static final Map<UUID, AbstractMenu> currentlyOpenedMenus = new ConcurrentHashMap<>();
    private final boolean autoUpdate = false;
    private Map<Integer, Button> buttons = new HashMap<>();
    @Setter
    private boolean closedByMenu = false;

    public abstract String getTitle(Player player);

    public abstract int getSize();

    public void OpenMenu(@NonNull Player player) {

        this.buttons = this.getButtons(player);
        AbstractMenu previousMenu = AbstractMenu.currentlyOpenedMenus.get(player.getUniqueId());

        Inventory inventory = null;
        int size = getSize();
        String title = ChatColor.translateAlternateColorCodes('&', getTitle(player));
        boolean update = false;

        //如果玩家正在打开Inv
        if (player.getOpenInventory() != null) {
            if (previousMenu == null) {
                player.closeInventory();
            } else {
                int previousSize = player.getOpenInventory().getTopInventory().getSize();
                //判断当前玩家打开Inv的格子和准备要打开的格子一样不 && 标题也一样
                if (previousSize == size) {
                    inventory = player.getOpenInventory().getTopInventory();
                    update = true;
                } else {
                    //关闭玩家当前打开的Inv 以打开下一个
                    previousMenu.setClosedByMenu(true);
                    player.closeInventory();
                }
            }
        }

        //如果玩家没有打开Inv就新建一个
        if (inventory == null) {
            inventory = Bukkit.createInventory(player, size, title);
        }

        inventory.setContents(new ItemStack[inventory.getSize()]);

        currentlyOpenedMenus.put(player.getUniqueId(), this);

        //把物品扔进当前Inv
        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
        }

        //如果这个Inv是需要更新的就更新 不想要就打开New Inv
        if (update) {
            player.updateInventory();
        } else {
            player.openInventory(inventory);
        }

        //if (!player.getOpenInventory().getTopInventory().getTitle().equals(title))
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        PacketPlayOutOpenWindow packet =
                new PacketPlayOutOpenWindow(ep.activeContainer.windowId,
                        "minecraft:chest",
                        new ChatMessage(title),
                        player.getOpenInventory().getTopInventory().getSize());
        ep.playerConnection.sendPacket(packet);
        ep.updateInventory(ep.activeContainer);

        this.setClosedByMenu(false);

    }


    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);

        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        if (item.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = item.getItemMeta();

            if (meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
            }

            item.setItemMeta(meta);
        }

        return item;
    }


    public void onClose(Player player, Inventory inventory) {
    }

    public void onClickEvent(InventoryClickEvent event) {

    }

    public abstract Map<Integer, Button> getButtons(Player player);

}
