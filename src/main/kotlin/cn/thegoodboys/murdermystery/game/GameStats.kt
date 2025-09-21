package cn.thegoodboys.murdermystery.game

enum class GameStats(val displayName: String) {
    WAITING("等待中"),
    COUNTDOWN("等待中"),
    PLAYING("游玩中"),
    ENDING("结束！");
}