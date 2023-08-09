package com.example.scoresapp.ui.match

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.scoresapp.R
import com.example.scoresapp.adapters.MatchAdapter
import com.example.scoresapp.constants.Constants.APP_DEBUG
import com.example.scoresapp.databinding.FragmentMatchListBinding
import com.example.scoresapp.extensions.displaySnackBar
import com.example.scoresapp.extensions.showView
import com.example.scoresapp.ui.MainActivity
import com.example.scoresapp.ui.UiState
import com.example.scoresapp.utils.TopSpacingItemDecoration
import com.example.scoresapp.viewmodels.MatchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MatchListFragment: Fragment(R.layout.fragment_match_list) {

    private val viewModel by activityViewModels<MatchViewModel>()
    private var matchAdapter: MatchAdapter? = null

    // Google recommendation avoiding memory leaks
    private var _binding: FragmentMatchListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setNavigationAndStatusBarColor()
        initRecyclerView()
        subscribeObservers()
    }

    private fun setNavigationAndStatusBarColor() {
        val nightModeFlags = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            (activity as? MainActivity)?.setWindowNavigationAndStatusColor(
                ResourcesCompat.getColor(resources, R.color.dynamic_blue, null)
            )
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle).collect { state ->
                binding.progressBar.showView(state is UiState.Loading)
                when (state) {
                    is UiState.Success -> {
                        Timber.tag(APP_DEBUG).d("MatchListFragment: subscribeObservers: matches size = ${state.data.size}")
                        matchAdapter?.submitList(state.data)
                    }
                    is UiState.Error -> {
                        state.errMsg?.let { binding.root.displaySnackBar(it) }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initRecyclerView() {
        matchAdapter = MatchAdapter(
            onItemClicked = { match ->
                Timber.tag(APP_DEBUG).d("MatchListFragment: onItemClicked: selectedMatch = $match")
                if (match.isValidData()) {
                    viewModel.currStoryPages = match.wscGame?.primeStory?.pages ?: emptyList()
                    findNavController().navigate(R.id.action_matchListFragment_to_matchStoryFragment)
                } else {
                    binding.root.displaySnackBar("No story for this game")
                }
            }
        )
        with(binding.matchRecyclerView) {
            val spacingItemDecoration = TopSpacingItemDecoration(
                requireContext().resources.getDimension(R.dimen.recycler_match_cards_margin).toInt()
            )
            addItemDecoration(spacingItemDecoration)
            adapter = matchAdapter

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        matchAdapter = null
        _binding = null
    }
}