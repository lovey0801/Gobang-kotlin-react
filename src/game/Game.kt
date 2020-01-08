package game

import components.board.board
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.ol

class Game : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "game") {
            div(classes = "game-board") {
                board()
            }
            div(classes = "game-info") {
                div {  }
                ol {  }
            }
        }
    }
}

fun RBuilder.game() = child(Game::class) {  }