import Configs.client
import com.petersamokhin.vksdk.core.model.event.MessageNew

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            client.onMessage { messageEvent ->
                onMessage(messageEvent)
            }

            println("Server started.")
            client.startLongPolling()
        }
    }
}

fun onMessage(event : MessageNew){
    val peerId = event.message.peerId
    val userId = event.message.fromId
    var text = event.message.text

    val regex = "\\[\\w+\\|@\\w+]\\s".toRegex() //regex finding references
    val foundReference = regex.find(text)?.value ?: ""

    text = text.replace(foundReference, "") //removes refs

    println("Got message '$text' from id '$userId'.")

    if (isAdmin(userId)) {
        println("User with id '$userId' is admin.")
        when (text) {
            "банлист" -> banlist(peerId)
            "онлайни" -> usersOnline(peerId)
            "онлайн" -> online(peerId)
            "вайтлистлист" -> whitelist(peerId)
            else -> rconCommand(peerId, event.message.text)
        }
    } else {
        println("User with id '$userId' is not admin!")
        sendMessage(peerId, "Вы не являетесь админом данного сервера.")
    }
}