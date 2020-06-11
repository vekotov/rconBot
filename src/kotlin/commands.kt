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
    sendMessage(peerId, text)
}

fun whitelist(peerId: Int) {
    val text = rcon.command("whitelist list")
    sendMessage(peerId, text)
}

fun rconCommand(peerId: Int, command: String) {
    val text = rcon.command(command)
    sendMessage(peerId, text)
}