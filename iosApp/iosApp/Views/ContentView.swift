import SwiftUI
import Combine
import Prabhupada
import AVFoundation

struct ContentView: View {
    
    @EnvironmentObject var theme: Theme
    
    @State
    private var componentHolder: ComponentHolder<RootComponentImpl>
    
    private var player: Player
    
    init(_ player: Player) {
        self.player = player
        
        _componentHolder = State(
            initialValue: ComponentHolder<RootComponentImpl> {
                RootComponentImpl(
                    componentContext: $0,
                    storeFactory: DefaultStoreFactory(),
                    deps: RootDeps(
                        db: DatabaseImpl(databaseDriverFactory: DatabaseDriverFactory()),
                        api: PrabhupadaApiKt.createPrabhupadaApi(),
                        playerBus: player.playerBus,
                        dispatchers: DispatcherProviderImplKt.dispatchers()
                    )
                )
            }
        )
    }
    
    var body: some View {
        RootView(componentHolder.component, player)
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
        ContentView(StubPlayer())
            .environmentObject(themes[0])
    }
}
