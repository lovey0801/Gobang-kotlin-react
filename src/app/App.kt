package app

import components.*
import react.*
import react.dom.*

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        val hello = "Hello"
        val world = "World"
        val c = "!"
        h1 {
            +"$hello $world $c"
        }
        ticker()
        welcome("Eagle", "YaJuan")
        img {
            attrs {
                src = "https://static.docschina.org/docschina-qr1.jpeg"
                width = "320"
                height = "320"
            }
        }
    }
}

fun RBuilder.app() = child(App::class) {}