package com.example.testapp.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import java.io.InputStreamReader
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TestAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val themeData = loadMaterialTheme(context)
    val colorScheme = createColorScheme(themeData, darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


fun loadMaterialTheme(context: Context): MaterialThemeData {
    val inputStream = context.assets.open("material-theme.json")
    val reader = InputStreamReader(inputStream)
    return Gson().fromJson(reader, MaterialThemeData::class.java)
}

fun createColorScheme(themeData: MaterialThemeData, darkTheme: Boolean): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = Color(android.graphics.Color.parseColor(themeData.primary)),
            secondary = Color(android.graphics.Color.parseColor(themeData.secondary)),
            tertiary = Color(android.graphics.Color.parseColor(themeData.tertiary))
        )
    } else {
        lightColorScheme(
            primary = Color(android.graphics.Color.parseColor(themeData.primary)),
            secondary = Color(android.graphics.Color.parseColor(themeData.secondary)),
            tertiary = Color(android.graphics.Color.parseColor(themeData.tertiary))
        )
    }
}