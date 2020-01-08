package components.square

import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.dom.*
import react.*
import kotlin.browser.window

interface SquareProps : RProps {
    var onClickFunction: (Event) -> Unit
    var value: String?
}

interface SquareState : RState {
    var isChanged: Boolean
}

class Square : RComponent<SquareProps, SquareState>() {

    override fun SquareState.init() {
        isChanged = false
    }

    override fun RBuilder.render() {
        button(classes = (if(state.isChanged) "square red" else "square")) {
            +(props.value?:"")

            attrs {
                onClickFunction = props.onClickFunction
            }
        }
    }

    override fun componentWillReceiveProps(nextProps: SquareProps) {
//        super.componentWillReceiveProps(nextProps)
        if (state.isChanged && props.value == nextProps.value) {
            setState {
                isChanged = false
            }
        }
        if (props.value != nextProps.value) {
            setState {
                isChanged = true
            }
        }
    }
}

fun RBuilder.square(value: String?, onClick: (Event) -> Unit) = child(Square::class) {
    attrs.value = value
    attrs.onClickFunction = onClick
}

