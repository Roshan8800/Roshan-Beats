package com.roshan.music

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NowPlayingScreen(
    nowPlayingViewModel: NowPlayingViewModel,
    content: @Composable () -> Unit
) {
    val uiState by nowPlayingViewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        nowPlayingViewModel.init(context)
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            FullScreenPlayer(
                uiState = uiState,
                onPlayPause = { if (uiState.isPlaying) nowPlayingViewModel.pause() else nowPlayingViewModel.play() },
                onSkipNext = { nowPlayingViewModel.skipToNext() },
                onSkipPrevious = { nowPlayingViewModel.skipToPrevious() }
            )
        },
        sheetPeekHeight = 60.dp,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                    MiniPlayer(
                        uiState = uiState,
                        onPlayPause = { if (uiState.isPlaying) nowPlayingViewModel.pause() else nowPlayingViewModel.play() }
                    )
                }
            }
        }
    )
}

@Composable
fun MiniPlayer(uiState: NowPlayingUiState, onPlayPause: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = uiState.currentTrack?.mediaMetadata?.artworkUri,
            contentDescription = "Album Art",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = uiState.currentTrack?.mediaMetadata?.title?.toString() ?: "Unknown Title")
            Text(text = uiState.currentTrack?.mediaMetadata?.artist?.toString() ?: "Unknown Artist")
        }
        IconButton(onClick = onPlayPause) {
            Icon(
                imageVector = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause"
            )
        }
    }
}

@Composable
fun FullScreenPlayer(
    uiState: NowPlayingUiState,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = uiState.currentTrack?.mediaMetadata?.artworkUri,
            contentDescription = "Album Art",
            modifier = Modifier.size(256.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = uiState.currentTrack?.mediaMetadata?.title?.toString() ?: "Unknown Title", style = MaterialTheme.typography.headlineMedium)
        Text(text = uiState.currentTrack?.mediaMetadata?.artist?.toString() ?: "Unknown Artist", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            IconButton(onClick = onSkipPrevious) {
                Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Previous")
            }
            IconButton(onClick = onPlayPause) {
                Icon(imageVector = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = "Play/Pause")
            }
            IconButton(onClick = onSkipNext) {
                Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Next")
            }
        }
    }
}
