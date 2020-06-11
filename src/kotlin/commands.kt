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
    val text = rcon.command("list")
    val regex = "§c\\d+".toRegex()
    val online = regex.find(text)?.value?.replace("§c", "")?.toInt() ?: 0
    sendMessage(peerId, "Текущий онлайн - $online игроков.")
}

fun whitelist(peerId: Int) {
    val text = rcon.command("whitelist list")
    sendMessage(peerId, text)
}

fun rconCommand(peerId: Int, command: String) {
    val text = rcon.command(command)
    sendMessage(peerId, text)
}