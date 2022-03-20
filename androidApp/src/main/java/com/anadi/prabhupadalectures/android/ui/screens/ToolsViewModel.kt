package com.anadi.prabhupadalectures.android.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.repository.ResultsRepository
import com.anadi.prabhupadalectures.repository.ToolsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel@Inject constructor(
    private val resultsRepository: ResultsRepository,
    private val toolsRepository: ToolsRepository,
) : ViewModel() {

    fun updateQuery(queryParam: QueryParam) =
        viewModelScope.launch {
            resultsRepository.updateQuery(queryParam)
        }

    fun isExpanded(filterName: String) =
        toolsRepository.isExpanded(filterName)

    fun saveExpanded(filterName: String, isExpanded: Boolean) =
        toolsRepository.saveExpanded(filterName, isExpanded)
}
