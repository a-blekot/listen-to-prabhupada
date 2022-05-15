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
                .listRowInsets(EdgeInsets())
        }
        .padding(-10)
        .edgesIgnoringSafeArea(.bottom)
//        .listStyle(.plain)
        .ignoresSafeArea(edges: .top)
    }
}

extension Lecture: Identifiable {}

//struct LectureListView_Previews: PreviewProvider {
//    static var previews: some View {
//        LectureListView(onEvent: {_ in})
//            .environmentObject(ModelData())
//    }
//}

//
//listView()
//    .navigationBarTitle("Lectures")
//    .navigationBarItems(
//        trailing: Button(
//            "Reload",
//            action: {
//                incPage()
//                self.viewModel.loadLectures(page: pager.page)
//            })
//    )
//    .onAppear(perform: { self.viewModel.startObserving() } )
//
//List(lectures) { lecture in
//    LectureListItem(lecture: lecture, isPlaying: false, onEvent: {e in viewModel.onEvent(e)})
//        .frame( maxWidth: .infinity)
//}
//    .frame(maxWidth: .infinity)
//    .padding(-10)
//    .edgesIgnoringSafeArea(.horizontal)
//    .edgesIgnoringSafeArea(.bottom)
//}
