package com.listentoprabhupada.common.results_impl.store

import com.arkivanov.mvikotlin.core.store.Store

import com.listentoprabhupada.common.results_api.ResultsState

internal interface ResultsStore : Store<ResultsIntent, ResultsState, ResultsLabel>

