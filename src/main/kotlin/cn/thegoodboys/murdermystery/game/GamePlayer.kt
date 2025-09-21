package cn.thegoodboys.murdermystery.game

import cn.thegoodboys.murdermystery.game.team.GameTeam
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.ArrayList
import java.util.UUID

class GamePlayer(val uuid: UUID, val name:String) {

    var gameTeam: GameTeam = GameTeam.WAITING
    var kills = 0
    var isCharging = false //是否正在蓄力

    companion object{
        //使用map来存储所有玩家
        private val gamePlayers = mutableMapOf<UUID, GamePlayer>()

        fun create(uuid: UUID, name: String): GamePlayer {
            var gamePlayer = get(uuid)
            if (gamePlayer != null) {
                return gamePlayer
            }
            gamePlayer = GamePlayer(uuid, name)
            gamePlayers[uuid] = gamePlayer
            return gamePlayer
        }

        fun get(uuid: UUID): GamePlayer? {
            for (gamePlayer in gamePlayers.values) {
                if (gamePlayer.uuid == uuid) {
                    return gamePlayer
                }
            }
            return null
        }

        fun getOnlinePlayers(): List<GamePlayer> {
            val onlinePlayers: MutableList<GamePlayer> = ArrayList()
            for (gamePlayer in gamePlayers.values) {
                if (gamePlayer.isOnline()) {
                    onlinePlayers.add(gamePlayer)
                }
            }
            return onlinePlayers
        }
    }

    fun isOnline(): Boolean {
        return Bukkit.getPlayer(uuid) != null
    }

    fun player(): Player = Bukkit.getPlayer(uuid)
}