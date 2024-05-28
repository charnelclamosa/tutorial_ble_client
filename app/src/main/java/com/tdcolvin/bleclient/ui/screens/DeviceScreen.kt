package com.tdcolvin.bleclient.ui.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tdcolvin.bleclient.ble.CTF_SERVICE_UUID
import org.json.JSONObject

@Composable
fun DeviceScreen(
    unselectDevice: () -> Unit,
    isDeviceConnected: Boolean,
    discoveredCharacteristics: Map<String, List<String>>,
    password: String?,
    nameWrittenTimes: Int,
    connect: () -> Unit,
    discoverServices: () -> Unit,
    displayDeviceInfo: () -> Unit,
    writeName: () -> Unit
) {
    val foundTargetService = discoveredCharacteristics.contains(CTF_SERVICE_UUID.toString())

    Column(
        Modifier.scrollable(rememberScrollState(), Orientation.Vertical)
    ) {
        Button(onClick = connect) {
            Text("Connect")
        }
        Text("Device connected: $isDeviceConnected")
        Button(onClick = discoverServices, enabled = isDeviceConnected) {
            Text("Discover Services")
        }
        LazyColumn {
            items(discoveredCharacteristics.keys.sorted()) { serviceUuid ->
                Text(text = serviceUuid, fontWeight = FontWeight.Black)
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    discoveredCharacteristics[serviceUuid]?.forEach {
                        Text(it)
                    }
                }
            }
        }
        Button(onClick = displayDeviceInfo, enabled = isDeviceConnected && foundTargetService) {
            Text("Display device information")
        }
        if (password != null) {
            var json = JSONObject(password)
            Text("Device information:\nDevice name: ${json.getString("device_name")}\nDevice model: ${json.getString("device_model")}\nBattery level: ${json.getString("battery_level")}")
        }
//        Button(onClick = writeName, enabled = isDeviceConnected && foundTargetService) {
//            Text("4. Write Your Name")
//        }
//        if (nameWrittenTimes > 0) {
//            Text("Successful writes: $nameWrittenTimes")
//        }

        OutlinedButton(modifier = Modifier.padding(top = 40.dp),  onClick = unselectDevice, enabled = isDeviceConnected) {
            Text("Disconnect")
        }
    }
}
