package pl.michalregulski.firebaseml

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ImageRecognitionService(private val imageLabeler: FirebaseVisionImageLabeler) {
    suspend fun tagImage(bitmap: Bitmap?): List<FirebaseVisionImageLabel> {
        if (bitmap != null) {
            try {
                val visionImg = FirebaseVisionImage.fromBitmap(bitmap)
                return imageLabeler.processImageAsync(visionImg)
            } catch (ex: Exception) {
                Log.wtf(MainActivity::class.simpleName, ex.message, ex)
            }
        }

        return emptyList()
    }

    private suspend fun FirebaseVisionImageLabeler.processImageAsync(image: FirebaseVisionImage) =
        suspendCancellableCoroutine<MutableList<FirebaseVisionImageLabel>> { continuation ->
            processImage(image).addOnSuccessListener {
                continuation.resume(it)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
}
