//
//  ChipData.swift
//  iosApp
//
//  Created by Aleksey Blekot on 29.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

struct ChipData : Identifiable {
    let id = UUID()
    let title: String
    let filterName: String
    let optionValue: String
    let isClearAll: Bool
    
    init?(filter: Filter) {
        
        guard let option = filter.options.first(where: { $0.isSelected }) else { return nil }
        
        self.title = option.text
        self.filterName = filter.name
        self.optionValue = option.value
        self.isClearAll = false
    }
    
    init(title: String, clearAll: Bool) {
        self.title = title
        self.filterName = ""
        self.optionValue = ""
        self.isClearAll = true
    }
}
