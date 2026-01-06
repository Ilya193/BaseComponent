package ru.ikom.basecomponent.example

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.ikom.basecomponent.R

class MainFragment(
    private val onBack: () -> Unit
) : Fragment(R.layout.main_content) {

    private val component: MainComponent by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener { onBack() }

        val loading = view.findViewById<ProgressBar>(R.id.loading)

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                component.state.collect {
                    loading.isVisible = it.isLoading
                }
            }

            launch {
                component.labels.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED).collect {
                    when (it) {
                        is MainComponent.Label.ShowSnackbar -> showSnackbar(it)
                    }
                }
            }
        }
    }

    private fun showSnackbar(label: MainComponent.Label.ShowSnackbar) {
        val rootView = view ?: return
        Snackbar.make(rootView, label.message, Snackbar.LENGTH_SHORT).show()
    }
}