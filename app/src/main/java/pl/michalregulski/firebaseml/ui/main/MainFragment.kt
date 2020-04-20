package pl.michalregulski.firebaseml.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import pl.michalregulski.firebaseml.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private val sharedViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = MainFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            sharedViewModel = this@MainFragment.sharedViewModel
        }

        return binding.root
    }

}
