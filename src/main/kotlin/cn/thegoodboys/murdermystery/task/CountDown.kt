package cn.thegoodboys.murdermystery.task

import cn.thegoodboys.murdermystery.game.Game
import cn.thegoodboys.murdermystery.game.GamePlayer
import cn.thegoodboys.murdermystery.game.GameStats
import cn.thegoodboys.murdermystery.utils.MessageUtils
import org.bukkit.Sound

class CountDown : Runnable {

    private lateinit var game: Game
    private var task: Int? = null
    private var starts = false

    fun start(game: Game) {
        this.game = game
        this.task = game.time
        this.game.gameStats = GameStats.COUNTDOWN
    }


    override fun run() {
        if (game.gameStats != GameStats.WAITING && game.gameStats != GameStats.COUNTDOWN) return
        if (GamePlayer.getOnlinePlayers().size < game.minPlayers) {
            game.time = 120
            game.gameStats = GameStats.WAITING
            return
        }
            if (game.time == 120 || game.time == 60 || game.time == 30 || game.time == 10 || game.time in 0..5) {
                MessageUtils.broadcastTitle("&c&l${game.time}","&e准备战斗吧！",10,5,10)
                MessageUtils.broadcastMessage("&e距离游戏开始还有&6${game.time}&e秒！")
                MessageUtils.broadcastSound(Sound.WOOD_CLICK,1f,1f)
            }
            if (!this.starts && game.time <= 10) {
                this.starts = true
            }
            if (!this.starts && GamePlayer.getOnlinePlayers().size >= game.maxPlayers) {
                game.time = 10
                this.starts = true
                MessageUtils.broadcastMessage("&e服务器房间已满，倒计时将在10秒后开始！")
            }
        if (game.time <= 1) {
            game.forceStart()
        }
        game.time--
    }
}