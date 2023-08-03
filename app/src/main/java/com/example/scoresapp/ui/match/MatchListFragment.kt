package com.example.scoresapp.ui.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.scoresapp.Constants.APP_DEBUG
import com.example.scoresapp.R
import com.example.scoresapp.adapters.MatchAdapter
import com.example.scoresapp.databinding.FragmentMatchListBinding
import com.example.scoresapp.utils.TopSpacingItemDecoration
import com.example.scoresapp.viewmodels.MatchListViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class MatchListFragment: Fragment(R.layout.fragment_match_list) {

    private val viewModel by activityViewModels<MatchListViewModel>()
    private var matchAdapter: MatchAdapter? = null

    // Google recommendation avoiding memory leaks
    private var _binding: FragmentMatchListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(APP_DEBUG).d("MatchListFragment: onCreate:")
        context?.let {
            viewModel.readDataFromJson(it)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.matches.collect {
                    Timber.tag(APP_DEBUG).d("MatchListFragment: subscribeObservers: matches size = ${it.size}")
                    matchAdapter?.submitList(it)
                }
            }
        }
    }

    private fun initRecyclerView() {
        matchAdapter = MatchAdapter(
            onItemClicked = { match ->
                Timber.tag(APP_DEBUG).d("MatchListFragment: onItemClicked: selectedMatch = $match")
//                findNavController().navigate()
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