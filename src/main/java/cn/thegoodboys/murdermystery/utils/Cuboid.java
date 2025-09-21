package cn.thegoodboys.murdermystery.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.Random;

public class Cuboid {
    private final int x1;
    private final int y1;
    private final int z1;
    private final int x2;
    private final int y2;
    private final int z2;
    public String worldName;

    public Cuboid(Location var1, Location var2) {
        this.worldName = var1.getWorld().getName();
        this.x1 = Math.min(var1.getBlockX(), var2.getBlockX());
        this.y1 = Math.min(var1.getBlockY(), var2.getBlockY());
        this.z1 = Math.min(var1.getBlockZ(), var2.getBlockZ());
        this.x2 = Math.max(var1.getBlockX(), var2.getBlockX());
        this.y2 = Math.max(var1.getBlockY(), var2.getBlockY());
        this.z2 = Math.max(var1.getBlockZ(), var2.getBlockZ());
    }

    public Cuboid(String var1) {
        String[] var2 = var1.split(", ");
        this.worldName = var2[0];
        this.x1 = Integer.parseInt(var2[1]);
        this.y1 = Integer.parseInt(var2[2]);
        this.z1 = Integer.parseInt(var2[3]);
        this.x2 = Integer.parseInt(var2[4]);
        this.y2 = Integer.parseInt(var2[5]);
        this.z2 = Integer.parseInt(var2[6]);
    }

    public boolean contains(Location var1) {
        return var1.getWorld().getName().equals(this.worldName) && var1.getBlockX() >= this.x1 && var1.getBlockX() <= this.x2 && var1.getBlockY() >= this.y1 && var1.getBlockY() <= this.y2 && var1.getBlockZ() >= this.z1 && var1.getBlockZ() <= this.z2;
    }

    public boolean containsIgnoringY(Location var1) {
        return var1.getWorld().getName().equals(this.worldName) && var1.getBlockX() >= this.x1 && var1.getBlockX() <= this.x2 && var1.getBlockZ() >= this.z1 && var1.getBlockZ() <= this.z2;
    }

    public int getLowerY() {
        return this.y1;
    }

    public Location getMin() {
        return new Location(Bukkit.getWorld(this.worldName), (double) this.x1, (double) this.y1, (double) this.z1);
    }

    public Location getMax() {
        return new Location(Bukkit.getWorld(this.worldName), (double) this.x2, (double) this.y2, (double) this.z2);
    }

    public int getSize() {
        return (this.x2 - this.x1 + 1) * (this.y2 - this.y1 + 1) * (this.z2 - this.z1 + 1);
    }

    public Location getRandomLocation() {
        World var1 = Bukkit.getWorld(this.worldName);
        Random var2 = new Random();
        int var3 = var2.nextInt(this.x2 - this.x1 + 1);
        int var4 = var2.nextInt(this.y2 - this.y1 + 1);
        int var5 = var2.nextInt(this.z2 - this.z1 + 1);
        Location var6 = new Location(var1, (double) (this.x1 + var3), (double) (this.y1 + var4), (double) (this.z1 + var5));
        return var6.getBlock().getType().equals(Material.AIR) ? var6 : var1.getHighestBlockAt(var6).getLocation();
    }

    public String toString() {
        return this.worldName + ", " + this.x1 + ", " + this.y1 + ", " + this.z1 + ", " + this.x2 + ", " + this.y2 + ", " + this.z2;
    }

    public Iterator<Block> iterator() {
        return new CuboidIterator(Bukkit.getWorld(this.worldName), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public class CuboidIterator implements Iterator<Block> {
        private final World w;
        private final int baseX;
        private final int baseY;
        private final int baseZ;
        private final int sizeX;
        private final int sizeY;
        private final int sizeZ;
        private int x;
        private int y;
        private int z;

        public CuboidIterator(World var2, int var3, int var4, int var5, int var6, int var7, int var8) {
            this.w = var2;
            this.baseX = var3;
            this.baseY = var7;
            this.baseZ = var5;
            this.sizeX = var6 - var3 + 1;
            this.sizeY = var7 - var4 + 1;
            this.sizeZ = var8 - var5 + 1;
            this.x = this.y = this.z = 0;
        }

        public boolean hasNext() {
            return this.x < this.sizeX && this.y > -this.sizeY && this.z < this.sizeZ;
        }

        public Block next() {
            Block var1 = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
            this.update();
            return var1;
        }

        public void update() {
            if (++this.x >= this.sizeX) {
                this.x = 0;
                if (++this.z >= this.sizeZ) {
                    this.z = 0;
                    --this.y;
                }
            }

        }

        public void remove() {
        }
    }
}
