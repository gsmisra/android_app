package com.dev.gauravmisra.lostinmystory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dev.gauravmisra.lostinmystory.model.Category
import com.dev.gauravmisra.lostinmystory.ui.StoryViewModel
import com.dev.gauravmisra.lostinmystory.ui.screens.CatalogScreen
import com.dev.gauravmisra.lostinmystory.ui.screens.HomeScreen
import com.dev.gauravmisra.lostinmystory.ui.screens.ReaderScreen
import com.dev.gauravmisra.lostinmystory.ui.theme.OneMoreChoiceTheme
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.dev.gauravmisra.lostinmystory.audio.BackgroundMusicManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OneMoreChoiceTheme  {

                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_START -> {
                                BackgroundMusicManager.start(context)
                            }
                            Lifecycle.Event.ON_STOP -> {
                                BackgroundMusicManager.pause()
                            }
                            Lifecycle.Event.ON_DESTROY -> {
                                BackgroundMusicManager.stop()
                            }
                            else -> {}
                        }
                    }

                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                        BackgroundMusicManager.stop()
                    }
                }


                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNav()
                }

            }
        }
    }
}


@Composable
fun AppNav() {
    val nav = rememberNavController()
    val vm: StoryViewModel = viewModel()


    NavHost(navController = nav, startDestination = "home") {

        composable("home") {
            HomeScreen(
                categories = vm.categories(),
                onCategoryClick = { cat ->
                    nav.navigate("catalog/${cat.id}")
                }
            )
        }

        composable(
            route = "catalog/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("categoryId")!!
            val category = Category.entries.first { it.id == id }

            CatalogScreen(
                category = category,
                stories = vm.stories(category),
                onStoryClick = { storyId ->
                    nav.navigate("reader/$storyId")
                },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "reader/{storyId}",
            arguments = listOf(navArgument("storyId") { type = NavType.StringType })
        ) { backStack ->
            val storyId = backStack.arguments?.getString("storyId")!!

            ReaderScreen(
                storyId = storyId,
                vm = vm,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
