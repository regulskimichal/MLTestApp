package pl.michalregulski.firebaseml.ui.main

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import pl.michalregulski.firebaseml.ObservableField
import pl.michalregulski.firebaseml.ObservableString

class MainActivityViewModel : ViewModel() {

    val bitmap  = ObservableField<Drawable?>()
    val labels = ObservableString()

}
