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
    
    let component: ResultsComponent
    
    var body: some View {
        NavigationView {
            VStack {
                LecturesView(component.lecturesComponent)
                    .environmentObject(theme)
            
                PlayerView(component.playerComponent)
                    .environmentObject(theme)
            }
            .navigationTitle("Results")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                Button("Filters") {
                    component.onEditFilters()
                }
            }
        }
        
        //    .navigationBarItems(
        //        trailing: Button(
        //            "Reload",
        //            action: {
        //                incPage()
        //                self.viewModel.loadLectures(page: pager.page)
        //            })
        //    )
        
    }
}

struct ResultsView_Previews: PreviewProvider {
    static var previews: some View {
        ResultsView(component: StubResultsComponent())
            .environmentObject(themes[0])
    }
}
