package cn.thegoodboys.murdermystery.listener

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.game.GamePlayer
import cn.thegoodboys.murdermystery.game.GameStats
import cn.thegoodboys.murdermystery.utils.chat.ActionBarUtil
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.scheduler.BukkitRunnable

class MurderListener: Listener {
    /**
     * TheGoodBoys 2025/5/31
     */

    private val game = MurderMystery.instance.game
    private val lastChargeTime = mutableMapOf<Player, Long>() //飞刀冷却
    private val THROW_COOLDOWN = 5 // 飞刀冷却时间
    private val chargingTasks = mutableMapOf<Player, Int>() //存储玩家飞刀蓄力的计时器

    @EventHandler(priority = EventPriority.HIGH)
    private fun onMurderSword(event: PlayerInteractEvent) {
        if (game.gameEnable) return
        if (game.gameStats != GameStats.PLAYING) return
        val player = event.player
        val gamePlayer = GamePlayer.get(player.uniqueId) ?: return
        if (game.murders.contains(player)) {
            if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
                if (event.item?.itemMeta?.displayName == "§a刀") {

                    // 检查是否在冷却中
                    if (lastChargeTime.containsKey(player)) {
                        val timeSinceLastCharge = System.currentTimeMillis() - lastChargeTime[player]!!
                        if (timeSinceLastCharge < THROW_COOLDOWN * 1000) {
                            val remainingCooldown =THROW_COOLDOWN - (timeSinceLastCharge / 1000)
                            val cooldownMessage = ChatColor.translateAlternateColorCodes(
                                '&',
                                "&c冷却中! &e还需要等待&c$remainingCooldown&e秒")
                            ActionBarUtil.sendActionBar(player,cooldownMessage)
                            player.playSound(player.location, Sound.NOTE_BASS, 1.0f, 0.5f) // 播放冷却提示音效
                            return  // 直接返回，不执行任何蓄力相关操作
                        }
                    }
                    if (chargingTasks.containsKey(player)) {
                        return
                    }
                    gamePlayer.isCharging = true // 开始蓄力
                    onCharge(player) // Added this line to start charging when right-clicking
                }
            }
        }
    }

    /**
     * 飞刀蓄力
     */
    private fun onCharge(player: Player) {
        val gamePlayer = GamePlayer.get(player.uniqueId) ?: return
        val chargeProgress = intArrayOf(0) // 改用进度计数而不是时间计数
        ActionBarUtil.sendActionBar(player,"&a蓄力中...")
        player.playSound(player.location, Sound.NOTE_PLING, 1.0f, 1.0f)

        val taskID = object :BukkitRunnable() {
            override fun run() {
                if (!player.isOnline || !gamePlayer.isCharging) {
                    cancel()
                    if (player.isOnline) {
                        cancelCharging(player)
                    }
                    return
                }
                if (chargeProgress[0] >= 8) { // 由于5tick一次,设置为8次约等于2秒
                    cancel()
                    throwSword(player)
                    chargingTasks.remove(player)
                    gamePlayer.isCharging = false
                    return
                }
                chargeProgress[0]++
                showChargingProgress(player, (chargeProgress[0] * 10) / 8) // 将进度转换为百分比显示
            }
        }.runTaskTimer(MurderMystery.instance, 0L, 5L).taskId
        chargingTasks.put(player, taskID)
    }

    /**
     * 取消蓄力
     */
    private fun cancelCharging(player: Player) {
        val taskId = chargingTasks[player] ?: return
        MurderMystery.instance.server.scheduler.cancelTask(taskId)
        chargingTasks.remove(player)
        val gamePlayer = GamePlayer.get(player.uniqueId) ?: return
        gamePlayer.isCharging = false
        ActionBarUtil.sendActionBar(player, "&c蓄力已取消")
        player.playSound(player.location, Sound.NOTE_BASS, 1.0f, 0.5f)
    }

    /**
     * 切换玩家手持物品时如果玩家正在蓄力，则取消蓄力
     */
    @EventHandler(priority = EventPriority.HIGH)
    private fun onPlayerInteract(event: PlayerItemHeldEvent) {
        if (game.gameEnable) return
        if (game.gameStats != GameStats.PLAYING) return
        val player: Player = event.player
        if (chargingTasks.containsKey(player)) {
            cancelCharging(player)
        }
    }

    /**
     * 显示蓄力进度
     */
    private fun showChargingProgress(player: Player?, progress: Int) {
        val bar = StringBuilder("&8[")
        for (i in 0..9) {
            bar.append(if (i < progress) "&e■" else "&7■")
        }
        bar.append("&8] &e").append(String.format("%.1f", progress * 0.25)).append("s") // 显示更精确的时间
        ActionBarUtil.sendActionBar(player, bar.toString())
    }

    /**
     * 发射飞刀
     */
    private fun throwSword(player: Player) {
        val loc = player.location
        val sword = loc.world.spawnEntity(loc.add(0.0, 0.5, 0.0), EntityType.ARMOR_STAND) as ArmorStand // 生成盔甲架
        sword.isVisible = false // 设置盔甲架不可见
        sword.setGravity(false) // 关闭重力效果
        sword.isSmall = true // 设置为小型盔甲架
        sword.itemInHand = player.inventory.itemInHand.clone() // 设置手持物品为玩家的剑
        sword.rightArmPose = sword.rightArmPose.setX(-Math.PI / 2) // 设置右手臂角度
        val direction = player.location.direction.multiply(1.5) // 设置飞行速度和方向

        object : BukkitRunnable() {
            var distance = 0
            override fun run() {
                //如果玩家不在线或者盔甲架死亡则取消任务
                if (!player.isOnline || sword.isDead) {
                    cancel()
                    return
                }
                //最大飞行距离为20
                if (distance >= 20) {
                    cancel()
                    return
                }
                sword.teleport(sword.location.add(direction))
                distance++
                // 检查是否击中玩家 (待实现)
            }

        }.runTaskTimer(MurderMystery.instance, 0L, 1L)
        lastChargeTime[player] = System.currentTimeMillis() // 记录上次飞刀时间
    }
}