package cn.thegoodboys.murdermystery.game

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.game.team.GameTeam
import cn.thegoodboys.murdermystery.task.CountDown
import cn.thegoodboys.murdermystery.task.GameTask
import cn.thegoodboys.murdermystery.task.SpawnGoldTick
import cn.thegoodboys.murdermystery.utils.GameItemUtils.onLobby
import cn.thegoodboys.murdermystery.utils.MessageUtils
import cn.thegoodboys.murdermystery.utils.chat.CC
import cn.thegoodboys.murdermystery.utils.item.ItemBuilder
import com.nametagedit.plugin.NametagEdit
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import kotlin.properties.Delegates

class Game {

    private val config = MurderMystery.instance.gameConfig

    val gameEnable = MurderMystery.instance.config.getBoolean("setup")
    lateinit var gameStats: GameStats
    var time = 120
    private var bukkitTask: BukkitTask? = null
    private var countdownTask = CountDown()
    //存储所有玩家为随机队伍
    val randomTeams = mutableListOf<Player>()
    //存储所有玩家为侦探
    val detectives = mutableListOf<Player>()
    //存储所有玩家为杀手
    val murders = mutableListOf<Player>()
    //存储所有玩家为平民
    val civilians = mutableListOf<Player>()
    //存储侦探玩家名称
    val detectivesName = mutableListOf<String>()
    //存储杀手玩家名称
    val murdersName = mutableListOf<String>()
    //存储英雄平民名称
    val civiliansName = mutableListOf<String>()


    var gameTime = 255
    //杀手领取武器时间
    var murderItem = 10
    //侦探领取武器时间
    var detectiveItem = 10
    //弓掉落
    var bowItem = false

    lateinit var mapName: String
    lateinit var lobbyLocation: Location
    lateinit var spawnLocation: MutableList<Location>
    lateinit var goldLocation: MutableList<Location>
    var maxPlayers by Delegates.notNull<Int>()
    var minPlayers by Delegates.notNull<Int>()

    //读取游戏
    fun load() {
        //绑图阶段不允许加载游戏
        if (gameEnable) {
            return
        }
        gameStats = GameStats.WAITING
        try {
            mapName = config.getString("Map")
            lobbyLocation = config.getLocation("Lobby")!!
            spawnLocation = config.getLocations("Spawns").toMutableList()
            goldLocation = config.getLocations("Golds").toMutableList()
            maxPlayers = config.getInt("MaxPlayers")
            minPlayers = config.getInt("MinPlayers")
            Bukkit.getLogger().info("游戏加载成功！")
        }catch (e: Exception){
            e.printStackTrace()
            Bukkit.getLogger().info("游戏加载失败！")
        }
    }

    //加入游戏
    fun addPlayer(player: Player) {
        val gamePlayer = GamePlayer.create(player.uniqueId,player.name)
        gamePlayer.player().inventory.clear()
        gamePlayer.player().inventory.armorContents = null
        gamePlayer.player().gameMode = GameMode.ADVENTURE
        gamePlayer.player().spigot().respawn()
        gamePlayer.player().inventory.clear()
        gamePlayer.player().foodLevel = 40
        gamePlayer.player().maxHealth = 20.0
        gamePlayer.player().health = 20.0
        gamePlayer.player().activePotionEffects.forEach { gamePlayer.player().removePotionEffect(it.type) }
        gamePlayer.player().teleport(lobbyLocation)
        gamePlayer.player().onLobby()
        if (gameStats == GameStats.WAITING && (GamePlayer.getOnlinePlayers().size >= minPlayers)) {
            bukkitTask?.cancel()
            countdownTask.start(this).also { bukkitTask = Bukkit.getScheduler().runTaskTimer(MurderMystery.instance,countdownTask,0,20) }
        }
        NametagEdit.getApi().clearNametag(gamePlayer.player())
        NametagEdit.getApi().setNametag(gamePlayer.player(), "§7", "")
        MessageUtils.broadcastMessage("&7${gamePlayer.name} &e加入了游戏！ &e(${GamePlayer.getOnlinePlayers().size}&b/&e${maxPlayers})")
    }

    //离开游戏
    fun leavePlayer(player: Player) {
        val gamePlayer = GamePlayer.get(player.uniqueId) ?: return
        MessageUtils.broadcastMessage("&e${gamePlayer.name} 离开了游戏！")
    }


    fun forceStart() {
        this.start()
    }

    private fun start() {
        gameStats = GameStats.PLAYING
        GamePlayer.getOnlinePlayers().forEach {
            it.player().activePotionEffects.forEach { potions -> it.player().removePotionEffect(potions.type) }
            it.player().inventory.clear()
            it.player().level = 0
            it.player().gameMode = GameMode.SURVIVAL
            it.player().inventory.armorContents = null
            it.player().exp = 0.0f
            if (spawnLocation.isEmpty()) {
                CC.send(it.player(), "&c&l警告！&r&c 未设置出生点！")
                it.player().teleport(lobbyLocation)
                return@forEach
            }
            randomTeams.add(it.player())
            it.player().teleport(spawnLocation.random())
        }
        assignTeam()
        teamInfo()
        //侦探
        randomTeams.forEach {
            val gamePlayer = GamePlayer.get(it.uniqueId) ?: return

        }
        //启动游戏线程
        Bukkit.getScheduler().runTaskTimer(MurderMystery.instance, GameTask(),0,20)
        Bukkit.getScheduler().runTaskTimer(MurderMystery.instance, SpawnGoldTick(),0,20 * 30)


        MessageUtils.broadcastMessage("&c&l严禁与杀手联手")
    }


    //分配队伍
    fun assignTeam() {
        // 随机打乱玩家列表
        randomTeams.shuffle()
        //队伍分配
        val detective = randomTeams.removeAt(0)
        val murder = randomTeams.removeAt(0)
        //设置队伍
        val gamePlayer1 = GamePlayer.get(detective.uniqueId) ?: return
        val gamePlayer2 = GamePlayer.get(murder.uniqueId) ?: return
        //设置队伍信息
        gamePlayer1.gameTeam = GameTeam.DETECTIVE
        gamePlayer2.gameTeam = GameTeam.MURDERER
        //存入list
        detectives.add(detective)
        detectivesName.add(detective.name)
        murders.add(murder)
        murdersName.add(murder.name)
        //设置剩下的队伍
        randomTeams.forEach {
            val gamePlayer = GamePlayer.get(it.uniqueId) ?: return
            gamePlayer.gameTeam = GameTeam.CIVILIAN
            civilians.add(it)
        }
    }

    //队伍信息及音效
    fun teamInfo() {
        MessageUtils.teamTitle(GameTeam.CIVILIAN,"&a身份: 平民","&e努力存活下来！活着就是胜利！",10,5,10)
        MessageUtils.teamTitle(GameTeam.DETECTIVE,"&a身份: &b侦探","&e找到杀手！并且将其击败！",10,5,10)
        MessageUtils.teamTitle(GameTeam.MURDERER,"&c身份: &c杀手","&e杀死所有玩家！让街道上尸横遍野！",10,5,10)

        MessageUtils.teamSound(GameTeam.CIVILIAN, Sound.VILLAGER_HIT,1f,1f)
        MessageUtils.teamSound(GameTeam.DETECTIVE, Sound.LEVEL_UP,1f,1f)
        MessageUtils.teamSound(GameTeam.MURDERER, Sound.WITHER_DEATH,1f,1f)
    }

}