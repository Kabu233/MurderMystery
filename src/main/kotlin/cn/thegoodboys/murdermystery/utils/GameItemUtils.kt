package cn.thegoodboys.murdermystery.utils

import cn.thegoodboys.murdermystery.utils.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

object GameItemUtils {

    /**
     * TheGoodBoys 2025/5/29
     */

    //给予大厅物品
    fun Player.onLobby() {
        val start = ItemBuilder(Material.DIAMOND).name("&b强制开始 &7(右键点击)").amount(1).build()
        val back = ItemBuilder(Material.BED).name("&c返回大厅 &7(右键点击)").amount(1).build()

        this.inventory.setItem(7,start)
        this.inventory.setItem(8,back)
    }



    //给予游戏物品
    fun Player.onGame() {
        val target = ItemBuilder(Material.COMPASS).name("&a指南针 &7(右键点击)").amount(1).build()

        this.inventory.setItem(4,target)
    }


    //给予旁观者物品
    fun Player.onSpectator() {
        val target = ItemBuilder(Material.COMPASS).name("&a追踪器 &7(右键点击)").amount(1).build()
        val back = ItemBuilder(Material.BED).name("&c返回大厅 &7(右键点击)").amount(1).build()

        this.inventory.setItem(0,target)
        this.inventory.setItem(8,back)
    }
}