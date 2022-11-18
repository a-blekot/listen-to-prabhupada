package com.listentoprabhupada.android.inapp

import android.app.Activity
import com.listentoprabhupada.common.utils.billing.BillingEvent
import com.listentoprabhupada.common.utils.billing.BillingEvent.PurchaseSuccess
import com.listentoprabhupada.common.utils.billing.BillingHelper
import com.listentoprabhupada.common.utils.billing.BillingOperation
import com.listentoprabhupada.common.utils.billing.BillingOperation.*
import com.listentoprabhupada.common.data.Donation
import com.listentoprabhupada.common.data.DonationLevel
import com.listentoprabhupada.common.data.getDonationLevel
import com.listentoprabhupada.common.utils.connectivity.ConnectivityObserver
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.Purchase.PurchaseState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BillingHelperAndroid(
    private val activity: Activity,
    private val scope: CoroutineScope,
    connectivityObserver: ConnectivityObserver,
) : BillingHelper {

    private var productDetailsList = emptyList<ProductDetails>()

    private val billingClient: BillingClient

    private val _events = MutableSharedFlow<BillingEvent>()

    private val purchaseListener = PurchasesUpdatedListener { billingResult, purchasesList ->
        if (billingResult.isOk) {
            if (purchasesList == null) {
                emitEvent(PurchaseSuccess(null))
            } else {
                purchasesList.forEach(::processPurchase)
            }
        } else {
            emitEvent(BillingEvent.Error(UPDATE_LISTENER, billingResult.responseCode, billingResult.debugMessage))
        }
    }

    override val availableDonations
        get() = productDetailsList
            .mapNotNull { it.toDonation() }
            .sortedBy { it.priceAmountMicros }

    override val events = _events.asSharedFlow()

    init {
        billingClient = BillingClient.newBuilder(activity)
            .enablePendingPurchases()
            .setListener(purchaseListener)
            .build()

        connectivityObserver.observe()
            .onEach { handleConnectionStatus(it) }
            .launchIn(scope)
    }

    override fun clean() {
        billingClient.endConnection()
    }

    override fun purchase(donation: Donation) {
        donation.toBillingFlowParams()?.let { billingFlowParams ->
            activity.runOnUiThread {
                onConnected {
                    billingClient.launchBillingFlow(activity, billingFlowParams)
                }
            }
        }
    }

    override fun checkUnconsumedPurchases() {
        onConnected {
            billingClient.queryPurchasesAsync(inappQueryPurchasesParams) { billingResult, purchasesList ->
                if (billingResult.isOk) {
                    purchasesList.forEach {
                        processPurchase(it)
                    }
                } else {
                    emitError(CHECK_UNCONSUMED, billingResult)
                }
            }
        }
    }

    private fun getProductDetails() {
        onConnected {
            billingClient.queryProductDetailsAsync(getQueryProductDetailsParams()) { billingResult, productDetailsList ->
                if (billingResult.isOk) {
                    this.productDetailsList = productDetailsList
                    emitEvent(BillingEvent.DonationsLoaded(availableDonations))
                } else {
                    emitError(GET_PRODUCT_DETAILS, billingResult)
                }
            }
        }
    }

    private fun processPurchase(purchase: Purchase) {
        if (purchase.isPurchased) {
            purchase.products.forEach { id ->
                availableDonations
                    .firstOrNull { it.donationLevel.productId == id }
                    ?.let { emitEvent(PurchaseSuccess(it)) }
            }

            consumePurchase(purchase) { billingResult, _ ->
                if (!billingResult.isOk) {
                    emitError(CONSUME_PURCHASE, billingResult)
                    //implement retry logic or try to consume again in onResume()
                }
            }
        }
    }

    private fun consumePurchase(purchase: Purchase, callback: ConsumeResponseListener) {
        onConnected {
            billingClient.consumeAsync(purchase.consumeParams) { billingResult, purchaseToken ->
                callback.onConsumeResponse(billingResult, purchaseToken)
            }
        }
    }

    private fun onConnected(block: () -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.isOk) {
                    block()
                }
            }

            override fun onBillingServiceDisconnected() {
                emitEvent(BillingEvent.Error(SERVER_DISCONNECTED))
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun handleConnectionStatus(available: Boolean) {
        Napier.d("handleConnectionStatus = $available", tag = "BILLING")
        if (available && availableDonations.isEmpty()) {
            Napier.d("getProductDetails", tag = "BILLING")
            getProductDetails()
        }
    }

    private fun getQueryProductDetailsParams() =
        QueryProductDetailsParams.newBuilder()
            .setProductList(getProducts())
            .build()

    private fun getProducts() =
        DonationLevel.values().map {
            getProduct(it)
        }

    private fun getProduct(donationLevel: DonationLevel) =
        QueryProductDetailsParams.Product
            .newBuilder()
            .setProductId(donationLevel.productId)
            .setProductType(ProductType.INAPP)
            .build()

    private fun Donation.toBillingFlowParams() =
        toProductDetailsParamsList()?.let { list ->
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(list)
                .build()
        }

    private fun Donation.toProductDetailsParamsList() =
        toProductDetailsParams()?.let { productDetails -> listOf(productDetails) }

    private fun Donation.toProductDetailsParams() =
        productDetailsList.firstOrNull { it.productId == donationLevel.productId }?.let {
            ProductDetailsParams.newBuilder()
                .setProductDetails(it)
                .build()
        }

    private val BillingResult.isOk
        get() = responseCode == BillingResponseCode.OK

    private val Purchase.isPurchased
        get() = purchaseState == PurchaseState.PURCHASED

    private val Purchase.consumeParams
        get() = ConsumeParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()

    private val inappQueryPurchasesParams
        get() = QueryPurchasesParams.newBuilder()
            .setProductType(ProductType.INAPP)
            .build()

    private fun emitError(operation: BillingOperation, result: BillingResult) {
        Napier.e("getProductDetails $operation ${result.responseCode} ${result.debugMessage}", tag = "BILLING")
        emitEvent(BillingEvent.Error(operation, result.responseCode, result.debugMessage))
    }

    private fun emitEvent(event: BillingEvent) =
        scope.launch {
            _events.emit(event)
        }

    private fun ProductDetails.toDonation(): Donation? {
        val donationLevel = getDonationLevel(productId) ?: return null
//            productId     name          title                           description                         formattedPrice
//            donate_1_usd, Donate 1 USD, Donate 1 USD (Memorize Shloka), Support this app by donating 1 USD, UAH 47.99

        return oneTimePurchaseOfferDetails?.run {
            Donation(donationLevel, formattedPrice, priceAmountMicros)
        }
    }
}