import SwiftUI
import Combine
import Prabhupada
import AVFoundation

struct ContentView: View {
    
    @EnvironmentObject var theme: Theme
    
    @State
    private var componentHolder: ComponentHolder<RootComponentImpl>
    
    init(_ playerBus: PlayerBus, _ db: Database?) {
        _componentHolder = State(
            initialValue: ComponentHolder<RootComponentImpl> {
                RootComponentImpl(
                    componentContext: $0,
                    storeFactory: DefaultStoreFactory(),
                    deps: RootDeps(
                        db: db!,
                        api: PrabhupadaApiKt.createPrabhupadaApi(),
                        playerBus: playerBus,
                        remoteConfig: RemoteConfig(useSimplePageView: true),
                        dispatchers: DispatcherProviderImplKt.dispatchers()
                    )
                )
            }
        )
    }
    
    var body: some View {
        RootView(componentHolder.component)
            .onAppear {
                LifecycleRegistryExtKt.resume(self.componentHolder.lifecycle)
                
                UIApplication.shared.beginReceivingRemoteControlEvents()
                //                self.becomeFirstResponder()
            }
            .onDisappear {
                LifecycleRegistryExtKt.stop(self.componentHolder.lifecycle)
                
                UIApplication.shared.endReceivingRemoteControlEvents()
            }
            .environmentObject(theme)
    }
    
    
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(StubPlayerBus(), nil)
            .environmentObject(themes[0])
    }
}
