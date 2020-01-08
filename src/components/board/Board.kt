package components.board

import react.*
import components.square.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import models.Game
import models.Player
import org.w3c.dom.HTMLInputElement
import react.dom.button
import react.dom.div
import react.dom.input
import kotlin.browser.document
import kotlin.browser.window

interface BoardState : RState {
    var game: Game?
    var myself: Player?
    var competitor: Player?
}

class Board : RComponent<RProps, BoardState>() {

    var loopValue:Int? = null

    override fun BoardState.init() {

    }

    private fun RBuilder.renderSquare(i: Int) {
        if (state.game != null) {
            var squares = state.game?.table?.squares
            if (squares != null) {
                square(squares[i]) {
                    if (squares[i].isNotEmpty() || state.game?.state != Game.GAMING) {
                        return@square
                    }
                    if (state.game?.curPlayer == state.myself?.role) {
                        play(i)
                    } else {
                        window.alert("Now is not your turn.")
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (state.game == null) {
            var inputValue = ""
            input {
                attrs {
                    type = InputType.text
                    placeholder = "输入你的昵称"
                    id = "nickname"
                    onChangeFunction = {
                        inputValue = it.type
                    }
                }
            }
            button {
                +"进入游戏"
                attrs {
                    onClickFunction = {
                        (document.getElementById("nickname") as HTMLInputElement).value.let { nickname ->
                            setState {
                                myself = Player(nickname, null)
                            }
                            api.enter(nickname).then {game ->
                                setGameState(game)
                                loopValue = window.setInterval({
                                    play()
                                }, 2000)
                            }
                        }
                    }
                }
            }
            return
        }
        val status = if (state.game?.state != Game.IDLE)
                        if (state.game?.winner == null)
                            if (state.game?.state == Game.GAME_OVER)
                                "Game Over! No Winners!"
                            else
                                "Next player: ${state.game?.curPlayer}"
                        else
                            "Winner player is ${state.game?.winner}"
                      else
                          "Game not start"

        val myselfStatus = state.myself?.let {
            "Me[name: ${it.name}, role: ${if (it.role != null) it.role else "Empty"}, ready: ${it.readyState}]"
        }

        val competitorStatus = state.competitor?.let {
            "Com[name: ${it.name}, role: ${if (it.role != null) it.role else "Empty"}, ready: ${it.readyState}]"
        }

        div {
            div(classes = "status") {
                +status
            }
            div(classes = "status") {
                +competitorStatus!!
            }
            var size = state.game?.table?.squares?.size ?: 0
            var cols = state.game?.table?.cols ?: 1
            for (i in 0 until size / cols + (if (size % cols > 0) 1 else 0)) {
                div(classes = "board-row") {
                    for (j in 0 until cols) {
                        val index = i * cols + j
                        if (j < size) {
                            renderSquare(index)
                        }
                    }
                }
            }
            div(classes = "status") {
                +myselfStatus!!
            }
            div {
                button {
                    +"准备"
                    attrs {
                        onClickFunction = {
                            val code = state.game?.table?.code
                            val role = state.myself?.role
                            if (code == null || role == null) {
                                console.error("ready error!!!")
                            } else {
                                api.ready(code, role).then { game ->
                                    setGameState(game)
                                }
                            }
                        }

                        disabled = state.myself?.readyState ?: true
                    }
                }
            }
        }
    }

    private fun calculateWinner(squares: Array<String?>) : String? {
        val lines = arrayOf(
                arrayOf(0, 1, 2),
                arrayOf(3, 4, 5),
                arrayOf(6, 7, 8),
                arrayOf(0, 3, 6),
                arrayOf(1, 4, 7),
                arrayOf(2, 5, 8),
                arrayOf(0, 4, 8),
                arrayOf(2, 4, 6)
        )

        for (e in lines) {
            if (squares[e[0]] != null && squares[e[0]] == squares[e[1]] && squares[e[0]] == squares[e[2]]) {
                return squares[e[0]]
            }
        }

        return null
    }

    private fun calculateGameOver(squares: Array<String?>) : Boolean {
        var gameOver = true
        for (e in squares) {
            if (e == null) {
                gameOver = false
                break
            }
        }
        return gameOver
    }

    private fun setGameState(game: Game?) {
        if (game == null) {
            console.error("enter error!")
        } else {
            if (state.game != null && state.game!!.timestamp >= game.timestamp) {
                return
            }
            setState {
                this.game = game
                if (game?.table?.playerX?.name.equals(state.myself?.name)) {
                    this.myself = game?.table?.playerX
                    this.competitor = game?.table?.playerO
                } else {
                    this.myself = game?.table?.playerO
                    this.competitor = game?.table?.playerX
                }
            }
        }
    }

    private fun play(position: Int = -1) {
        val code = state.game?.table?.code
        val role = state.myself?.role
        if (code == null || role == null) {
            console.error("play error!!!")
        } else {
            api.play(code, role, position).then { game ->
                setGameState(game)
            }
        }
    }
}

fun RBuilder.board() = child(Board::class) {  }