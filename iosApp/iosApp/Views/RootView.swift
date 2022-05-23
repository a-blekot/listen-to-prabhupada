//
//  RootView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada
import AVFoundation

struct RootView: View {
    
    @EnvironmentObject var theme: Theme
    
    @ObservedObject
    private var routerStates: ObservableValue<RouterState<AnyObject, RootComponentChild>>
    
    init(_ component: RootComponent) {
        self.routerStates = ObservableValue(component.routerState)
        
        NapierProxyKt.debugBuild()
    }
    
    var body: some View {
        let child = routerStates.value.activeChild.instance

        switch child {
        case let results as RootComponentChildResults:
            ResultsView(component: results.component)

        case let filters as RootComponentChildFilters:
            FiltersView(filters.component)
                .transition(
                    .asymmetric(
                        insertion: AnyTransition.move(edge: .trailing),
                        removal: AnyTransition.move(edge: .trailing)
                    )
                )
                .animation(.easeInOut)

        default: EmptyView() // fatalError("Unexpected router state \(child)")
        }
    }
}

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView(StubRoot())
            .environmentObject(themes[0])
    }

    class StubRoot : RootComponent {
        let routerState: Value<RouterState<AnyObject, RootComponentChild>> =
            simpleRouterState(RootComponentChildResults(component: StubResultsComponent()))
    }
}
