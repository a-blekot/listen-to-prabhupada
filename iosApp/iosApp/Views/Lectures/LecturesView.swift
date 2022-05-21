//
//  LectureList.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct LecturesView: View {
    private let component: LecturesComponent
    
    @ObservedObject
    private var models: ObservableValue<LecturesState>
    
    init(_ component: LecturesComponent) {
        self.component = component
        self.models = ObservableValue(component.flow)
    }
    
    var body: some View {
        let model = models.value
        
        List(model.lectures){ lecture in
            LectureListItem(lecture: lecture, component: component)
                //.listRowInsets(EdgeInsets())
        }
        //    .frame(maxWidth: .infinity)
        .edgesIgnoringSafeArea(.bottom)
        .edgesIgnoringSafeArea(.horizontal)
        .listStyle(.plain)
//        .ignoresSafeArea(edges: .top)
    }
}

extension Lecture: Identifiable {}

struct LecturesView_Previews: PreviewProvider {
    static var previews: some View {
        LecturesView(StubLecturesComponent())
            .environmentObject(themes[0])
    }
}
