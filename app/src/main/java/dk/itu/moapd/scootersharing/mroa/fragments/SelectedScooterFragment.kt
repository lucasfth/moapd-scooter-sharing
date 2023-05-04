package dk.itu.moapd.scootersharing.mroa.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.common.Barcode.BarcodeFormat
import com.google.mlkit.vision.common.InputImage
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentSelectedScooterBinding
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 * Use the [SelectedScooterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedScooterFragment : Fragment() {

    /**
     * _binding
     */
    private var _binding: FragmentSelectedScooterBinding? = null

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    private val selectedScooter = MainFragment.selectedScooter

    private lateinit var scannedQr: String

    private lateinit var scanningOptions: BarcodeScannerOptions

    private lateinit var scanner: BarcodeScanner

    private lateinit var analysisUseCase: ImageAnalysis


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedScooterBinding.inflate(inflater, container, false)

        MainActivity.storage.child("scooters").child("${selectedScooter.name}.png").downloadUrl.addOnSuccessListener {
            Glide.with(binding.scooterImage.context)
                .load(it)
                .centerCrop()
                .into(binding.scooterImage)
        }

        scanningOptions = BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        scanner = BarcodeScanning.getClient(scanningOptions)
        analysisUseCase = ImageAnalysis.Builder().build()

        analysisUseCase.setAnalyzer(
            // newSingleThreadExecutor() will let us perform analysis on a single worker thread
            Executors.newSingleThreadExecutor(),
            { imageProxy ->
                processImageProxy(scanner, imageProxy)
            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindCameraUseCases()


        with(binding) {
            scooterInfoPanel.text = selectedScooter.name

            clickScanQr.setOnClickListener {

            }

            clickStart.setOnClickListener {
                if ("https://www.youtube.com/watch?v=dQw4w9WgXcQ" == scannedQr) {
                    Snackbar.make(root, "It motherfucking worked", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(root, "Unlock scooter before starting the ride", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindCameraUseCases() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()


            // setting up the preview use case
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                }

            // configure to use the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    previewUseCase,
                    analysisUseCase
                )
            } catch (illegalStateException: IllegalStateException) {
                // If the use case has already been bound to another lifecycle or method is not called on main thread.
                Log.e(TAG, illegalStateException.message.orEmpty())
            } catch (illegalArgumentException: IllegalArgumentException) {
                // If the provided camera selector is unable to resolve a camera to be used for the given use cases.
                Log.e(TAG, illegalArgumentException.message.orEmpty())
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {

        imageProxy.image?.let { image ->
            val inputImage =
                InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)
                    // `rawValue` is the decoded value of the barcode
                    barcode?.rawValue?.let { value ->
                        // update our textView to show the decoded value
                        println("We got this: $value")
                        scannedQr = value
                        Snackbar.make(binding.root, "We got this $value", Snackbar.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    // This failure will happen if the barcode scanning model
                    // fails to download from Google Play Services
                    Log.e(TAG, it.message.orEmpty())
                }.addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must
                    // call image.close() on received images when finished
                    // using them. Otherwise, new images may not be received
                    // or the camera may stall.
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }
}