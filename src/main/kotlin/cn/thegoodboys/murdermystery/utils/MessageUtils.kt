package cn.thegoodboys.murdermystery.utils

import cn.thegoodboys.murdermystery.game.GamePlayer
import cn.thegoodboys.murdermystery.game.team.GameTeam
import cn.thegoodboys.murdermystery.utils.chat.ActionBarUtil
import cn.thegoodboys.murdermystery.utils.chat.CC
import org.bukkit.Sound

object MessageUtils {

    fun broadcastMessage(message: String) {
        GamePlayer.getOnlinePlayers().forEach {
            CC.send(it.player(),message)
        }
    }

    fun broadcastTitle(title: String,subtitle: String,fadeIn: Int,stay: Int,fadeOut: Int) {
        GamePlayer.getOnlinePlayers().forEach {
            TitleUtil.sendTitle(it.player(),fadeIn,stay,fadeOut,title,subtitle)
        }
    }

    fun broadcastActionBar(message: String) {
        GamePlayer.getOnlinePlayers().forEach {
            ActionBarUtil.sendActionBar(it.player(),message)
        }
    }

    fun broadcastSound(sound: Sound,volume: Float,pitch: Float) {
        GamePlayer.getOnlinePlayers().forEach {
            it.player().playSound(it.player().location,sound,volume,pitch)
        }
    }

    fun teamMessage(gameTeam: GameTeam,message: String) {
        GamePlayer.getOnlinePlayers().forEach {
            if (it.gameTeam == gameTeam) {
                CC.send(it.player(),message)
            }
        }
    }

    fun teamTitle(gameTeam: GameTeam,title: String, subtitle: String,fadeIn: Int,stay: Int,fadeOut: Int) {
        GamePlayer.getOnlinePlayers().forEach {
            if (it.gameTeam == gameTeam) {
                TitleUtil.sendTitle(it.player(),fadeIn,stay,fadeOut,title,subtitle)
            }
        }
    }

    fun teamActionBar(gameTeam: GameTeam,message: String) {
        GamePlayer.getOnlinePlayers().forEach {
            if (it.gameTeam == gameTeam) {
                ActionBarUtil.sendActionBar(it.player(),message)
            }
        }
    }

    fun teamSound(gameTeam: GameTeam,sound: Sound, volume: Float, pitch: Float) {
        GamePlayer.getOnlinePlayers().forEach {
            if (it.gameTeam == gameTeam) {
                it.player().playSound(it.player().location,sound,volume,pitch)
            }
        }
    }

}