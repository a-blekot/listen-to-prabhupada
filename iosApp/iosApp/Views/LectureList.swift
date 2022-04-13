//
//  LectureList.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LectureList: View {
    @EnvironmentObject var modelData: ModelData
    let onEvent: (CommonUiEvent) -> ()
    
    var body: some View {
        List {
            ForEach(modelData.lectures){ lecture in
                LectureListItem(lecture: lecture, isPlaying: false, onEvent: onEvent)
                    .listRowInsets(EdgeInsets())
            }
        }
        .padding(-10)
        .edgesIgnoringSafeArea(.bottom)
//        .listStyle(.plain)
        .ignoresSafeArea(edges: .top)
    }
}

struct LectureList_Previews: PreviewProvider {
    static var previews: some View {
        LectureList(onEvent: {_ in})
            .environmentObject(ModelData())
    }
}
