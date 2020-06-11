import java.io.DataInputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicInteger

class MinecraftRcon(host: String, port: Int) {
    private val socket: Socket = Socket(host, port)
    private val outputStream
        get() = socket.getOutputStream()
    private val inputStream
        get() = socket.getInputStream()
    private val dataInputStream
        get() = DataInputStream(inputStream)
    private val requestIdCounter: AtomicInteger = AtomicInteger(1)

    fun command(cmd: String): String {
        val response = send(RconPacket.RequestType.COMMAND, cmd.toByteArray())
        return response.payload.toString(Charset.defaultCharset())
    }

    fun authenticate(password: String): Boolean {
        val response = send(RconPacket.RequestType.LOGIN, password.toByteArray())
        return response.requestId == response.responseId
    }

    fun send(type: RconPacket.RequestType, payload: ByteArray): RconPacket {
        val requestSize = 4 + 4 + payload.size + 2
        val requestId = requestIdCounter.getAndIncrement()
        val request = ByteBuffer.allocate(4 + requestSize).apply {
            order(ByteOrder.LITTLE_ENDIAN)
            putInt(requestSize)
            putInt(requestId)
            putInt(type.id)
            put(payload)
            put(0)
            put(0)
        }
        return synchronized(socket) {
            outputStream.write(request.array())
            val header = ByteArray(3 * 4)
            dataInputStream.read(header)
            val usableHeader = ByteBuffer.wrap(header).apply {
                order(ByteOrder.LITTLE_ENDIAN)
            }
            val size = usableHeader.int
            val responseId = usableHeader.int
            val responseType = RconPacket.RequestType.getById(usableHeader.int)

            val responsePayload = ByteArray(size - 4 - 4 - 2)

            dataInputStream.readFully(responsePayload)
            dataInputStream.readFully(ByteArray(2))
            RconPacket(size, requestId, responseId, responseType, responsePayload)
        }
    }
}

class RconPacket(val size: Int, val requestId: Int, val responseId: Int, val type: RequestType, val payload: ByteArray) {
    enum class RequestType(val id: Int) {
        COMMAND_RESPONSE(0),
        COMMAND(2),
        LOGIN(3);

        companion object {
            fun getById(id: Int) = RequestType.values().single {it.id == id}
        }
    }
}