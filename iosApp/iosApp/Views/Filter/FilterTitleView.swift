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
            Button {
                withAnimation(.easeOut(duration: 0.3)) {
                    onExpandedChanged(!isExpanded)
                }
            } label: {
                Label("Options", systemImage: "chevron.right.circle")
                    .labelStyle(.iconOnly)
                    .imageScale(.large)
                    .rotationEffect(.degrees(isExpanded ? 90 : 0))
                    .scaleEffect(isExpanded ? 1.5 : 1)
                    .foregroundColor(theme.bodyTextColor)
                    .padding()
            }
            
            Spacer()
            
            Text(filter.title)
                .font(theme.buttonFont)
                .foregroundColor(theme.bodyTextColor)
            
            Spacer()
            
        }
        .background(theme.buttonDarkBacground)
        .padding(.bottom, isExpanded ? 2 : 0)
        
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
