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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NowPlayingScreen(
    nowPlayingViewModel: NowPlayingViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val uiState by nowPlayingViewModel.uiState.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            FullScreenPlayer(uiState = uiState)
        },
        sheetPeekHeight = 60.dp,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                    MiniPlayer(uiState = uiState)
                }
            }
        }
    )
}

@Composable
fun MiniPlayer(uiState: NowPlayingUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "${uiState.currentSong?.title} - ${uiState.currentSong?.artist}")
    }
}

@Composable
fun FullScreenPlayer(uiState: NowPlayingUiState) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Full Screen Player")
            Text(text = "${uiState.currentSong?.title}")
            Text(text = "${uiState.currentSong?.artist}")
        }
    }
}
