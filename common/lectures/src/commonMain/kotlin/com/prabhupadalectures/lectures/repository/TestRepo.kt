package com.prabhupadalectures.lectures.repository

import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import com.prabhupadalectures.lectures.data.lectures
import com.prabhupadalectures.lectures.data.lectures.Lecture
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TestState(
    val isLoading: Boolean = false,
    val lectures: List<Lecture> = emptyList(),
)

interface TestRepo {
    fun observeState(): Flow<TestState>

    suspend fun getResults(page: Int): ApiModel
    suspend fun updatePage(page: Int)
}

class TestRepoImpl(private val api: PrabhupadaApi) :
    CoroutineScope by CoroutineScope(Dispatchers.Default), TestRepo {

    private val state = MutableStateFlow(TestState())

    override fun observeState() = state.asStateFlow()

    override suspend fun getResults(page: Int): ApiModel =
        api.getResults(page)

    override suspend fun updatePage(page: Int) {
        if (state.value.isLoading) {
            Napier.d("loadMore canceled, isLoading = true!", tag = "TestRepo")
            return
        }

        updateLoading(true)
        updateData(api.getResults(page))
    }

    private fun updateData(apiModel: ApiModel) {
        val newState = TestState(
            isLoading = false,
            lectures = state.value.lectures + lectures(apiModel),
        )

        state.value = newState
    }

    private fun updateLoading(loading: Boolean) =
        updateStateIfNeeded(loading = loading)

    private fun updateStateIfNeeded(
        loading: Boolean = state.value.isLoading,
        lectures: List<Lecture> = state.value.lectures
    ) {
        if (loading != state.value.isLoading || lectures != state.value.lectures) {
            state.value = state.value.run {
                copy(
                    isLoading = loading,
                    lectures = lectures
                )
            }
        }
    }
}
