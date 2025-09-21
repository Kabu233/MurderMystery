package cn.thegoodboys.murdermystery.task

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.game.GamePlayer
import cn.thegoodboys.murdermystery.game.GameStats
import cn.thegoodboys.murdermystery.game.team.GameTeam
import cn.thegoodboys.murdermystery.utils.GameItemUtils.onGame
import cn.thegoodboys.murdermystery.utils.MessageUtils
import cn.thegoodboys.murdermystery.utils.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.Sound

class GameTask: Runnable {

    private val game = MurderMystery.instance.game

    override fun run() {
        if (game.gameStats != GameStats.PLAYING) return

        //获胜统计
        if (game.detectives.isEmpty() && game.civilians.isEmpty()) {
            game.gameStats = GameStats.ENDING
            MessageUtils.teamTitle(GameTeam.MURDERER,"&a你赢了！","&e平民已全部死亡！",10,5,10)
            MessageUtils.teamTitle(GameTeam.CIVILIAN,"&c你输了！","",10,5,10)
            MessageUtils.teamTitle(GameTeam.DETECTIVE,"&c你输了！","&e平民已全部死亡！",10,5,10)
            MessageUtils.teamTitle(GameTeam.DIED,"&c你输了！","",10,5,10)
        }else if (game.murders.isEmpty()) {
            game.gameStats = GameStats.ENDING
            MessageUtils.teamTitle(GameTeam.MURDERER,"&c你输了！","",10,5,10)
            MessageUtils.teamTitle(GameTeam.CIVILIAN,"&a你赢了！","",10,5,10)
            MessageUtils.teamTitle(GameTeam.DETECTIVE,"&a你赢了！","",10,5,10)
            MessageUtils.teamTitle(GameTeam.DIED,"&a你赢了！","",10,5,10)
        }

        //侦探领取武器时间
        if (game.detectiveItem > 0) {
            game.detectiveItem--
        }else {
            //实现给予侦探武器
            game.detectives.forEach { it
                val gamePlayer = GamePlayer.get(it.uniqueId) ?: return@forEach
                if (gamePlayer.gameTeam == GameTeam.DETECTIVE) {
                    //实现给予侦探武器
                    val item = ItemBuilder(Material.BOW).unbreakable(true,true).name("&a弓").build()
                    val item1 = ItemBuilder(Material.ARROW).unbreakable(true,true).name("&a箭").build()
                    gamePlayer.player().inventory.setItem(1,item)
                    gamePlayer.player().inventory.setItem(9,item1)
                }
            }
        }

        //杀手领取武器时间
        if (game.murderItem > 0) {
            if (game.murderItem in 1..5) {
                MessageUtils.teamMessage(GameTeam.MURDERER,"&e你将在&c${game.murderItem}&e秒后获得武器！")
                MessageUtils.teamMessage(GameTeam.CIVILIAN,"&e杀手将在&c${game.murderItem}&e秒后获得武器！")
                MessageUtils.teamMessage(GameTeam.DETECTIVE,"&e杀手将在&c${game.murderItem}&e秒后获得武器！")
                MessageUtils.teamMessage(GameTeam.DIED,"&e杀手将在&c${game.murderItem}&e秒后获得武器！")
            }
            game.murderItem--
        }else {
            //实现给予杀手武器
            val item = ItemBuilder(Material.IRON_SWORD).unbreakable(true,true).name("&a刀").build()
            game.murders.forEach { it
                val gamePlayer = GamePlayer.get(it.uniqueId) ?: return@forEach
                if (gamePlayer.gameTeam == GameTeam.MURDERER) {
                    gamePlayer.player().inventory.setItem(1,item)
                }
            }
            MessageUtils.teamMessage(GameTeam.MURDERER,"&e你拿到了剑！")
            MessageUtils.teamTitle(GameTeam.MURDERER,"","&6右键点击&e来掷出你的剑！",10,5,10)
        }

        //游戏阶段时间
        if (game.gameTime == 60) {
            //给予杀手武器最后60秒
            game.murders.forEach { it
                val gamePlayer = GamePlayer.get(it.uniqueId) ?: return@forEach
                if (gamePlayer.gameTeam == GameTeam.MURDERER) {
                    //实现给予杀手指南针
                    gamePlayer.player().onGame()
                }
            }
        }

        //游戏时间
        if (game.gameTime > 0) {
            if (game.gameTime == 15 || game.gameTime == 10 || game.gameTime in 1..5) {
                MessageUtils.broadcastMessage("&e游戏将在&c${game.gameTime}&e秒后结束！")
                MessageUtils.broadcastSound(Sound.CLICK,1f,1f)
            }
            game.gameTime--
        }else {
            game.gameStats = GameStats.ENDING
            MessageUtils.teamTitle(GameTeam.MURDERER,"&c你输了！","&e时间到！",10,5,10)
            MessageUtils.teamTitle(GameTeam.CIVILIAN,"&a你赢了！","&e杀手已经没有时间了！",10,5,10)
            MessageUtils.teamTitle(GameTeam.DETECTIVE,"&a你赢了！","&e杀手已经没有时间了！",10,5,10)
            MessageUtils.teamTitle(GameTeam.DIED,"&a你赢了！","&e杀手已经没有时间了！",10,5,10)
        }
    }
}