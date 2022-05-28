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
    let component : LecturesComponent
    
    var body: some View {
        HStack(alignment: .center, spacing: 20) {
            button(.first)
            button(.prev_20)
            button(.prev_5)
            button(.prev_1)
            
            Text("\(pagination.curr) из \(pagination.total)")
                .font(theme.bodyFont)
                .foregroundColor(theme.bodyTextColor)
                .lineLimit(1)
                .padding(.horizontal, 2)
                .frame(width: 80)
            
            button(.next_1)
            button(.next_5)
            button(.next_20)
            button(.last)
        }
        .padding(10)
        .frame(maxWidth: .infinity, minHeight: 40)
        .border(.orange, width: 2)
    }
    
    private func button(_ buttonType: ButtonType) -> PageButtonView {
        return PageButtonView(buttonType, pagination, component)
    }
}

struct PageControlView_Previews: PreviewProvider {
    static var previews: some View {
        PageControlView(pagination: mockPagination(10, 100), component: StubLecturesComponent())
            .environmentObject(themes[0])
    }
}
