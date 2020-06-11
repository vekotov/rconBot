import Configs.admins
import Configs.rconIP
import Configs.rconPassword
import Configs.rconPort
import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.model.VkSettings
import org.omg.CORBA.Object
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.system.exitProcess

object Configs {
    var groupId: Int = 0
    var accessToken: String = ""
    var rconIP: String = ""
    var rconPort: Int = 0
    var rconPassword: String = ""
    val admins: MutableList<Int> = mutableListOf()

    init {
        val fileName = "config.yml"
        val file = File(fileName)
        if (file.exists()) {
            if (!loadConfigs(file)) exitProcess(1)
        } else {
            val text = javaClass.getResource("config.yml").readText() //getting config file from JAR
            file.createNewFile()
            file.writeText(text)
            if (!loadConfigs(file)) exitProcess(1)
        }
    }

    var rcon = MinecraftRcon(rconIP, rconPort)

    init {
        if (!rcon.authenticate(rconPassword)) {
            println("Error - cannot connect to rcon!")
            exitProcess(2)
        } else {
            println("Connected to rcon!")
        }
    }

    var client = VkApiClient(groupId, accessToken, VkApiClient.Type.Community, VkSettings(vkHttpClient))
}

fun loadConfigs(file: File): Boolean {
    val yaml = Yaml()
    val stream = file.inputStream()
    val data: Map<String, Object> = yaml.load(stream)

    Configs.accessToken = if (data.containsKey("token")) data["token"].toString()
    else "".also { println("Error, token not found in config.yml"); return false }

    Configs.groupId = if (data.containsKey("groupID")) data["groupID"].toString().toInt()
    else 0.also { println("Error, groupID not found in config.yml"); return false }

    rconIP = if (data.containsKey("rconIP")) data["rconIP"].toString()
    else "".also { println("Error, rconIP not found in config.yml"); return false }

    rconPort = if (data.containsKey("rconPort")) data["rconPort"].toString().toInt()
    else 0.also { println("Error, rconIP not found in config.yml"); return false }

    rconPassword = if (data.containsKey("rconPassword")) data["rconPassword"].toString()
    else "".also { println("Error, rconIP not found in config.yml"); return false }

    try {
        val listOfAdmins = if (data.containsKey("admins")) data["admins"].toString() else ""
        listOfAdmins.removeSurrounding("[", "]")
                .split(", ")
                .forEach { admins.add(it.toInt()) }
    } catch (e: Exception) {
        return false
    }
    return true
}