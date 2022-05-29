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
    let page: Int
    let pagination: Pagination
    let component : LecturesComponent
    
    var body: some View {
        HStack(alignment: .center, spacing: 6) {
            button(.first)
            button(.prev_20)
            button(.prev_5)
            button(.prev_1)
            
            Text("\(page) из \(pagination.total)")
                .font(theme.bodyFont)
                .foregroundColor(theme.bodyTextColor)
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
        PageControlView(page: 1, pagination: mockPagination(100, 100), component: StubLecturesComponent())
            .environmentObject(themes[0])
    }
}
