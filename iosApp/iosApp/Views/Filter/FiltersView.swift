//
//  FiltersView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FiltersView: View {
    private let component: FiltersComponent
    
    @ObservedObject
    private var models: ObservableValue<FiltersState>
    
    init(_ component: FiltersComponent) {
        self.component = component
        self.models = ObservableValue(component.models)
    }
    
    var body: some View {
        let model = models.value
        
        return List(model.filters) { filter in
            FilterItemView(filter: filter, component: component)
                .frame( maxWidth: .infinity)
            }
            .frame(maxWidth: .infinity)
            .padding(-10)
            .edgesIgnoringSafeArea(.horizontal)
            .edgesIgnoringSafeArea(.bottom)
    }
}


extension Filter: Identifiable {}

//struct FiltersView_Previews: PreviewProvider {
//    static var previews: some View {
//        FiltersView()
//    }
//}
