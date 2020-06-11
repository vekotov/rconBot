import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.model.VkSettings
import com.petersamokhin.vksdk.core.model.event.MessageNew
import com.petersamokhin.vksdk.core.model.objects.Keyboard
import com.petersamokhin.vksdk.core.model.objects.keyboard
import com.petersamokhin.vksdk.http.VkOkHttpClient

const val groupId = 0
const val accessToken = "0fb186c...."
const val rconIP = "127.0.0.1"
const val rconPort = 11121
const val rconPassword = "12345678"
val admins = listOf<Int>(1, 2)

val vkHttpClient = VkOkHttpClient()
val rcon = MinecraftRcon(rconIP, rconPort)
val client = VkApiClient(groupId, accessToken, VkApiClient.Type.Community, VkSettings(vkHttpClient))

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if(!rcon.authenticate(rconPassword)){
                println("Error - cannot connect to rcon!")
                return
            }else{
                println("Connected to rcon!")
            }
            client.onMessage { messageEvent ->
                onMessage(messageEvent)
            }

            println("Server started.")
            client.startLongPolling()
        }
    }
}

fun getKeyboard() : Keyboard{
    return keyboard(oneTime = true) {
        row {
            negativeButton("банлист")
        }
        row {
            positiveButton("онлайни")
        }
        row {
            primaryButton("онлайн")
        }
        row {
            secondaryButton("вайтлистлист")
        }
    }
}

fun isAdmin(userId : Int) : Boolean{
    return admins.contains(userId)
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

fun sendMessage(toId : Int, text : String, keys : Keyboard = getKeyboard()){
    client.sendMessage {
        peerId = toId
        message = text
        keyboard = keys
    }.execute()
}

fun banlist(peerId : Int){
    var text = rcon.command("banlist players")
    sendMessage(peerId, text)
}

fun usersOnline(peerId : Int){
    var text = rcon.command("list")
    sendMessage(peerId, text)
}

fun online(peerId: Int){
    var text = rcon.command("list")
    sendMessage(peerId, text)
}

fun whitelist(peerId: Int){
    var text = rcon.command("whitelist list")
    sendMessage(peerId, text)
}

fun rconCommand(peerId: Int, command : String){
    var text = rcon.command(command)
    sendMessage(peerId, text)
}