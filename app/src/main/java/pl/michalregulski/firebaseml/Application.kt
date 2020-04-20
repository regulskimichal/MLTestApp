package pl.michalregulski.firebaseml

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.michalregulski.firebaseml.ui.main.MainFragment
import pl.michalregulski.firebaseml.ui.main.MainActivityViewModel

class Application : Application() {

    private val module = module {
        viewModel { MainActivityViewModel() }
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
