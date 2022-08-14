//
//  SimplePageControl.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.08.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct SimplePageControlView: View {
    
    @EnvironmentObject var theme: Theme
    let pagination: Pagination
    let component : ResultsComponent
    
    var body: some View {
        HStack(alignment: .center, spacing: theme.dimens.paddingXS) {
            button(.prev_1)
                .environmentObject(theme)
                .frame(width: .infinity)
            
            Text("\(pagination.curr) из \(pagination.total)")
                .font(theme.titleSmall)
                .foregroundColor(theme.colors.tertiary)
                .lineLimit(1)
                .frame(width: 100)
            
            button(.next_1)
                .environmentObject(theme)
                .frame(width: .infinity)
        }
        .frame(height: theme.dimens.buttonHeight)
    }
    
    private func button(_ buttonType: ButtonType) -> SimplePageButtonView {
        return SimplePageButtonView(buttonType, pagination, component)
    }
}
