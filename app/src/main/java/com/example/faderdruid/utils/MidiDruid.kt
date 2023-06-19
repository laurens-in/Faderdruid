package com.example.faderdruid.utils

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.os.Handler
import android.os.Looper
import android.util.Log

class MidiDruid(private val m: MidiManager) {
    private var device: MidiDevice? = null
    private var port: MidiInputPort? = null

    val availableDevices: Array<MidiDeviceInfo>
        get() = m.devices

    fun connectDevice(d: MidiDeviceInfo, cb: (t: Boolean) -> Unit) {
        m.openDevice(d, { d ->
            device = d
            port = d.openInputPort(0)
            cb(true)
        }, Handler(Looper.getMainLooper()))
    }

    fun closeDevice() {
        port?.close()
        device?.close()
    }

    fun sendCC(ccNum: Int, ccVal: Int) {
        if (port != null) {
            val buffer = ByteArray(32)
            var numBytes = 0
            val channel = 1 // MIDI channels 1-16 are encoded as 0-15.
            buffer[numBytes++] = (0xB0 + (channel - 1)).toByte() // CC message
            buffer[numBytes++] = ccNum.toByte() // CC number
            buffer[numBytes++] = ccVal.toByte() // CC value
            val offset = 0
            // post is non-blocking
            port!!.send(buffer, offset, numBytes)
            Log.d("SENDING MIDI: ", "$ccNum $ccVal")
        }
    }
}