package com.faye.devto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faye.devto.details.BlogPostDetailsScreen
import com.faye.devto.overview.BlogPostsOverviewScreen
import com.faye.devto.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen() {
    val navController = rememberNavController()
    var showNavigationBack by remember { mutableStateOf(false) }

    navController.addOnDestinationChangedListener { controller, _, _ ->
        showNavigationBack = controller.previousBackStackEntry != null
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                navigationIcon = {
                    if (showNavigationBack) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr),
                    it.calculateTopPadding(),
                    it.calculateEndPadding(LayoutDirection.Ltr),
                    it.calculateBottomPadding()
                ),
            color = MaterialTheme.colorScheme.background
        ) {
            with(navController.toNavGraph()) {
                NavHost {
                    addBlogPostsOverviewContent {
                        BlogPostsOverviewScreen(hiltViewModel()) { blogPost ->
                            navigateToDetails(blogPost.id)
                        }
                    }
                    addBlogPostDetailsContent {
                        BlogPostDetailsScreen(hiltViewModel())
                    }
                }
            }
        }
    }
}

@Composable
private fun NavHostController.toNavGraph(): NavGraph = remember { NavGraph(this) }
