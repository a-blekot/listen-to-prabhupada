//
//  FilterItemView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FilterItemView: View {
    let filter: Filter
    let component: FiltersComponent

    var body: some View {
        Text(filter.title)
    }
}

//struct FilterItemView_Previews: PreviewProvider {
//    static var previews: some View {
//        FilterItemView()
//    }
//}
