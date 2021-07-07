package work.avila.flashlight

import android.hardware.SensorEvent
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.TorchState

class Flashlight (camera: Camera) {
    private var cameraControl: CameraControl = camera.cameraControl
    private var cameraInfo: CameraInfo = camera.cameraInfo

    fun toggle(value: Boolean? = null) {
        val toggle = value ?: (cameraInfo.torchState.value != TorchState.ON)
        if (cameraInfo.hasFlashUnit()) {
            cameraControl.enableTorch(toggle)
        }
    }

    fun toggleBasedOnLuminosity(sensorEvent: SensorEvent?, switch: Boolean = false) {
        if (switch && sensorEvent?.values !== null) {
            val result = sensorEvent.values[0]
            toggle(result < 200)
        }
    }
}