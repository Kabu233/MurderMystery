package cn.thegoodboys.murdermystery.listener

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.game.GameStats
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class EntityListener: Listener {

    /**
     * TheGoodBoys 2025/5/31
     */

    private val game = MurderMystery.instance.game

    @EventHandler(priority = EventPriority.HIGH)
    private fun onDamage(event: EntityDamageEvent) {
        if (game.gameEnable) return
        event.isCancelled = true
    }



    //侦探弓箭
    @EventHandler(priority = EventPriority.HIGH)
    private fun onShootBow(event: EntityShootBowEvent) {
        if (game.gameEnable) return
        if (game.gameStats != GameStats.PLAYING) return
        val shooter = event.getEntity() as Player
        val arrow = event.projectile as Arrow
        arrow.setMetadata("MurderMysteryArrow", FixedMetadataValue(MurderMystery.instance, true))
        shooter.world.playSound(shooter.location, Sound.SHOOT_ARROW,1f,1f)

        // 清除玩家的箭矢
        shooter.inventory.remove(ItemStack(Material.ARROW,1))
        shooter.updateInventory()

        //箭矢消失 1秒消失
        object : BukkitRunnable() {
            override fun run() {
                if (!arrow.isDead && arrow.hasMetadata("MurderMysteryArrow")) {
                    arrow.remove() // 强制移除箭矢
                }
            }
        }.runTaskLater(MurderMystery.instance, 20)

        //箭矢补充计时器
        object : BukkitRunnable() {
            override fun run() {
                if (game.gameStats != GameStats.PLAYING) return
                if (!shooter.isOnline) return
                if (!game.detectives.contains(shooter)) return
                shooter.inventory.setItem(9, ItemStack(Material.ARROW,1)) // 修改为第9格(索引1)
            }
        }.runTaskTimer(MurderMystery.instance, 0, 15*20)

    }



}