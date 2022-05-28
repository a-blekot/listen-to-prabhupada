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
    @EnvironmentObject var theme: Theme
    @State private var isExpanded: Bool
    
    let filter: Filter
    let component: FiltersComponent
    
    init(filter: Filter, component: FiltersComponent) {
        self.filter = filter
        self.component = component
        _isExpanded = State(initialValue: filter.isExpanded)
    }

    var body: some View {
        
        VStack {
            FilterTitleView(
                filter: filter,
                isExpanded: isExpanded,
                onExpandedChanged: { isExpanded in
                    self.isExpanded = isExpanded
                    component.onFilterExpanded(filterName: filter.name, isExpanded: isExpanded)
                }
            )
            if isExpanded {
                VStack(alignment: .leading) {
                    ForEach(filter.options) { option in
                        OptionView(option: option, onOptionSelected: { isSelected in
                            component.onQueryParam(
                                queryParam: QueryParam(
                                    filterName: filter.name,
                                    selectedOption: option.value,
                                    isSelected: isSelected
                                )
                            )
                        })
                    }
                }
                .transition(
                    .move(edge: .top)
                        .combined(with: .scale(scale: 0.1, anchor: .top))
                )
            }
        }
    }
}

extension Option: Identifiable {}

struct FilterItemView_Previews: PreviewProvider {
    static var previews: some View {
        FilterItemView(filter: mockFilter("City"), component: StubFiltersComponent())
            .previewLayout(PreviewLayout.fixed(width: 500, height: 50))
            .previewDisplayName("Filter preview")
            .environmentObject(themes[0])
    }
}
