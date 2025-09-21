package cn.thegoodboys.murdermystery.task

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.game.Game
import cn.thegoodboys.murdermystery.game.GamePlayer
import cn.thegoodboys.murdermystery.game.GameStats
import cn.thegoodboys.murdermystery.utils.chat.scoreboard.FastBoard
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.text.SimpleDateFormat
import java.util.Date

class ScoreBoardTask: Listener {

    private var dateFormat = SimpleDateFormat("yy/MM/dd")

    fun set(game: Game, player: Player) {
        val fastBoard = FastBoard(player)
        Bukkit.getScheduler().runTaskTimer(MurderMystery.instance, { updateBoard(game, player, fastBoard) }, 0, 5)
    }

    fun updateBoard(game: Game, player: Player, fastBoard: FastBoard) {
        var list = mutableListOf<String>()
        val gamePlayer = GamePlayer.get(player.uniqueId) ?: return

        fastBoard.updateTitle("&e&l密室杀手")
        list.add("&7# ${dateFormat.format(Date())}")
        list.add(" ")
        when (game.gameStats) {
            GameStats.WAITING-> {
                list.add("&f地图: &a${game.mapName}")
                list.add("&f玩家: &a${GamePlayer.getOnlinePlayers().size}/${game.maxPlayers}")
                list.add(" ")
                list.add("&f等待中...")
                list.add(" ")
                list.add("&f模式: &a经典")
                list.add(" ")
                list.add("&emc91.top")
            }
            GameStats.COUNTDOWN-> {
                list.add("&f地图: &a${game.mapName}")
                list.add("&f玩家: &a${GamePlayer.getOnlinePlayers().size}/${game.maxPlayers}")
                list.add(" ")
                list.add("&f即将开始: &a${game.time}秒")
                list.add(" ")
                list.add("&f模式: &a经典")
                list.add(" ")
                list.add("&bmc91.top")
            }
            GameStats.PLAYING, GameStats.ENDING-> {
                list.add("&f身份: ${gamePlayer.gameTeam.displayName}")
                list.add(" ")
                list.add("&f剩余平民: &a${game.civilians.size}")
                list.add("&f剩余时间: &a${game.gameTime}")
                list.add(" ")
                if (game.detectives.isEmpty() && !game.bowItem) {
                    list.add("&f弓: &a未掉落")
                }else if (game.detectives.isEmpty()) {
                    list.add("&f弓: &c已掉落")
                }else {
                    list.add("&f侦探: &a存活")
                }
                list.add(" ")
                list.add("&f地图: &a${game.mapName}")
                list.add(" ")
                list.add("&bmc91.top")
            }
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val game = MurderMystery.instance.game
        set(game,e.player)
    }
}