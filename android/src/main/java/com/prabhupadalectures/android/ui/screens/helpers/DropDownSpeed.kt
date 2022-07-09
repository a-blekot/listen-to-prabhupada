package com.prabhupadalectures.android.ui.screens.helpers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

//
//private val list = listOf(
//    Speedy("0,50", 0.50f),
//    Speedy("0,75", 0.75f),
//    Speedy("1,00", 1.00f),
//    Speedy("1,25", 1.25f),
//    Speedy("1,50", 1.50f),
//    Speedy("1,75", 1.75f),
//    Speedy("2,00", 2.00f),
//)
//
//private class Speedy(val title: String, val speed: Float)

//Spacer(modifier = Modifier.height(20.dp))
//
//var expanded by remember { mutableStateOf(false) }

//            Spacer(modifier = Modifier.height(10.dp))

//
//            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                list.forEach {
//                    DropdownMenuItem(
//                        onClick = {
//                            expanded = false
//                            playerComponent.onSpeed(it.speed)
//                        }
//                    ) {
//                        Text(text = it.title)
//                    }
//                }
//            }