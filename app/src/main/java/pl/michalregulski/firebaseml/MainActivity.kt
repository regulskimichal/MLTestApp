package pl.michalregulski.firebaseml

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.michalregulski.firebaseml.databinding.MainActivityBinding
import pl.michalregulski.firebaseml.utils.applyNavigationMarginInsets
import pl.michalregulski.firebaseml.utils.negate

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()
    lateinit var binding: MainActivityBinding

    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
        }

        gestureDetector =
            GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    viewModel.isFullScreen.negate()
                    return true
                }
            })

        setTheme(R.style.AppTheme)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupFloatingActionButton(binding.fab)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MainFragment::class.java, null, null)
                .commitNow()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PHOTO_REQUEST_CODE -> {
                    lifecycleScope.launch {
                        viewModel.bitmapUri.set(data?.data)
                        viewModel.isFullScreen.apply {
                            set(true)
                            notifyChange()
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
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
        fab.applyNavigationMarginInsets()
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

    companion object {
        const val PHOTO_REQUEST_CODE = 13153
        const val READ_EXTERNAL_STORAGE_CODE = 1
    }

}
