package cn.thegoodboys.murdermystery.utils.item;

import cn.thegoodboys.murdermystery.utils.chat.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Bed;

import java.util.ArrayList;
import java.util.List;

public class BedUtil {

    public static List<Location> getLocation(Player player, Block block) {
        if (!block.getType().equals(Material.BED_BLOCK)) {
            CC.send(player, "&c你需要站到 床尾 才能设置！");
            return null;
        }
        Bed bed = (Bed) block.getState().getData();
        if (bed.isHeadOfBed()) {
            CC.send(player, "&c你需要站到 床尾 才能设置！");
            return null;
        }
        List<Location> locations = new ArrayList<>();
        locations.add(block.getLocation());
        if (new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ()).getBlock().getType() == Material.BED_BLOCK) {
            locations.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ()));
        }
        if (new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ()).getBlock().getType() == Material.BED_BLOCK) {
            locations.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ()));
        }
        if (new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1).getBlock().getType() == Material.BED_BLOCK) {
            locations.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1));
        }
        if (new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1).getBlock().getType() == Material.BED_BLOCK) {
            locations.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1));
        }
        return locations;
    }

}
