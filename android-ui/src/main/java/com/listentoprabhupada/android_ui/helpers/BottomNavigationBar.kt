//package com.listentoprabhupada.android_ui.screens.helpers
//
//import androidx.compose.foundation.Image
//import androidx.compose.material3.BottomNavigation
//import androidx.compose.material3.BottomNavigationItem
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.material3.icons.Icons
//import androidx.compose.material3.icons.rounded.Download
//import androidx.compose.material3.icons.rounded.Favorite
//import androidx.compose.material3.icons.rounded.Filter
//import androidx.compose.material3.icons.rounded.FilterList
//import androidx.compose.material3.icons.rounded.List
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import com.listentoprabhupada.common.root.RootComponent
//
////val items = listOf(
////    NavigationItem.Home,
////    NavigationItem.Music,
////    NavigationItem.Movies,
////    NavigationItem.Books,
////    NavigationItem.Profile
////)
//
//sealed class BottomNavigationItem(val imageVector: ImageVector, val title: String) {
//    object Results: BottomNavigationItem(Icons.Rounded.List, "Лекции")
//    object Favorites: BottomNavigationItem(Icons.Rounded.Favorite, "Избранное")
//    object Downloads: BottomNavigationItem(Icons.Rounded.Download, "Загрузки")
//    object Filters: BottomNavigationItem(Icons.Rounded.FilterList, "Фильтры")
//}
////
////val botomNavigationItems = listOf(
////
////        )
//
//@Composable
//fun BottomNavigationBar() {
//    BottomNavigation(
//        backgroundColor = Color.White,
//        contentColor = Orange
//    ) {
//        BottomNavigationItem(
//            icon = { Icon(imageVector = item.imageVector, contentDescription = item.title) },
//            label = { Text(text = item.title) },
//            selectedContentColor = Orange,
//            unselectedContentColor = Orange.copy(0.4f),
//            alwaysShowLabel = true,
//            selected = false,
//            onClick = {
//                /* Add code later */
//            }
//        )
//    }
//}