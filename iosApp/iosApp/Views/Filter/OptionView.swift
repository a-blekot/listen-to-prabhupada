//
//  OptionView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 20.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct OptionView: View {
    
    @EnvironmentObject var theme: Theme
    
    let option: Option
    let onOptionSelected: (Bool) -> ()
    
    var body: some View {
        HStack {
            Text(option.text)
                .font(theme.titleMedium)
                .foregroundColor(theme.colors.filtersText)
                .padding(.leading)
            
            Spacer()
            
        }
        .contentShape(Rectangle())
        .onTapGesture { onOptionSelected(!option.isSelected) }
        .frame(maxWidth: .infinity, minHeight: 50)
        .background(option.isSelected ? theme.colors.filtersSelected : theme.colors.filtersNeutral)
        .padding(.horizontal, 20)
        .padding(.bottom, 2)
    }
}

struct OptionView_Previews: PreviewProvider {
    static var previews: some View {
        OptionView(option: mockOption("New York", isSelected: true), onOptionSelected: {_ in})
            .previewLayout(PreviewLayout.fixed(width: 450, height: 50))
            .previewDisplayName("Filter preview")
            .environmentObject(themes[0])
    }
}
