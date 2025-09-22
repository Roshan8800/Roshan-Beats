package com.roshan.music

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

data class OnboardingPage(
    val title: String,
    val description: String
)

private val onboardingPages = listOf(
    OnboardingPage(
        title = "Discover New Music",
        description = "Find new tracks and artists you'll love."
    ),
    OnboardingPage(
        title = "Create Your Playlists",
        description = "Curate your own playlists for any mood or occasion."
    ),
    OnboardingPage(
        title = "Listen Anytime, Anywhere",
        description = "Enjoy your music offline, on any of your devices."
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            count = onboardingPages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = onboardingPages[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        Button(
            onClick = {
                if (pagerState.currentPage < onboardingPages.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    navController.popBackStack()
                    navController.navigate("main")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = if (pagerState.currentPage < onboardingPages.size - 1) "Next" else "Finish")
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(text = page.title, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = page.description, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
    }
}
