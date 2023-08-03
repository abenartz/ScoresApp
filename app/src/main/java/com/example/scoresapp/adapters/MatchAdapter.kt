package com.example.scoresapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scoresapp.api.responses.MatchData
import com.example.scoresapp.databinding.MatchListItemBinding
import com.example.scoresapp.extensions.load

class MatchAdapter(
    private val onItemClicked: ((MatchData) -> Unit)
): ListAdapter<MatchData, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MatchViewHolder(
            MatchListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val match = getItem(position)
        (holder as MatchViewHolder).bind(match)
    }

    inner class MatchViewHolder(
        private val binding: MatchListItemBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        private lateinit var selectedMatch: MatchData

        init {
            binding.root.setOnClickListener {
                onItemClicked.invoke(selectedMatch)
            }
        }

        fun bind(item: MatchData) {
            selectedMatch = item
            binding.apply {
                item.wscGame?.primeStory?.pages?.lastOrNull()?.let { page ->
                    textScoreHome.text = page.homeScore.toString()
                    textScoreAway.text = page.awayScore.toString()
                }
                item.teams?.home.let {
                    imageTeamHome.load(it?.logo)
                    textTeamNameHome.text = it?.name
                }
                item.teams?.away.let {
                    imageTeamAway.load(it?.logo)
                    textTeamNameAway.text = it?.name
                }
            }
        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<MatchData>() {

    override fun areItemsTheSame(oldItem: MatchData, newItem: MatchData): Boolean {
        return oldItem.WSCGameId == newItem.WSCGameId
    }

    override fun areContentsTheSame(oldItem: MatchData, newItem: MatchData): Boolean {
        return oldItem == newItem
    }
}