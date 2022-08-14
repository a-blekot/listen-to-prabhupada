//
//  FilterTitleView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 20.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FilterTitleView: View {
    
    @EnvironmentObject var theme: Theme
    
    let filter: Filter
    let isExpanded: Bool
    let onExpandedChanged: (Bool) -> ()
    
    init(filter: Filter, isExpanded: Bool, onExpandedChanged: @escaping (Bool) -> ()) {
        self.filter = filter
        self.isExpanded = isExpanded
        self.onExpandedChanged = onExpandedChanged
    }
    
    var body: some View {
        HStack {
            Text(filter.title)
                .font(.system(size: 18))
                .foregroundColor(theme.colors.filtersText)
                .padding(.horizontal, theme.dimens.paddingM)
            
            Spacer()
            
            Button {
                onExpandedChanged(!isExpanded)
            } label: {
                Label("Options", systemImage: isExpanded ? "minus" : "plus")
                    .labelStyle(.iconOnly)
                    .imageScale(.large)
                    .foregroundColor(theme.colors.filtersCountText)
                    .padding(.horizontal, theme.dimens.paddingM)
            }
            
        }
        .frame(height: theme.dimens.rowHeightL)
        .contentShape(RoundedRectangle(cornerRadius: theme.dimens.radiusS))
        .background(RoundedRectangle(cornerRadius: theme.dimens.radiusS).fill(theme.colors.filtersCategory))
        .onTapGesture { onExpandedChanged(!isExpanded) }
    }
}

struct FilterTitleView_Previews: PreviewProvider {
    static var previews: some View {
        FilterTitleView(filter: mockFilter("Years"), isExpanded: true, onExpandedChanged: {_ in})
            .previewLayout(PreviewLayout.fixed(width: 500, height: 50))
            .previewDisplayName("Filter preview")
            .environmentObject(themes[0])
    }
}
