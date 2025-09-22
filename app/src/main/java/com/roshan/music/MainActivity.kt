package com.roshan.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Card
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roshan.music.ui.theme.RoshanBeatsTheme
import com.roshan.music.ui.theme.Purple500
import com.roshan.music.ui.theme.Teal200
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoshanBeatsTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("onboarding") {
            OnboardingScreen(navController = navController)
        }
        composable("main") {
            MainScreen()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        navController.popBackStack()
        navController.navigate("onboarding")
    }

    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(Brush.verticalGradient(colors = listOf(Purple500, Teal200)))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Roshan Beats", color = Color.White, modifier = Modifier.alpha(alpha = alpha))
    }
}

@Composable
fun MainScreen() {
    NowPlayingScreen {
        val navController = rememberNavController()
        androidx.compose.material3.Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainScreenNavigation(navController = navController)
            }
        }
    }
}

@Composable
fun MainScreenNavigation(navController: androidx.navigation.NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Search.route) {
            SearchScreen()
        }
        composable(Screen.Library.route) {
            LibraryScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Home Screen")
    }
}

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = viewModel()) {
    val uiState by searchViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        androidx.compose.material3.TextField(
            value = uiState.searchQuery,
            onValueChange = { searchViewModel.onSearchQueryChanged(it) },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        // Trending Searches
        Text(text = "Trending Searches")
        LazyColumn {
            items(uiState.trendingSearches) { trend ->
                Text(text = trend)
            }
        }

        // Recent Searches
        Text(text = "Recent Searches")
        LazyColumn {
            items(uiState.recentSearches) { recent ->
                Text(text = recent)
            }
        }
    }
}

@Composable
fun LibraryScreen(libraryViewModel: LibraryViewModel = viewModel()) {
    val uiState by libraryViewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Playlists", "Albums", "Artists", "Downloads")

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> PlaylistsTab(playlists = uiState.playlists)
            1 -> AlbumsTab(albums = uiState.albums)
            2 -> ArtistsTab(artists = uiState.artists)
            3 -> DownloadsTab(downloads = uiState.downloads)
        }
    }
}

@Composable
fun PlaylistsTab(playlists: List<Playlist>) {
    LazyColumn {
        items(playlists) { playlist ->
            Text(text = "${playlist.name} (${playlist.trackCount} songs)")
        }
    }
}

@Composable
fun AlbumsTab(albums: List<Album>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Albums")
    }
}

@Composable
fun ArtistsTab(artists: List<Artist>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Artists")
    }
}

@Composable
fun DownloadsTab(downloads: List<Song>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Downloads")
    }
}

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()
    val settingsItems = mapOf(
        "Account & Subscription" to Icons.Default.AccountBox,
        "Playback Preferences" to Icons.Default.PlayArrow,
        "Notifications" to Icons.Default.Notifications,
        "Privacy & Security" to Icons.Default.PrivacyTip,
        "Theme (Dark/Light)" to Icons.Default.Style
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Card
        uiState.user?.let { user ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Avatar",
                        modifier = Modifier.size(128.dp)
                    )
                    Text(text = user.username, fontSize = 24.sp)
                    Text(text = user.subscriptionTier, fontSize = 16.sp, color = Color.Gray)
                }
            }
        }

        // Settings
        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(settingsItems.toList()) { (title, icon) ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Icon(imageVector = icon, contentDescription = title)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = title, fontSize = 18.sp)
                }
            }
        }
    }
}
