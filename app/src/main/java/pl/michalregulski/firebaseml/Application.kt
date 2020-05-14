package pl.michalregulski.firebaseml

import android.app.Application
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.google.firebase.ml.vision.FirebaseVision
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {

    private val module = module {
        single { FirebaseVision.getInstance() }
        single { get<FirebaseVision>().onDeviceImageLabeler }
        single { ImageRecognitionService(get()) }
        viewModel { MainActivityViewModel(androidApplication(), get()) }
        fragment { MainFragment() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            fragmentFactory()
            modules(module)
        }
    }

}

@GlideModule
class GlideModule : AppGlideModule()
