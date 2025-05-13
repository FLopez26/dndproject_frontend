package com.example.dndproject_frontend.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

data class AppColors(
    val outlinedTextFieldFocusedBorder: Color,
    val outlinedTextFieldUnfocusedBorder: Color,
    val outlinedTextFieldCursor: Color,
    val outlinedTextFieldFocusedText: Color,
    val outlinedTextFieldFocusedLabel: Color,
    val outlinedTextFieldUnfocusedLabel: Color,
    val buttonContainer: Color,
    val buttonContent: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}

private val DarkColorScheme = darkColorScheme(
    primary = RedPrimary,
    secondary = GrayPrimary,
    background = LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = RedPrimary,
    secondary = GrayPrimary,
    background = LightGray
)

object AppStyles {
    val outlinedTextFieldColors: TextFieldColors
        @Composable
        get() = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppTheme.colors.outlinedTextFieldFocusedBorder,
            unfocusedBorderColor = AppTheme.colors.outlinedTextFieldUnfocusedBorder,
            cursorColor = AppTheme.colors.outlinedTextFieldCursor,
            focusedTextColor = AppTheme.colors.outlinedTextFieldFocusedText,
            focusedLabelColor = AppTheme.colors.outlinedTextFieldFocusedLabel,
            unfocusedLabelColor = AppTheme.colors.outlinedTextFieldUnfocusedLabel
        )

    val buttonColors: ButtonColors
        @Composable
        get() = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.buttonContainer,
            contentColor = AppTheme.colors.buttonContent
        )
}

@Composable
fun AppColors(): AppColors {
    return AppColors(
        outlinedTextFieldFocusedBorder = GrayPrimary,
        outlinedTextFieldUnfocusedBorder = GrayPrimary,
        outlinedTextFieldCursor = GrayPrimary,
        outlinedTextFieldFocusedText = MaterialTheme.colorScheme.onBackground,
        outlinedTextFieldFocusedLabel = GrayPrimary,
        outlinedTextFieldUnfocusedLabel = GrayPrimary,
        buttonContainer = RedPrimary,
        buttonContent = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun Dndproject_frontendTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val appColors = AppColors()

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}