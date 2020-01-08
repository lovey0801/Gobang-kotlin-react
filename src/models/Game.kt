package models

class Game(var table: Table?, var state: Int = IDLE, var winner: String?, var curPlayer: String?, var timestamp: String = "") {
    companion object GameState {
        const val IDLE = 0
        const val GAMING = 1
        const val GAME_OVER = 2
    }
}