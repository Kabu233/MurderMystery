package cn.thegoodboys.murdermystery.listener

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.game.GamePlayer
import cn.thegoodboys.murdermystery.game.GameStats
import cn.thegoodboys.murdermystery.utils.BungeeUtil
import cn.thegoodboys.murdermystery.utils.chat.ActionBarUtil
import cn.thegoodboys.murdermystery.utils.chat.CC
import cn.thegoodboys.murdermystery.utils.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

class PlayerListener: Listener {

    /**
     *
        TheGoodBoys 2025/5/29

    **/


    private val game = MurderMystery.instance.game

    @EventHandler(priority = EventPriority.HIGH)
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        if (game.gameEnable) return
        val player = event.player
        event.joinMessage = null
        if (GamePlayer.getOnlinePlayers().size > game.maxPlayers) {
            player.kickPlayer("房间满了！")
            return
        }
        if (game.gameStats != GameStats.WAITING || game.gameStats != GameStats.COUNTDOWN) {
            player.kickPlayer("游戏开始，无法进入！")
            return
        }
        game.addPlayer(player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        if (game.gameEnable) return
        val player = event.player
        event.quitMessage = null
        game.leavePlayer(player)
    }


    @EventHandler(priority = EventPriority.HIGH)
    private fun onClick(event: PlayerInteractEvent) {
        if (game.gameEnable) return
        val player = event.player
        when (game.gameStats) {
            GameStats.WAITING, GameStats.COUNTDOWN -> {
                if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
                    //强制开始
                    if (player.itemInHand?.itemMeta?.displayName == "§b强制开始 §7(右键点击)") {
                        game.forceStart()
                    }
                    //返回大厅
                    if (player.itemInHand?.itemMeta?.displayName == "§c返回大厅 §7(右键点击)") {
                        BungeeUtil.send(MurderMystery.instance.config.getString("lobby"),player)
                    }

                }
            }
            GameStats.PLAYING-> {

            }
            GameStats.ENDING-> {

            }
        }
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun playerFoodLevel(event: FoodLevelChangeEvent) {
        if (game.gameEnable) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun onPlayerMoveInventory(event: InventoryClickEvent) {
        if (game.gameEnable) return
        val player = event.whoClicked as Player
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun onPlayerPickUpGoldItem(event: PlayerPickupItemEvent) {
        if (game.gameEnable) return
        if (game.gameStats != GameStats.PLAYING) return
        val player = event.player
        if (event.item?.itemStack?.type == Material.GOLD_INGOT) {
            val item = ItemBuilder(Material.GOLD_INGOT).name("&e金锭").amount(1).build()
            //获取玩家背包数量+1
            player.inventory.setItem(8, item)
            player.playSound(player.location, Sound.ITEM_PICKUP,1f,1f)
            ActionBarUtil.sendActionBar(player,"&6+5金币")
            event.item.remove()
            event.isCancelled = true
        }
    }


}