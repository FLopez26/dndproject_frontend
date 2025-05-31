package com.fls.dndproject_frontend.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fls.dndproject_frontend.presentation.navigation.Screen

data class NavigationItem(val title: String, val icon: ImageVector, val route: String)

@Composable
fun CustomNavigationBar(
    currentRoute: String?,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationItem("MyCharacters", Icons.Filled.Home, Screen.MyCharacters.route),
        NavigationItem("Forum", Icons.Filled.Public, Screen.Forum.route),
        NavigationItem("Profile", Icons.Filled.Person, Screen.Profile.route)
    )

    val topBarColor = Color(185, 0, 0)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        items.forEach { item ->
            val isSelected = currentRoute?.startsWith(item.route.substringBefore("/{")) == true

            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) topBarColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                animationSpec = tween(durationMillis = 300)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        onTabClick(item.route)
                    }
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = animatedColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}