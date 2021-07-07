package work.avila.flashlight

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import work.avila.flashlight.databinding.ActivityMainBinding

import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private var detectDarkness: Boolean = false
    private var sosModeJob: Job? = null
    private var sosMode: Boolean = false
    private var flashlight: Flashlight? = null
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detectDarkness.text = getString(R.string.detect_darkness, "OFF")
        binding.sosMode.text = getString(R.string.sos_mode,"OFF")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.toggle.setOnClickListener { onToggle() }
        binding.detectDarkness.setOnClickListener { onDetectDarkness() }
        binding.sosMode.setOnClickListener { onSOSMode() }
    }

    private fun onToggle() {
        flashlight?.toggle()
    }

    private fun onDetectDarkness() {
        detectDarkness = !detectDarkness
        val label = if (detectDarkness) "ON" else "OFF"
        binding.detectDarkness.text = getString(R.string.detect_darkness, label)
    }

    private fun onSOSMode() {
        if (sosMode) {
            sosModeJob?.cancel()
            flashlight?.toggle(false)
            binding.sosMode.text = getString(R.string.sos_mode,"OFF")
        } else {
            sosModeJob = startSOSMode()
            binding.sosMode.text = getString(R.string.sos_mode, "ON")
        }
        sosMode = !sosMode
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val imageCapture = ImageCapture.Builder().setFlashMode(ImageCapture.FLASH_MODE_ON).build()
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraInstance = cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)
            flashlight = Flashlight(cameraInstance)
            print("Camera instantiated")
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                print("Permissions NOT granted")
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onSensorChanged(event: SensorEvent?) {
        flashlight?.toggleBasedOnLuminosity(event, detectDarkness)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    private fun print(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun startSOSMode() = GlobalScope.launch {
        while (true) {
            delay(500)
            flashlight?.toggle()
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}