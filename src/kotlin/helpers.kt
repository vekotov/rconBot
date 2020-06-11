import Configs.admins
import Configs.client
import com.petersamokhin.vksdk.core.model.objects.Keyboard
import com.petersamokhin.vksdk.core.model.objects.keyboard

fun sendMessage(toId: Int, text: String, keys: Keyboard = getKeyboard()) {
    client.sendMessage {
        peerId = toId
        message = text
        keyboard = keys
    }.execute()
}

fun getKeyboard(): Keyboard {
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

fun isAdmin(userId: Int): Boolean {
    return admins.contains(userId)
}