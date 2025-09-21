package cn.thegoodboys.murdermystery.game.team

enum class GameTeam(val displayName: String) {
    WAITING("§f§l无身份"),
    DETECTIVE("§b§l侦探"),
    MURDERER("§c§l杀手"),
    CIVILIAN("§a§l平民"),
    DIED("§7§l死亡");
}