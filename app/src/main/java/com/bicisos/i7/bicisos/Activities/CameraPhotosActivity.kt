package com.bicisos.i7.bicisos.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_camera_photos.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


typealias LumaListener = (luma: Double) -> Unit

class CameraPhotosActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var flash: Boolean = false
    private var preview: Preview? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_photos)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listener for take photo button
        camera_capture_button.setOnClickListener { takePhoto() }

        camera_flash_button.setOnClickListener { turnOnOffFlash() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun turnOnOffFlash(){
        if(camera!!.cameraInfo.hasFlashUnit()){
            if(flash) {
                camera_flash_button.setImageResource(R.mipmap.flashon)
                camera!!.cameraControl.enableTorch(false)
            } else {
                camera_flash_button.setImageResource(R.mipmap.flashoff)
                camera!!.cameraControl.enableTorch(true)
            }
            flash = !flash
        }else{
            Toast.makeText(this, "Su dispositivo no cuenta con flash", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        progressCam.visibility = View.VISIBLE
        camera_capture_button.visibility = View.INVISIBLE

        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    progressCam.visibility = View.INVISIBLE
                    camera_capture_button.visibility = View.VISIBLE

                    Toast.makeText(
                        applicationContext,
                        "Tuvimos un problema al capturar la foto. Intente más tarde.",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    finish()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                    try{
                        val savedUri = Uri.fromFile(photoFile)
                        val options = BitmapFactory.Options()
                        options.inSampleSize = 4
                        val bitmapReduce = BitmapFactory.decodeFile(savedUri.path, options);

                        val tempFileReturn = File(
                            outputDirectory,
                            SimpleDateFormat(
                                FILENAME_FORMAT, Locale.US
                            ).format(System.currentTimeMillis()) + ".jpg"
                        )

                        FileOutputStream(tempFileReturn).use { out ->
                            bitmapReduce.compress(
                                Bitmap.CompressFormat.JPEG,
                                90,
                                out
                            ) // bmp is your Bitmap instance
                        }

                        val msg = "Photo capture succeeded: ${tempFileReturn.path}"
                        Log.d(TAG, msg)

                        val returnIntent = Intent()
                        returnIntent.putExtra("result", tempFileReturn.path)
                        setResult(Activity.RESULT_OK, returnIntent)

                        if (camera!!.cameraInfo.hasFlashUnit()) {
                            if (flash) {
                                camera_flash_button.setImageResource(R.mipmap.flashon)
                                camera!!.cameraControl.enableTorch(false)
                            }
                            flash = false
                        }

                        finish()

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            applicationContext,
                            "Tuvimos un problema al capturar la foto. Intente más tarde.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    //it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                    viewFinder.width.toFloat(), viewFinder.height.toFloat()
                )
                val centerWidth = viewFinder.width.toFloat() / 2
                val centerHeight = viewFinder.height.toFloat() / 2

                val autoFocusPoint = factory.createPoint(centerWidth, centerHeight)

                camera!!.cameraControl.startFocusAndMetering(
                    FocusMeteringAction.Builder(
                        autoFocusPoint,
                        FocusMeteringAction.FLAG_AF
                    ).apply {
                        setAutoCancelDuration(1, TimeUnit.SECONDS)
                    }.build()
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mediaDir = externalMediaDirs.firstOrNull()?.let {
                File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir
            else filesDir
        } else {
            return filesDir
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Conceda permisos para acceder a la cámara",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

//*********** size reduce ******************

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}