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
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Application
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.roshan.music.di.ViewModelFactory
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
fun MainScreen(nowPlayingViewModel: NowPlayingViewModel = viewModel()) {
    NowPlayingScreen(nowPlayingViewModel = nowPlayingViewModel) {
        val navController = rememberNavController()
        androidx.compose.material3.Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainScreenNavigation(
                    navController = navController,
                    nowPlayingViewModel = nowPlayingViewModel
                )
            }
        }
    }
}

@Composable
fun MainScreenNavigation(
    navController: androidx.navigation.NavHostController,
    nowPlayingViewModel: NowPlayingViewModel
) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(nowPlayingViewModel = nowPlayingViewModel)
        }
        composable(Screen.Search.route) {
            SearchScreen(nowPlayingViewModel = nowPlayingViewModel)
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
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current.applicationContext as Application)),
    nowPlayingViewModel: NowPlayingViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.showAiDialog) {
        AiPromptDialog(
            onDismiss = { homeViewModel.showAiDialog(false) },
            onGenerate = { prompt ->
                homeViewModel.getAiRecommendations(prompt)
                homeViewModel.showAiDialog(false)
            }
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // AI Recommendations
        item {
            Button(onClick = { homeViewModel.showAiDialog(true) }) {
                Text("Get AI Recommendations")
            }
            if (uiState.aiRecommendations.isNotEmpty()) {
                Text("AI Recommendations", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(uiState.aiRecommendations) { track ->
                        Card(modifier = Modifier.padding(8.dp).clickable {
                            nowPlayingViewModel.playTrack(track)
                        }) {
                            Column {
                                // AsyncImage(model = track.image, contentDescription = track.name)
                                Text(text = track.name)
                                Text(text = track.artist_name)
                            }
                        }
                    }
                }
            }
        }

        // Trending Albums
        item {
            Text("Trending Now", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
            LazyRow {
                items(uiState.trendingAlbums) { album ->
                    Card(modifier = Modifier.padding(8.dp)) {
                        Column {
                            // AsyncImage(model = album.image, contentDescription = album.name)
                            Text(text = album.name)
                            Text(text = album.artist_name)
                        }
                    }
                }
            }
        }

        // Albums by Genre
        uiState.genreAlbums.forEach { (genre, albums) ->
            item {
                Text(genre, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(albums) { album ->
                        Card(modifier = Modifier.padding(8.dp)) {
                            Column {
                                // AsyncImage(model = album.image, contentDescription = album.name)
                                Text(text = album.name)
                                Text(text = album.artist_name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AiPromptDialog(onDismiss: () -> Unit, onGenerate: (String) -> Unit) {
    var prompt by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("AI Recommendations") },
        text = {
            OutlinedTextField(value = prompt, onValueChange = { prompt = it }, label = { Text("e.g., songs for a rainy day") })
        },
        confirmButton = {
            Button(onClick = { onGenerate(prompt) }) {
                Text("Generate")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel(factory = ViewModelFactory(LocalContext.current.applicationContext as Application)),
    nowPlayingViewModel: NowPlayingViewModel
) {
    val uiState by searchViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Tracks", "Albums", "Artists")

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        androidx.compose.material3.TextField(
            value = uiState.searchQuery,
            onValueChange = { searchViewModel.onSearchQueryChanged(it) },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { searchViewModel.search() }) {
            Text("Search")
        }

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
            0 -> LazyColumn {
                items(uiState.tracks) { track ->
                    Text(
                        text = "${track.name} - ${track.artist_name}",
                        modifier = Modifier.clickable {
                            nowPlayingViewModel.playTrack(track)
                        }
                    )
                }
            }
            1 -> LazyColumn {
                items(uiState.albums) { album ->
                    Text(text = "${album.name} - ${album.artist_name}")
                }
            }
            2 -> LazyColumn {
                items(uiState.artists) { artist ->
                    Text(text = artist.name)
                }
            }
        }
    }
}

@Composable
fun LibraryScreen(libraryViewModel: LibraryViewModel = viewModel(factory = ViewModelFactory(LocalContext.current.applicationContext as Application))) {
    val uiState by libraryViewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Playlists", "Albums", "Artists", "Downloads")
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }

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

        if (showCreatePlaylistDialog) {
            CreatePlaylistDialog(
                onDismiss = { showCreatePlaylistDialog = false },
                onCreate = { name, description ->
                    libraryViewModel.createPlaylist(name, description)
                    showCreatePlaylistDialog = false
                }
            )
        }

        when (selectedTabIndex) {
            0 -> PlaylistsTab(
                playlists = uiState.playlists,
                onCreatePlaylist = { showCreatePlaylistDialog = true }
            )
            1 -> AlbumsTab(albums = uiState.albums)
            2 -> ArtistsTab(artists = uiState.artists)
            3 -> DownloadsTab(downloads = uiState.downloads)
        }
    }
}

@Composable
fun PlaylistsTab(
    playlists: List<com.roshan.music.data.database.PlaylistWithTracks>,
    onCreatePlaylist: () -> Unit
) {
    Column {
        Button(onClick = onCreatePlaylist) {
            Text("Create Playlist")
        }
        LazyColumn {
            items(playlists) { playlist ->
                Text(text = "${playlist.playlist.name} (${playlist.tracks.size} songs)")
            }
        }
    }
}

@Composable
fun CreatePlaylistDialog(onDismiss: () -> Unit, onCreate: (String, String?) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Playlist") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = { onCreate(name, description) }) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
