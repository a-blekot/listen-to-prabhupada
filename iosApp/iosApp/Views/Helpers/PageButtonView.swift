//
//  PageButtonView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

enum ButtonType : Int32 {
    case first = 0
    case prev_20 = -20
    case prev_5 = -5
    case prev_1 = -1
    case next_1 = 1
    case next_5 = 5
    case next_20 = 20
    case last = 100500
}

struct PageButtonView: View {
    let buttonType: ButtonType
    let pagination: Pagination
    let component: LecturesComponent
    
    init(_ buttonType: ButtonType, _ pagination: Pagination, _ component: LecturesComponent) {
        self.buttonType = buttonType
        self.pagination = pagination
        self.component = component
    }
    
    var body: some View {
        Button {
            component.onPage(page: pagination.nextPage(buttonType))
        } label: {
            Image(systemName: icon(for: buttonType))
                .resizable(resizingMode: .tile)
                .frame(width: SIZE_FAVORITE_BUTTON, height: SIZE_FAVORITE_BUTTON)
                .fixedSize()
                .foregroundColor(.orange)
        }
        .disabled(!pagination.canAdd(buttonType))
    }
    
    private func icon(for buttonType: ButtonType) -> String {
        switch buttonType {
        case .first: return "backward.end"
        case .prev_20: return "chevron.backward.square"
        case .prev_5: return "chevron.backward.2"
        case .prev_1: return "chevron.backward"
        case .next_1: return "chevron.forward"
        case .next_5: return "chevron.forward.2"
        case .next_20: return "chevron.forward.square"
        case .last: return "forward.end"
        }
    }
}

extension Pagination {
    func canAdd(_ buttonType: ButtonType) -> Bool {
        switch buttonType {
        case .first: return curr > 1
        case .last: return curr < total
        default: return canAdd(pages: buttonType.rawValue)
        }
    }
    
    func nextPage(_ buttonType: ButtonType) -> Int32 {
        switch buttonType {
        case .first: return 1
        case .last: return total
        default: return add(pages: buttonType.rawValue)
        }
    }
}

struct PageButtonView_Previews: PreviewProvider {
    static var previews: some View {
        PageButtonView(.next_5, mockPagination(5, 100), StubLecturesComponent())
            .environmentObject(themes[0])
    }
}
