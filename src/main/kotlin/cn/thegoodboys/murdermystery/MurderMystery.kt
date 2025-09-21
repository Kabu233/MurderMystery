package cn.thegoodboys.murdermystery

import cn.thegoodboys.murdermystery.config.Config
import cn.thegoodboys.murdermystery.game.Game
import cn.thegoodboys.murdermystery.utils.RegisterCommand
import cn.thegoodboys.murdermystery.utils.RegisterEvent
import cn.thegoodboys.murdermystery.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class MurderMystery : JavaPlugin() {

    lateinit var config: Config
    lateinit var gameConfig: Config
    lateinit var game: Game

    companion object {
        @JvmStatic
        lateinit var instance: MurderMystery
            private set
    }

    override fun onEnable() {
        instance = this
        Utils.register(this)
        config = Config(this)
        gameConfig = Config(this, "game.yml")
        game = Game()
        game.load()
        RegisterEvent(this).register()
        RegisterCommand().register()
        Bukkit.getLogger().info("MurderMystery enabled! by TheGoodBoys.")
    }

    override fun onDisable() {
        Bukkit.getLogger().info("MurderMystery disabled! by TheGoodBoys.")
    }
}