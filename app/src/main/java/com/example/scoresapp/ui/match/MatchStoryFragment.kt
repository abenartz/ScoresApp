package com.example.scoresapp.ui.match

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.navigation.fragment.findNavController
import com.example.scoresapp.R
import com.example.scoresapp.constants.Constants.APP_DEBUG
import com.example.scoresapp.databinding.FragmentMatchStoryBinding
import com.example.scoresapp.viewmodels.MainViewModel
import jp.shts.android.storiesprogressview.StoriesProgressView
import timber.log.Timber

class MatchStoryFragment: Fragment(R.layout.fragment_match_story) {

    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L

    private var pressTime = 0L
    private var pressLimit = 500L
    // TODO: maybe can remove if prefetch
    private var isFirstTime = true

    private val viewModel by activityViewModels<MainViewModel>()
    private var player: ExoPlayer? = null

    private var _binding: FragmentMatchStoryBinding? = null
    private val binding get() = _binding!!



    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis();
                player?.pause()
                binding.progressStories.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                player?.play()
                binding.progressStories.resume()
                return@OnTouchListener pressLimit < now - pressTime
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
            setStoriesCount(viewModel.storyPages.size)
            val durations = viewModel.storyPages.map { it.duration?.toLong() ?: 0L }.toLongArray()
            setStoriesCountWithDurations(durations)
        }
    }

    private fun initializePlayer() {
        context?.let { context ->
            Timber.tag(APP_DEBUG).d("MatchStoryFragment: initMediaPlayer: num of stories = ${viewModel.storyPages.size}")
            player = ExoPlayer.Builder(context).build().apply {
                binding.playerView.player = this
                binding.playerView.useController = false
                val mediaItems = viewModel.storyPages.map { MediaItem.fromUri(it.videoUrl ?: "") }
                setMediaItems(mediaItems, mediaItemIndex, playbackPosition)
                playWhenReady = true
                setMenuVisibility(false)
                addListener(playbackStateListener)
                prepare()
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
                    if (isFirstTime)  {
                        isFirstTime = false
                        binding.progressStories.startStories()
                    }
                }
            }
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            mediaItemIndex = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
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

}