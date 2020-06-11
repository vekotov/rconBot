import Configs.client
import com.petersamokhin.vksdk.core.model.event.MessageNew
import com.petersamokhin.vksdk.http.VkOkHttpClient


var vkHttpClient = VkOkHttpClient()


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
    println("Got message '${event.message.text}' from id '$userId'.")
    if(isAdmin(userId)){
        println("User with id '$userId' is admin.")
        when(event.message.text){
            "банлист" -> banlist(peerId)
            "онлайни" -> usersOnline(peerId)
            "онлайн" -> online(peerId)
            "вайтлистлист" -> whitelist(peerId)
            else -> rconCommand(peerId, event.message.text)
        }
    }else{
        println("User with id '$userId' is not admin!")
        sendMessage(peerId, "Вы не являетесь админом данного сервера.")
    }
}