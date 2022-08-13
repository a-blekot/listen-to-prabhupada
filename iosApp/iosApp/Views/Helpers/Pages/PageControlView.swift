//
//  PageControlView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct PageControlView: View {
    
    @EnvironmentObject var theme: Theme
    let pagination: Pagination
    let component : ResultsComponent
    
    var body: some View {
        HStack(alignment: .center, spacing: 6) {
            button(.first)
            button(.prev_20)
            button(.prev_5)
            button(.prev_1)
            
            Text("\(pagination.curr) из \(pagination.total)")
                .font(theme.titleSmall)
                .foregroundColor(theme.colors.tertiary)
                .lineLimit(1)
                .padding(.horizontal, 2)
                .frame(width: 75)
            
            button(.next_1)
            button(.next_5)
            button(.next_20)
            button(.last)
        }
    }
    
    private func button(_ buttonType: ButtonType) -> PageButtonView {
        return PageButtonView(buttonType, pagination, component)
    }
}

struct PageControlView_Previews: PreviewProvider {
    static var previews: some View {
        PageControlView(pagination: mockPagination(2, 100), component: StubResultsComponent())
            .environmentObject(themes[0])
    }
}
