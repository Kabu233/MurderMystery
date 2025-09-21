package cn.thegoodboys.murdermystery.utils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ItemUtil {

    public static void addItem(Inventory inventory, Material material, int amount, int date, String itemname, List<String> lores, boolean glow) {
        ItemStack itemStack = new ItemStack(material, amount, (short) date);
        SetMetaData(itemStack, itemname, lores);
        if (glow) itemStack = addGlow(itemStack);
        inventory.addItem(itemStack);
    }

    public static void setItem(Inventory inventory, Material material, int amount, int date, String itemname, List<String> lores, boolean glow, int lattice) {
        ItemStack itemStack = new ItemStack(material, amount, (short) date);
        SetMetaData(itemStack, itemname, lores);
        if (glow) itemStack = addGlow(itemStack);
        inventory.setItem(lattice, itemStack);
    }

    public static void addItem(Inventory inventory, String url, int amount, String itemname, List<String> lores) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);

        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        itemStack.setItemMeta(itemMeta);
        SetMetaData(itemStack, itemname, lores);
        inventory.addItem(itemStack);
    }

    public static String getLore(ItemStack is, int line) {
        try {
            return is.getItemMeta().getLore().get(line);
        } catch (Exception ex) {
            return "NULL";
        }

    }

    public static void setItem(Inventory inventory, String url, int amount, String itemname, List<String> lores, int lattice) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);

        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        itemStack.setItemMeta(itemMeta);
        SetMetaData(itemStack, itemname, lores);
        inventory.setItem(lattice, itemStack);
    }

    public static void setItem(Inventory inventory, String PlayerName, String ItemName, int amount, List<String> lores, int Slot) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(PlayerName);
        itemStack.setItemMeta(skullMeta);
        ItemUtil.SetMetaData(itemStack, ItemName, lores);
        inventory.setItem(Slot, itemStack);
    }


    public static void SetMetaData(ItemStack item, String name, List<String> lores) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lores);
        item.setItemMeta(im);
    }

    public static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
}
