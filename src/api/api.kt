package api

import models.Game
import org.w3c.fetch.*
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Promise

const val HOST = "http://192.168.24.15:8088/"

class GameResp<T>(var code: Int, var msg: String, var data: T)

fun <T> get(url: String): Promise<T?> {
    var r = Request(url)
    return window.fetch(r).then { response ->
        if (response.status as Int == 200) {
            return@then response.text()
        } else {
            return@then null
        }
    }.then {
        if (it != null) {
            val resp = JSON.parse<GameResp<T>>(it.toString())
            if (resp.code != 0) {
                console.error("error[$url]: ${resp.msg}")
                return@then null
            } else {
                return@then resp.data
            }
        } else {
            return@then null
        }
    }
}

fun enter(name: String): Promise<Game?> {
    var url = HOST + "enter?name=" + name
    return get<Game>(url)
}

fun ready(tableCode: Int, role: String): Promise<Game?> {
    var url = "${HOST}ready?tableCode=$tableCode&role=$role"
    return get<Game>(url)
}

fun play(tableCode: Int, role: String, position: Int): Promise<Game?> {
    var url = "${HOST}play?tableCode=$tableCode&role=$role&position=$position"
    return get<Game>(url)
}
