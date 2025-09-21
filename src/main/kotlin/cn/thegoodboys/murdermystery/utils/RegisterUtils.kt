package cn.thegoodboys.murdermystery.utils

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.commands.AdminCommand
import cn.thegoodboys.murdermystery.listener.BlockListener
import cn.thegoodboys.murdermystery.listener.PlayerListener
import cn.thegoodboys.murdermystery.task.ScoreBoardTask
import org.bukkit.Bukkit

class RegisterEvent(val javaPlugin: MurderMystery) {

    fun register() {
        Bukkit.getPluginManager().registerEvents(PlayerListener(),javaPlugin)
        Bukkit.getPluginManager().registerEvents(BlockListener(),javaPlugin)
        Bukkit.getPluginManager().registerEvents(ScoreBoardTask(),javaPlugin)
    }
}

class RegisterCommand() {

    fun register() {
        Bukkit.getPluginCommand("murdermystery").executor = AdminCommand()
    }

}


