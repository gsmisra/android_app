package com.dev.gauravmisra.lostinmystory.ui


import android.graphics.BitmapFactory
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun rememberAssetImageBitmap(assetPath: String?): ImageBitmap? {
    val context = LocalContext.current
    var image by remember(assetPath) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(assetPath) {
        image = null
        val path = assetPath?.trim().orEmpty()
        if (path.isBlank()) return@LaunchedEffect

        image = withContext(Dispatchers.IO) {
            try {
                context.assets.open(path).use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            } catch (_: Exception) {
                null
            }
        }
    }
    return image
}
