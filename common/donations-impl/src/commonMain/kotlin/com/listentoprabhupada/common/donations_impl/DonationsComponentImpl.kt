package com.listentoprabhupada.common.donations_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.statekeeper.consume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.listentoprabhupada.common.data.Donation
import com.listentoprabhupada.common.donations_api.DonationsComponent
import com.listentoprabhupada.common.donations_api.DonationsOutput
import com.listentoprabhupada.common.donations_api.DonationsState
import com.listentoprabhupada.common.donations_impl.store.DonationsIntent.*
import com.listentoprabhupada.common.donations_impl.store.DonationsLabel
import com.listentoprabhupada.common.donations_impl.store.DonationsStoreFactory
import com.listentoprabhupada.common.utils.*
import com.listentoprabhupada.common.utils.billing.BillingEvent
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val KEY_DONNATIONS_STATE = "KEY_DONNATIONS_STATE"

class DonationsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: DonationsDeps,
    private val output: Consumer<DonationsOutput>
) : DonationsComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            DonationsStoreFactory(
                storeFactory = storeFactory,
                initialState = stateKeeper.consume(KEY_DONNATIONS_STATE) ?: initialState,
                deps = deps,
            ).create()
        }

    private val initialState
        get() = DonationsState(deps.billingHelper?.availableDonations?.loadTitles() ?: emptyList())

    private val scope: CoroutineScope = lifecycleCoroutineScope(deps.dispatchers.main)

    override val flow: Value<DonationsState> = store.asValue()

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)

        store.init(instanceKeeper)
        stateKeeper.register(KEY_DONNATIONS_STATE) { store.state }

        observeBillingHelperEvents()
    }

    override fun purchase(donation: Donation) = store.accept(Purchase(donation))

    private fun handleLabel(label: DonationsLabel) {
        when (label) {
            DonationsLabel.SuccessPurchase -> output.invoke(DonationsOutput.SuccessPurchase)
        }
    }

    private fun observeBillingHelperEvents() =
        deps.billingHelper?.run {
            scope.launch {
                events.collect(::handleBillingEvent)
            }
        }

    private fun handleBillingEvent(event: BillingEvent) =
        when (event) {
            is BillingEvent.DonationsLoaded -> store.accept(LoadingComplete(event.list))
            is BillingEvent.Error -> {
                Napier.e(
                    message = event.logMsg,
                    throwable = event.throwable,
                    tag = "BillingError"
                )
            }
            is BillingEvent.PurchaseSuccess -> store.accept(SuccessPurchase)
        }

    private val BillingEvent.Error.logMsg
        get() = "operation: $operation, responseCode: $responseCode, msg: $msg"

    private fun List<Donation>.loadTitles() =
        map { it.copy(title = deps.stringResourceHandler.resolveDonationTitle(it.donationLevel)) }
}
