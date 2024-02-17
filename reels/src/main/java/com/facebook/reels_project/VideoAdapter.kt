package com.facebook.reels_project

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.facebook.reels_project.databinding.ListVideoBinding
import com.facebook.reels_project.databinding.MyCustomControllerLayoutBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class VideoAdapter(
    var context: Context,
    var videos: ArrayList<Video>,
    var videoPreparedListener: OnVideoPreparedListener
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(
        val binding: ListVideoBinding,
        var context: Context,
        var videoPreparedListener: OnVideoPreparedListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var mediaSource: MediaSource
        private lateinit var progressBar: SeekBar


        fun setVideoPath(url: String) {

            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        binding.pbLoading.visibility = View.VISIBLE
                    } else if (playbackState == Player.STATE_READY) {
                        binding.pbLoading.visibility = View.GONE
                        updateSeekBar()
                    }
                }
            })

            binding.playerView.player = exoPlayer

            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            val dataSourceFactory = DefaultDataSource.Factory(context)

            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            if (absoluteAdapterPosition == 0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }

            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
        }

        private fun updateSeekBar() {
            val customControllerBinding: MyCustomControllerLayoutBinding? =
                MyCustomControllerLayoutBinding.bind(binding.root)

            // Check if binding was successful
            if (customControllerBinding != null) {
                progressBar = customControllerBinding.progressBar

                // Set up the SeekBar max value based on video duration
                val duration = exoPlayer.duration
                progressBar.max = duration.toInt()

                // Create a handler to update the SeekBar periodically
                val handler = android.os.Handler()
                handler.post(object : Runnable {
                    override fun run() {
                        // Update SeekBar progress based on current position
                        val currentPosition = exoPlayer.currentPosition
                        progressBar.progress = currentPosition.toInt()

                        // Schedule the next update after a short delay (e.g., 100 milliseconds)
                        handler.postDelayed(this, 100)
                    }
                })

                // Set up a listener for SeekBar changes
                progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        // Update video playback position when SeekBar is changed by the user
                        if (fromUser) {
                            exoPlayer.seekTo(progress.toLong())
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        // Not needed for your use case, but can be used for handling touch events
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        // Not needed for your use case, but can be used for handling touch events
                    }
                })
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = ListVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(view, context, videoPreparedListener)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val model = videos[position]

        holder.binding.tvTitle.text = model.title
        holder.setVideoPath(model.url)
        holder.binding.dotsbutton.setOnClickListener {
            val optionsBottomSheetFragment = OptionsBottomSheetFragment()
            optionsBottomSheetFragment.setOptionsClickListener(object : OptionsBottomSheetFragment.OptionsClickListener {
                override fun onCaptionClicked() {
                    // Handle caption option click
                    // You can implement the desired behavior here
                }

                override fun onDescriptionClicked() {
                    // Handle description option click
                    // You can implement the desired behavior here
                }

                override fun onReportClicked() {
                    // Handle report option click
                    // You can implement the desired behavior here
                }
            })

            // Ensure that the context is an instance of FragmentActivity
            if (context is FragmentActivity) {
                optionsBottomSheetFragment.show((context as FragmentActivity).supportFragmentManager, optionsBottomSheetFragment.tag)
            }
        }

    }
    override fun getItemCount(): Int {
        return videos.size
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }
}