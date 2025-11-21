    package com.example.apod_kt.ui.components

    import androidx.compose.foundation.layout.height
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Favorite
    import androidx.compose.material.icons.filled.Home
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material3.Icon
    import androidx.compose.material3.NavigationBar
    import androidx.compose.material3.NavigationBarItem
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.navigation.compose.rememberNavController
    import java.util.Locale

    enum class Destinations (
        val route: String,
        val title: String,
        val icon: ImageVector,
        val contentDescription: String
    ) {
        APOD("apod", "APOD", Icons.Default.Home, "APOD"),
        FAVOURITES("favourites", "Favourites", Icons.Default.Favorite, "Favourites"),
        USER("user", "User", Icons.Default.Person, "User")
    }

    @Composable
    fun BottomBar() {
        val navController = rememberNavController()
        val items = listOf("APOD", "Favourites", "User")

        NavigationBar (
            modifier = Modifier.height(90.dp)
        ) {
            items.forEach { route ->
                NavigationBarItem(
                    selected = navController.currentDestination?.route == route,
                    onClick = { navController.navigate(route) },
                    icon = {
                        val vector = when (route) {
                            "APOD" -> Icons.Default.Home
                            "Favourites" -> Icons.Default.Favorite
                            "User" -> Icons.Default.Person
                            else -> Icons.Default.Home
                        }

                        Icon(
                            imageVector = vector,
                            contentDescription = route
                        )
                    },
                    label = { Text(route.uppercase(Locale.getDefault()))}
                )
            }
        }
    }