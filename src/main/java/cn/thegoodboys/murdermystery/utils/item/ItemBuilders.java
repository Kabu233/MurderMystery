package cn.thegoodboys.murdermystery.utils.item;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class ItemBuilders {
    private ItemStack is;

    public ItemBuilders(Material m) {
        this(m, 1);
    }

    public ItemBuilders(ItemStack is) {
        this.is = is;
    }

    public ItemBuilders(Material m, int amount) {
        this.is = new ItemStack(m, amount);
    }

    public ItemBuilders(Material m, int amount, byte durability) {
        this.is = new ItemStack(m, amount, durability);
    }

    public ItemBuilder m120clone() {
        return new ItemBuilder(this.is);
    }

    public ItemBuilders setDurability(short dur) {
        this.is.setDurability(dur);
        return this;
    }

    public ItemBuilders setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilders setName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders addFlag(ItemFlag... flag) {
        ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(flag);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders removeFlag(ItemFlag... flag) {
        ItemMeta im = this.is.getItemMeta();
        im.removeItemFlags(flag);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders addUnsafeEnchantment(Enchantment ench, int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilders addRandomUnsafeEnchantment(int level, Enchantment... ench) {
        this.is.addUnsafeEnchantment(ench[new Random().nextInt(ench.length)], level);
        return this;
    }

    public ItemBuilders clearLores() {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(new ArrayList());
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders removeEnchantment(Enchantment ench) {
        this.is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilders removeEnchantments() {
        ItemMeta im = this.is.getItemMeta();
        im.getEnchants().forEach((enchantment, integer) -> im.removeEnchant(enchantment));
        return this;
    }

    public ItemBuilders setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta(im);
        } catch (ClassCastException e) {
        }
        return this;
    }

    public ItemBuilders addEnchant(Enchantment ench, int level) {
        ItemMeta im = this.is.getItemMeta();
        im.addEnchant(ench, level, true);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilders setInfinityDurability() {
        this.is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilders setUnbreakable() {
        ItemMeta im = this.is.getItemMeta();
        im.spigot().setUnbreakable(true);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders setType(Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemBuilders setLore(String... lore) {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders setLore(List<String> lore) {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders removeLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (lore.contains(line)) {
            lore.remove(line);
            im.setLore(lore);
            this.is.setItemMeta(im);
            return this;
        }
        return this;
    }

    public ItemBuilders removeLoreLine(int index) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size()) {
            return this;
        }
        lore.remove(index);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders addLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) {
            lore = new ArrayList<>(im.getLore());
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders addLoreLine(String line, int pos) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.add(pos, line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilders addLoreLine(String... lines) {
        for (String line : lines) {
            addLoreLine(line);
        }
        return this;
    }

    public ItemBuilders setDyeColor(DyeColor color) {
        this.is.setDurability(color.getData());
        return this;
    }

    @Deprecated
    public ItemBuilders setWoolColor(DyeColor color) {
        if (this.is.getType().equals(Material.WOOL)) {
            this.is.setDurability(color.getData());
            return this;
        }
        return this;
    }

    public ItemBuilders setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) this.is.getItemMeta();
            im.setColor(color);
            this.is.setItemMeta(im);
        } catch (ClassCastException e) {
        }
        return this;
    }

    public ItemBuilders setMapView(MapView mapView) {
        setDurability(mapView.getId());
        return this;
    }

    public ItemStack build() {
        return this.is;
    }

    public ItemBuilders addPotion(PotionEffect potionEffect) {
        PotionMeta im = (PotionMeta) this.is.getItemMeta();
        im.addCustomEffect(potionEffect, true);
        this.is.setItemMeta(im);
        return this;
    }
}
