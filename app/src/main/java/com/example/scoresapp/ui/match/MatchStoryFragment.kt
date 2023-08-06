package com.example.scoresapp.ui.match

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.media3.common.IllegalSeekPositionException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.example.scoresapp.R
import com.example.scoresapp.constants.Constants.APP_DEBUG
import com.example.scoresapp.databinding.FragmentMatchStoryBinding
import com.example.scoresapp.extensions.toDurationArray
import com.example.scoresapp.extensions.toMediaItems
import com.example.scoresapp.ui.MainActivity
import com.example.scoresapp.viewmodels.MatchViewModel
import com.example.scoresapp.viewmodels.StoryViewModel
import timber.log.Timber


class MatchStoryFragment: Fragment(R.layout.fragment_match_story) {

    private val activityViewModel by activityViewModels<MatchViewModel>()
    private val viewModel by viewModels<StoryViewModel>()
    private var player: ExoPlayer? = null

    private var _binding: FragmentMatchStoryBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewModel.pressTime = System.currentTimeMillis();
                player?.pause()
                binding.progressStories.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                player?.play()
                binding.progressStories.resume()
                return@OnTouchListener PRESS_LIMIT < now - viewModel.pressTime
            }
            else -> false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? MainActivity)?.setWindowNavigationAndStatusColor(Color.BLACK)
        setClickListeners()
        setupStoriesProgressView()
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Build.VERSION.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    private fun setClickListeners() {
        with(binding) {
            backwardView.setOnClickListener {
                progressStories.reverse()
                player?.playPreviousVideo()
            }
            forwardView.setOnClickListener {
                progressStories.skip()
                player?.playNextVideo()
            }
            backwardView.setOnTouchListener(onTouchListener)
            forwardView.setOnTouchListener(onTouchListener)
        }
    }

    private fun setupStoriesProgressView() {
        with(binding.progressStories) {
            setStoriesCount(activityViewModel.currStoryPages.size)
            val durations = activityViewModel.currStoryPages.toDurationArray()
            setStoriesCountWithDurations(durations)
            startStories(viewModel.mediaItemIndex)
            pause()
        }
    }

    private fun initializePlayer() {
        context?.let { context ->
            Timber.tag(APP_DEBUG).d("MatchStoryFragment: initMediaPlayer: num of stories = ${activityViewModel.currStoryPages.size}")
            player = ExoPlayer.Builder(context).build().apply {
                try {
                    binding.playerView.player = this
                    binding.playerView.useController = false
                    val mediaItems = activityViewModel.currStoryPages.toMediaItems()
                    setMediaItems(mediaItems, viewModel.mediaItemIndex, viewModel.playbackPosition)
                    playWhenReady = true
                    setMenuVisibility(false)
                    addListener(playbackStateListener)
                    prepare()
                } catch (e: IllegalSeekPositionException) {
                    Timber.tag(APP_DEBUG).e("MatchStoryFragment: initMediaPlayer: $e")
                    findNavController().popBackStack()
                }
            }
        }
    }

    private val playbackStateListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    findNavController().popBackStack()
                }
                Player.STATE_READY -> {
                    binding.progressStories.resume()
                }
            }
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            with(viewModel) {
                playbackPosition = exoPlayer.currentPosition
                mediaItemIndex = exoPlayer.currentMediaItemIndex
                playWhenReady = exoPlayer.playWhenReady
            }
            exoPlayer.removeListener(playbackStateListener)
            exoPlayer.release()
        }
        player = null
    }

    private fun ExoPlayer.playNextVideo() {
        if (hasNextMediaItem()) {
            seekToNext()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun ExoPlayer.playPreviousVideo() {
        if (hasPreviousMediaItem()) {
            seekToPreviousMediaItem()
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
        binding.progressStories.pause()
    }


    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }
    override fun onDestroy() {
        binding.progressStories.destroy()
        _binding = null
        super.onDestroy()

    }
    
    companion object {
        private const val PRESS_LIMIT = 500L

    }

}