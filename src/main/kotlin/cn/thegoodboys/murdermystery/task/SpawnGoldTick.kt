package cn.thegoodboys.murdermystery.task

import cn.thegoodboys.murdermystery.MurderMystery
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class SpawnGoldTick: Runnable {

    private val game = MurderMystery.instance.game

    override fun run() {
        // 如果金锭位置列表为空，直接返回
        if (game.goldLocation.isEmpty()) return
        
        // 随机决定生成2个或3个金锭
        val spawnCount = Random.nextInt(2, 4)
        
        // 获取所有可用的位置（没有金锭的位置）
        val availableLocations = game.goldLocation.filter { location ->
            // 检查该位置1格范围内是否有金锭实体
            location.world.getNearbyEntities(location, 1.0, 1.0, 1.0).none { entity ->
                entity.type == org.bukkit.entity.EntityType.DROPPED_ITEM && 
                (entity as org.bukkit.entity.Item).itemStack.type == Material.GOLD_INGOT
            }
        }
        
        if (availableLocations.isEmpty()) return
        
        // 从可用位置中随机选择生成点
        val spawnLocations = availableLocations.shuffled().take(minOf(spawnCount, availableLocations.size))
        
        // 在选中的位置生成金锭
        spawnLocations.forEach { location ->
            location.world.dropItem(location, ItemStack(Material.GOLD_INGOT))
        }
    }
}