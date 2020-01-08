package index

import game.game
import kotlinext.js.*
import react.dom.*
import kotlin.browser.document

fun main(args: Array<String>) {
    requireAll(require.context("src", true, js("/\\.css$/")))

    render(document.getElementById("root")) { game() }
}