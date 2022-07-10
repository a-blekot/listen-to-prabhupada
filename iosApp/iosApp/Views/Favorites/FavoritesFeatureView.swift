//
//  FavoritesFeatureView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FavoritesFeatureView: View {
    @EnvironmentObject var theme: Theme
    @State var hideTopBar = false
    @State var hideBottomBar = false
    
    let component: FavoritesFeatureComponent
    init(_ component: FavoritesFeatureComponent) {
        self.component = component
    }
    
    var body: some View {
        NavigationView {
            ZStack(alignment: .bottom) {
                
                let topArea: CGFloat = 20
                let bottomArea: CGFloat = 550
                
                FavoritesView(component.favoritesComponent, $hideTopBar, $hideBottomBar, topArea, bottomArea)
                    .environmentObject(theme)
                    .animation(.easeOut(duration: 0.2))
                
                if !hideBottomBar {
                    PlayerView(component.playerComponent)
                        .environmentObject(theme)
                        .transition(
                            .move(edge: .bottom)
                            .combined(with: .scale(scale: 0.1, anchor: .bottom))
                        )
                }

            }
            .navigationTitle("Favorites")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarHidden(hideTopBar)
            .animation(.easeOut(duration: 0.7))
        }
    }
}

struct FavoritesFeatureView_Previews: PreviewProvider {
    static var previews: some View {
        FavoritesFeatureView(StubFavoritesFeatureComponent())
            .environmentObject(themes[0])
    }
}
