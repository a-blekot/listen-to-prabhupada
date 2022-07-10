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
    
    private let component: RootComponent
    
    init(_ component: RootComponent) {
        self.component = component
        self.routerStates = ObservableValue(component.routerState)
        
        NapierProxyKt.debugBuild()
    }
    
    var body: some View {
        let child = routerStates.value.activeChild.instance

        VStack {
            ChildView(child: child)
                .frame(maxHeight: .infinity)
            
            HStack(spacing: 8) {
                
                Button(action: component.onResultsTabClicked) {
                    Label("Лекции", systemImage: "list.bullet")
                        .labelStyle(IconOnlyLabelStyle())
                        .opacity(child is RootComponentChildResults ? 1 : 0.5)
                        .frame(maxWidth: .infinity)
                }
                
                Button(action: component.onFavoritesTabClicked) {
                    Label("Избранное", systemImage: "heart.fill")
                        .labelStyle(IconOnlyLabelStyle())
                        .opacity(child is RootComponentChildFavorites ? 1 : 0.5)
                        .frame(maxWidth: .infinity)
                }
                
//                Button(action: component.onDownloadsTabClicked) {
//                    Label("Favorites", systemImage: "heart.fill")
//                        .labelStyle(VerticalLabelStyle())
//                        .opacity(child is RootComponentChildFavorites ? 1 : 0.5)
//                }
                
                Button(action: component.onFiltersTabClicked) {
                    Label("Фильтры", systemImage: "line.3.horizontal.decrease.circle")
                        .labelStyle(IconOnlyLabelStyle())
                        .opacity(child is RootComponentChildFilters ? 1 : 0.5)
                        .frame(maxWidth: .infinity)
                }
            }
            .foregroundColor(Color.orange)
        }
    }
}

private struct ChildView: View {
    let child: RootComponentChild
    
    var body: some View {
        switch child {
        case let results as RootComponentChildResults:
            ResultsView(component: results.component)
            
        case let filters as RootComponentChildFilters:
            FiltersView(filters.component)
                .transition(.move(edge: .trailing))
            
//        case let downloads as RootComponentChildDownloads:
//            FavoritesFeatureView(favorites.component)
//                .transition(.move(edge: .trailing))
            
        case let favorites as RootComponentChildFavorites:
            FavoritesFeatureView(favorites.component)
                .transition(.move(edge: .trailing))

        default: EmptyView() // fatalError("Unexpected router state \(child)")
        }
    }
}

private struct IconLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.icon
    }
}

private struct VerticalLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        VStack(alignment: .center, spacing: 8) {
            configuration.icon
            configuration.title
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
        
        func onResultsTabClicked() {}
        func onFavoritesTabClicked() {}
        func onDownloadsTabClicked() {}
        func onFiltersTabClicked() {}
    }
}
