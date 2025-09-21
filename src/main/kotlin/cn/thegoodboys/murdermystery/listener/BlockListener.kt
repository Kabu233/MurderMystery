package cn.thegoodboys.murdermystery.listener

import cn.thegoodboys.murdermystery.MurderMystery
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class BlockListener: Listener {


    /**
     * TheGoodBoys 2025/5/29
     */

    private val game = MurderMystery.instance.game

    @EventHandler(priority = EventPriority.HIGH)
    private fun onBlockBreak(event: BlockBreakEvent) {
        if (game.gameEnable) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun onBlockPlaced(event: BlockPlaceEvent) {
        if (game.gameEnable) return
        event.isCancelled = true
    }
}