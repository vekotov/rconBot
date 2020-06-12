import Configs.rcon

fun banlist(peerId: Int) {
    val text = rcon.command("banlist players")
    sendMessage(peerId, text)
}

fun usersOnline(peerId: Int) {
    val text = rcon.command("list")
    sendMessage(peerId, text)
}

fun online(peerId: Int) {
    val text = rcon.command("minecraft:list")
    val regex = "\\d+".toRegex()
    val online = regex.find(text)?.value?.toInt() ?: 0
    sendMessage(peerId, "Текущий онлайн - $online игроков.")
}

fun whitelist(peerId: Int) {
    val text = rcon.command("whitelist list")
    sendMessage(peerId, text)
}

fun rconCommand(peerId: Int, command: String) {
    if (!command.startsWith("/")) return

    val text = rcon.command(command)
    sendMessage(peerId, text)
}