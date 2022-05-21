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
            Text(String(buttonType.rawValue))
        }
            .disabled(!pagination.canAdd(buttonType))
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
    
    class StubLecturesComponent : LecturesComponent {
        func onCurrentLecture(id: Int64, isPlaying: Bool) {
            
        }
        
        func onDownload(id: Int64) {
            
        }
        
        func onFavorite(id: Int64, isFavorite: Bool) {
            
        }
        
        func onNext() {
            
        }
        
        func onPage(page: Int32) {
            
        }
        
        func onPause() {
            
        }
        
        func onPlay(id: Int64) {
            
        }
        
        func onPrev() {
            
        }
        
        func onSeekBack() {
            
        }
        
        func onSeekForward() {
            
        }
        
        func onSeekTo(timeMs: Int64) {
            
        }
        
        func onSliderReleased() {
            
        }
        
        func onUpdateFilters() {
            
        }
        
        var flow: Value<LecturesState> =
            valueOf(
                LecturesState(
                    isLoading: false,
                    lectures: [mockLecture(1)],
                    pagination: mockPagination(5, 100)
                )
            )
    }
}
