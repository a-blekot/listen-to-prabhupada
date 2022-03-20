package com.anadi.prabhupadalectures.android.navigation

import androidx.lifecycle.ViewModel
import com.anadi.prabhupadalectures.android.navigation.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(
    private val router: Router,
) : ViewModel(), Router by router
