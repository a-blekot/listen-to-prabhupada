//
//  StubFiltersComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 16.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubFiltersComponent: FiltersComponent {
    func onApplyChanges() {}
    func onClearAll() {}
    func onFilterExpanded(filterName: String, isExpanded: Bool) {}
    func onQueryParam(queryParam: QueryParam) {}
    
    var models: Value<FiltersState> = valueOf(
        FiltersState(
            isLoading: false,
            filters: [
                mockFilter("City"),
                mockFilter("Year"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
                mockFilter("Scripture"),
            ],
            totalLecturesCount: 900,
            pagesCount: 100
        )
    )
}
