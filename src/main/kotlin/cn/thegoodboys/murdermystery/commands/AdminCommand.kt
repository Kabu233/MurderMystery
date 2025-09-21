package cn.thegoodboys.murdermystery.commands

import cn.thegoodboys.murdermystery.MurderMystery
import cn.thegoodboys.murdermystery.utils.chat.CC
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AdminCommand: CommandExecutor {

    private val config = MurderMystery.instance.gameConfig
    private val spawns = mutableListOf<Location>()
    private val golds = mutableListOf<Location>()

    override fun onCommand(sender: CommandSender?, command: Command?, string: String?, args: Array<out String?>?): Boolean {
        if (sender is Player) {
            if (sender.isOp) {
                if (args!!.isNotEmpty()) {
                    when (args[0]) {
                        "help"-> {
                            helpMessage(sender)
                            return true
                        }
                        "create"-> {
                            if (args.size >= 2) {
                                val name = args[1]
                                config.set("Map",name)
                                config.save()
                                CC.send(sender,"&a竞技场创建成功！")
                                return true
                            }else {
                                CC.send(sender,"&c缺少参数！")
                                return false
                            }
                        }
                        "setlobby"-> {
                            config.setLocation(sender.location,"Lobby")
                            config.save()
                            CC.send(sender,"&a等待大厅设置成功！")
                            return true
                        }
                        "setspawn"-> {
                            spawns.add(sender.location)
                            config.setLocations(spawns,"Spawns")
                            config.save()
                            CC.send(sender,"&a出生点设置成功！数量：${spawns.size}")
                            return true
                        }
                        "setgold"-> {
                            golds.add(sender.location)
                            config.setLocations(golds,"Golds")
                            config.save()
                            CC.send(sender,"&a金锭设置成功！数量：${golds.size}")
                            return true
                        }
                        "setmaxplayers"->{
                            if (args.size >= 2) {
                                val maxPlayers = args[1]?.toInt()
                                config.set("MaxPlayers",maxPlayers)
                                config.save()
                                CC.send(sender,"&a游戏总玩家设置成功！")
                                return true
                            }
                        }
                        "setminplayers"->{
                            if (args.size >= 2) {
                                val minPlayers = args[1]?.toInt()
                                config.set("MinPlayers",minPlayers)
                                config.save()
                                CC.send(sender,"&a游戏最小玩家设置成功！")
                                return true
                            }
                        }
                        "setup"-> {
                            MurderMystery.instance.config.set("setup",false)
                            MurderMystery.instance.config.save()
                            CC.send(sender,"&a竞技场设置保存成功！")
                            Bukkit.shutdown()
                            return true
                        }
                    }
                }else {
                    CC.send(sender,"&c缺少参数！")
                    return false
                }
            }else {
                CC.send(sender,"&c你没有使用命令的权限！")
                return false
            }
        }
        return false
    }



    //显示帮助
    private fun helpMessage(sender: Player) {
        val listMessage = mutableListOf(
            "&a&l MurderMystery &e&lAdmin Commands help",
            " ",
            "&a/murdermystery create <name> &7- &f创建竞技场",
            "&a/murdermystery setlobby &7- &f设置等待大厅",
            "&a/murdermystery setspawn &7- &f设置出生点",
            "&a/murdermystery setgold &7- &f设置金锭",
            "&a/murdermystery setpos1 &7- &f设置区域位置1",
            "&a/murdermystery setpos2 &7- &f设置区域位置2",
            "&a/murdermystery setmaxplayers &7- &f设置游戏总玩家",
            "&a/murdermystery setminplayers &7- &f设置游戏最小玩家",
            "&a/murdermystery setup &7- &f保存竞技场",
            " ",
            "&a&l MurderMystery &e&lAdmin Commands help",
        ).joinToString("\n")
        CC.send(sender,listMessage)
    }


}