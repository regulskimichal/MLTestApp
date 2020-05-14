package pl.michalregulski.firebaseml

import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.michalregulski.firebaseml.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private val viewModel: MainActivityViewModel by sharedViewModel()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            sharedViewModel = this@MainFragment.viewModel
        }

        initViewModel()

        return binding.root
    }

    private fun initViewModel() {
        viewModel.isFullScreen.addOnPropertyChangedListener { value, _ ->
            val mainActivity = activity as MainActivity?
            if (value) {
                fullScreenMode(mainActivity, mainActivity?.binding?.fab, binding.mainFragmentCL)
            } else {
                windowedMode(mainActivity, mainActivity?.binding?.fab, binding.mainFragmentCL)
            }
        }

        viewModel.isFullScreen.apply {
            set(false)
            notifyChange()
        }
    }

    private fun fullScreenMode(
        appCompatActivity: AppCompatActivity?,
        fab: FloatingActionButton?,
        layout: ViewGroup
    ) {
        appCompatActivity?.apply {
            supportActionBar?.hide()
            window.decorView.apply {
                systemUiVisibility = defaultSystemUIVisibility or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
        fab?.hide()
        layout.setBackgroundColor(BLACK)
    }

    private fun windowedMode(
        appCompatActivity: AppCompatActivity?,
        fab: FloatingActionButton?,
        layout: ViewGroup
    ) {
        appCompatActivity?.apply {
            supportActionBar?.show()
            window.decorView.apply {
                systemUiVisibility =
                    defaultSystemUIVisibility
            }
        }
        fab?.show()
        layout.setBackgroundColor(WHITE)
    }

    companion object {
        private const val defaultSystemUIVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

}
