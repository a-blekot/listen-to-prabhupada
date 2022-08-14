//
//  SimplePageButton±View.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.08.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct SimplePageButtonView: View {
    @EnvironmentObject var theme: Theme
    
    let buttonType: ButtonType
    let pagination: Pagination
    let component: ResultsComponent
    
    init(_ buttonType: ButtonType, _ pagination: Pagination, _ component: ResultsComponent) {
        self.buttonType = buttonType
        self.pagination = pagination
        self.component = component
    }
    
    var body: some View {
        let enabled = pagination.canAdd(buttonType)
        Button {
            component.onPage(page: pagination.nextPage(buttonType))
        } label: {
            Text(text(for: buttonType))
                .lineLimit(1)
                .font(theme.titleSmall)
                .foregroundColor(theme.colors.tertiary)
                .padding(.horizontal, theme.dimens.paddingM)
        }
        .frame(height: theme.dimens.buttonHeight)
        .background(enabled ? theme.colors.btnPages : Color.clear)
        .overlay(
            RoundedRectangle(cornerRadius: 25)
                .stroke(theme.colors.primary, lineWidth: 2)
        )
        .cornerRadius(25)
        .disabled(!enabled)
    }
    
    private func text(for buttonType: ButtonType) -> String {
        switch buttonType {
        case .prev_1: return "Предыдущая"
        case .next_1: return "Следующая"
        default: return "Unknown"
        }
    }
}
