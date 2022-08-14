//
//  FavoritesFeatureView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FavoritesView: View {
    @EnvironmentObject var theme: Theme
    
    private let component: FavoritesComponent
    
    @ObservedObject
    private var state: ObservableValue<FavoritesState>

    init(_ component: FavoritesComponent) {
        self.component = component
        self.state = ObservableValue(component.flow)
    }
    
    var body: some View {
        NavigationView {
            ZStack(alignment: .bottom) {
                ScrollView(.vertical, showsIndicators: false) {
                    VStack(alignment: .center, spacing: 20) {
                        ForEach(state.value.lectures) { lecture in
                            LectureItemView(lecture: lecture, component: component)
                                .environmentObject(theme)
                        }
                    }
                }
            }
            .navigationBarHidden(true)
        }
    }
}

struct FavoritesView_Previews: PreviewProvider {
    static var previews: some View {
        FavoritesView(StubFavoritesComponent())
            .environmentObject(themes[0])
    }
}
