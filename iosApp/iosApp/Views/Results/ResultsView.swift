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
    
    let component: ResultsComponent
    
    var body: some View {
        
        VStack {
            LecturesView(component.lecturesComponent)
        
            PlayerView(component.playerComponent)
        }
    }
}

//struct ResultsView_Previews: PreviewProvider {
//    static var previews: some View {
//        ResultsView()
//    }
//    
//    class StubResultsComponent: ResultsComponent {
//        
//    }
//}
