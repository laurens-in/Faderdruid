package com.example.faderdruid.ui.model

import android.app.Application
import android.content.Context
import android.content.Context.MIDI_SERVICE
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.faderdruid.utils.MidiDruid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ConnectionState(val deviceConnected: Boolean, val portOpen: Boolean)

class MidiViewModel(application: Application): AndroidViewModel(application) {

    private val druid = MidiDruid(requireNotNull(application.getSystemService(Application.MIDI_SERVICE) as MidiManager?))
    private val _connectionState =
        MutableStateFlow<ConnectionState>(ConnectionState(deviceConnected = false, portOpen = false))
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    val devices: Array<MidiDeviceInfo>
        get() = druid.availableDevices

    fun connect(d: MidiDeviceInfo) {
        druid.connectDevice(d) { t ->
            _connectionState.update { state ->
                state.copy(
                    deviceConnected = t,
                    portOpen = t
                )
            }
        }
    }

    fun sendCC(ccNum: Int, ccVal: Int) {
        druid.sendCC(ccNum,ccVal)
    }
}