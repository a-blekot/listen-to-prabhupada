package com.anadi.prabhupadalectures.android.base.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(
    private val router: Router,
) : ViewModel(), Router by router