package pl.michalregulski.firebaseml

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.michalregulski.firebaseml.databinding.MainActivityBinding
import pl.michalregulski.firebaseml.ui.main.MainActivityViewModel
import pl.michalregulski.firebaseml.ui.main.MainFragment
import kotlin.coroutines.resumeWithException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupKoinFragmentFactory()

        binding = MainActivityBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
        }

        setTheme(R.style.AppTheme)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupFloatingActionButton(binding.fab)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, MainFragment::class.java, null, null)
                .commitNow()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PHOTO_REQUEST_CODE -> {
                    val image = getImageFromData(data)
                    if (image != null) {
                        viewModel.bitmap.set(BitmapDrawable(resources, image))
                        tagImage(image)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                startPickingImageActivity()
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupFloatingActionButton(fab: FloatingActionButton) {
        fab.setOnClickListener {
            startPickingImageActivity()
        }
    }

    private fun getImageFromData(data: Intent?): Bitmap? {
        val selectedImage = data?.data
        return MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
    }

    private fun startPickingImageActivity() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_CODE
            )
        } else {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }.also {
                startActivityForResult(it, PHOTO_REQUEST_CODE)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun tagImage(bitmap: Bitmap) {
        val visionImg = FirebaseVisionImage.fromBitmap(bitmap)
        lifecycleScope.launchWhenStarted {
            try {
                val labels: List<FirebaseVisionImageLabel> =
                    FirebaseVision.getInstance().onDeviceImageLabeler.processImageAsync(visionImg)
                viewModel.labels.set(labels.joinToString { it.text })
            } catch (ex: Exception) {
                Log.wtf(MainActivity::class.simpleName, ex.message, ex)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun FirebaseVisionImageLabeler.processImageAsync(image: FirebaseVisionImage) =
        suspendCancellableCoroutine<MutableList<FirebaseVisionImageLabel>> { continuation ->
            processImage(image).addOnSuccessListener {
                continuation.resume(it) {}
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }

    companion object {
        const val PHOTO_REQUEST_CODE = 13153
        const val READ_EXTERNAL_STORAGE_CODE = 1
    }

}
