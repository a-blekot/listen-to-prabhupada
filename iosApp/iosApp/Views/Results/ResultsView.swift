//
//  ResultsView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

extension Lecture: Identifiable {}

struct ResultsView: View {
    
    @EnvironmentObject var theme: Theme
    
    @ObservedObject
    private var state: ObservableValue<ResultsState>
    
    let component: ResultsComponent
    
    init(_ component: ResultsComponent) {
        self.component = component
        self.state = ObservableValue(component.flow)
    }
    
    var body: some View {
        let state = state.value
        
        NavigationView {
            ZStack(alignment: .bottom) {
                ScrollView(.vertical, showsIndicators: false) {
                    VStack(alignment: .center, spacing: theme.dimens.paddingM) {
                        
                        if (state.useSimplePageView) {
                            SimplePageControlView(pagination: state.pagination, component: component)
                                .environmentObject(theme)
                        } else {
                            PageControlView(pagination: state.pagination, component: component)
                                .environmentObject(theme)
                        }

                        if !state.lectures.isEmpty {
                            ForEach(state.lectures) { lecture in
                                LectureItemView(lecture: lecture, component: component)
                                    .environmentObject(theme)
                            }
                        } else {
                            ForEach(0...8, id: \.self) { _ in
                                LectureShimmerView()
                            }
                            .animation(.easeInOut(duration: 2).repeatForever())
                        }
                    }
                }
                .padding(.horizontal, theme.dimens.horizontalScreenPadding)
            }
            .navigationTitle("Results")
            .navigationBarTitleDisplayMode(.inline)
            .navigationViewStyle(.stack)
            .animation(.easeOut(duration: 0.7))
            .foregroundColor(Color.orange)
        }
        
    }
}


struct ResultsView_Previews: PreviewProvider {
    static var previews: some View {
        ResultsView(StubResultsComponent())
            .environmentObject(themes[0])
    }
}
