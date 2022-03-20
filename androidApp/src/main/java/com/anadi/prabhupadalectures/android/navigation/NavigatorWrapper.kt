package com.anadi.prabhupadalectures.android.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.anadi.prabhupadalectures.android.navigation.RouterEvent.Pop
import com.anadi.prabhupadalectures.android.navigation.RouterEvent.Push
import com.anadi.prabhupadalectures.android.di.Route.RMainScreen
import io.github.aakira.napier.Napier
import kotlin.reflect.KClass

private typealias ProviderKey = KClass<out ScreenProvider>
private typealias ScreenFactory = (ScreenProvider) -> Screen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigatorWrapper() {
    var navigator: Navigator? = null
    val router = hiltViewModel<RouterViewModel>()

    LaunchedEffect(Unit) {
        Napier.e("BEFORE ROUTER")
        router.routerEvents.collect { routerEvent ->
            Napier.e("routerEvent: $routerEvent")
            when (routerEvent) {
                is Pop -> {
                    Napier.e("GoBack")
                    navigator?.pop()
                }
                is Push -> {
                    val screens = routerEvent.routes.map { it.toScreen() }
                    navigator?.push(screens)
                }
            }
        }
    }

    val screen = rememberScreen(RMainScreen)
    Navigator(screen) {
        navigator = it
        SlideTransition(it)
    }
}

@Suppress("UNCHECKED_CAST")
object ScreenRegistryHelper {
    private val factoriesMember by lazy { ScreenRegistry::class.members.find { it.name == "factories" } }
    private val factories: ThreadSafeMap<ProviderKey, ScreenFactory> by lazy {
        factoriesMember!!.call(ScreenRegistry) as ThreadSafeMap<ProviderKey, ScreenFactory>
    }

    fun get(route: Route): Screen {
        val screenFactory = factories[route::class]!!
        return screenFactory.invoke(route)
    }
}

fun Route.toScreen(): Screen {
    return ScreenRegistryHelper.get(this)
}