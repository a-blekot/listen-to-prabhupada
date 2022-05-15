import SwiftUI
import Combine
import Prabhupada

struct ContentView: View {
    
    @State
    private var componentHolder =
        ComponentHolder<RootComponentImpl> {
            RootComponentImpl(
                componentContext: $0,
                storeFactory: DefaultStoreFactory(),
                deps: RootDeps(
                    db: DatabaseImpl(databaseDriverFactory: DatabaseDriverFactory()),
                    api: PrabhupadaApiImpl(client: KtorClientFactory().build(), dispatchers: DispatcherProviderImplKt.dispatchers()),
                    playerBus: PlayerBusImpl(dispatchers: DispatcherProviderImplKt.dispatchers()),
                    dispatchers: DispatcherProviderImplKt.dispatchers()
                )
            )
        }
        
    var body: some View {
        RootView(componentHolder.component)
            .onAppear { LifecycleRegistryExtKt.resume(self.componentHolder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(self.componentHolder.lifecycle) }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
