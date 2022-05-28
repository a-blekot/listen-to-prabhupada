//
//  ResultsView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct ResultsView: View {
    
    @EnvironmentObject var theme: Theme
    @State var hideTopBar = false
    @State var hideBottomBar = false
    
    let component: ResultsComponent
    
    var body: some View {
        NavigationView {
            ZStack(alignment: .bottom) {
                
                let topArea: CGFloat = 20
                let bottomArea: CGFloat = 550
                
                //
                //                let t = UIScreen.main.focusedView?.safeAreaInsets.top
                //                let b = UIScreen.main.focusedView?.safeAreaInsets.bottom
                //
                //                let _ = print("top \(t), bottom \(b)")
                
                
                
                LecturesView(component.lecturesComponent, $hideTopBar, $hideBottomBar, topArea, bottomArea)
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
            .navigationTitle("Results")
            .navigationBarTitleDisplayMode(.inline)
            .navigationViewStyle(.stack)
            .navigationBarHidden(hideTopBar)
            .animation(.easeOut(duration: 0.7))
            .toolbar {
                Button("Filters") {
                    component.onEditFilters()
                }
                .foregroundColor(Color.orange)
            }
            
        }
    }
}

struct ResultsView_Previews: PreviewProvider {
    static var previews: some View {
        ResultsView(component: StubResultsComponent())
            .environmentObject(themes[0])
    }
}
