//
//  PlayerView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct PlayerView: View {
    private let component: PlayerComponent
    
    @ObservedObject
    private var models: ObservableValue<PlayerState>
    
    init(_ component: PlayerComponent) {
        self.component = component
        self.models = ObservableValue(component.flow)
    }
    
    var body: some View {
        let model = models.value
        
        Text(model.lecture.title)
    }
}

//struct PlayerView_Previews: PreviewProvider {
//    static var previews: some View {
//        PlayerView()
//    }
//}
