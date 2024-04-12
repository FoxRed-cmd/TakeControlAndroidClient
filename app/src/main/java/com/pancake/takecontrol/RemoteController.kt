package com.pancake.takecontrol

import android.util.Log
import java.net.Socket

class RemoteController {
    var ipAddress: String = "0.0.0.0"
    var port: Int = 5000

    constructor(ipAddress: String, port: Int) {
        this.ipAddress = ipAddress
        this.port = port
    }

    fun volumeDown() {  sendCommand("/voldown") }
    fun volumeOff() { sendCommand("/voloff") }
    fun volumeUp() {  sendCommand("/volup") }
    fun previousStep() { sendCommand("/previousStep") }
    fun pause() { sendCommand("/pause") }
    fun nextStep() { sendCommand("/nextStep") }
    fun previousPlay() { sendCommand("/previous") }
    fun stop() {  sendCommand("/stop") }
    fun nextPlay() { sendCommand("/next") }
    fun power() { sendCommand("/power") }
    fun restart() { sendCommand("/restart") }
    fun sleep() { sendCommand("/sleep") }

    private fun sendCommand(command: String) {
        Thread {
            try {
                val socket = Socket(ipAddress, port)
                val outputStream = socket.getOutputStream()
                val data = command.toByteArray()
                outputStream.write(data)
                outputStream.flush()
                socket.close()
            } catch (e: Exception) {
                // Handle connection errors
                Log.e("SocketError", "Error sending command: ${e.message}")
            }
        }.start()
    }
}