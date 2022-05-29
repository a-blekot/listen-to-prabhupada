//
//  SelectedFilters.swift
//  iosApp
//
//  Created by Aleksey Blekot on 29.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct SelectedFilters: View {
    
    let chips: [ChipData]
    let component: FiltersComponent
    var hasSelectedFilters = false
    @State private var totalHeight = CGFloat(100)
    
    var body: some View {
        var width = CGFloat.zero
        var height = CGFloat.zero
        
        VStack {
            GeometryReader { geo in
                ZStack(alignment: .topLeading) {
                    ForEach(chips) { chipData in
                        chip(chipData)
                            .padding(3)
                            .alignmentGuide(.leading) { dimension in
                                
                                if (abs(width - dimension.width) > geo.size.width) {
                                    width = 0
                                    height -= dimension.height
                                }
                                
                                let result = width
                                if chipData.isClearAll {
                                    width = 0
                                } else {
                                    width -= dimension.width
                                }
                                return result
                            }
                            .alignmentGuide(.top) { dimension in
                                
                                debugPrint("title = \(chipData.title), w = \(width), h = \(height)")
                                
                                let result = height
                                if chipData.isClearAll {
                                    height = 0
                                }
                                return result
                            }
                    }
                }
                .background(GeometryReader {gp -> Color in
                    DispatchQueue.main.async {
                        // update on next cycle with calculated height of ZStack !!!
                        self.totalHeight = gp.size.height
                    }
                    return Color.clear
                })
            }
        }
        .frame(height: totalHeight)
    }
    
    private func chip(_ chipData: ChipData) -> some View {
        if chipData.isClearAll {
            return Chips(
                text: chipData.title,
                onClick: {
                    component.onClearAll()
                }
            )
        } else {
            return Chips(
                text: chipData.title,
                onClick: {
                    component.onQueryParam(
                        queryParam: QueryParam(
                            filterName: chipData.filterName,
                            selectedOption: chipData.optionValue,
                            isSelected: false
                        )
                    )
                }
            )
        }
    }
}

struct SelectedFilters_Previews: PreviewProvider {
    static var previews: some View {
        FiltersView(StubFiltersComponent())
            .environmentObject(themes[0])
    }
}
