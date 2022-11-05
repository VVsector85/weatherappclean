package com.learning.weatherappclean.presentation.ui.components

import android.net.Uri
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.launch

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
    }
    val playerView = remember {
        PlayerView(context)
    }
    AndroidView(factory = { playerView })

    LaunchedEffect(uri) {
        launch{
            Log.d("my_tag", "Launched effect")
            exoPlayer.apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
                setMediaSource(source)
                prepare()
                playWhenReady = true
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                repeatMode = Player.REPEAT_MODE_ONE
            }

            playerView.apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        }
    }

    DisposableEffect(exoPlayer) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.play()
                    Log.d("my_tag", "RESUME")
                }
                Lifecycle.Event.ON_STOP -> {
                    exoPlayer.pause()
                    Log.d("my_tag", "STOP")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.release()
                    Log.d("my_tag", "DESTROY")
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.release()
            Log.d("my_tag", "Dispose")
        }
    }
}
