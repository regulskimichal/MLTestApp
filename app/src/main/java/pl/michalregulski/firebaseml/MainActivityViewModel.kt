package pl.michalregulski.firebaseml

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import pl.michalregulski.firebaseml.utils.Observable
import pl.michalregulski.firebaseml.utils.ObservableBoolean
import kotlin.coroutines.resume

class MainActivityViewModel(
    application: Application,
    private val imageRecognitionService: ImageRecognitionService
) : AndroidViewModel(application) {

    val bitmapUri = Observable<Uri?>(null)
    val bitmapDrawable = Observable<BitmapDrawable?>(null)
    val labels = Observable("")
    val isFullScreen = ObservableBoolean()

    init {
        bitmapUri.addOnPropertyChangedListener { uri, _ ->
            processImage(uri)
        }
    }

    private fun processImage(uri: Uri?) {
        viewModelScope.launch {
            val bitmap = loadImage(getApplication(), uri)
            bitmapDrawable.set(bitmap?.toDrawable(getApplication<Application>().resources))
            labels.set(imageRecognitionService.tagImage(bitmap).joinToString("\n") { it.text })
        }
    }

    private suspend fun loadImage(context: Context, imageUrl: Uri?): Bitmap? =
        suspendCancellableCoroutine { continuation ->
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        continuation.resume(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) = Unit
                })
        }
}
