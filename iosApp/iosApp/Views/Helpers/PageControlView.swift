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
    
    let pagination: Pagination
    let component : LecturesComponent
    
    
    var body: some View {
        HStack(alignment: .center) {
            button(.first)
            button(.prev_20)
            button(.prev_5)
            button(.prev_1)
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
        PageControlView(pagination: mockPagination(10, 100), component: StubLecturesComponent())
    }
    
    class StubLecturesComponent: LecturesComponent {
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
        
        let flow: Value<LecturesState> =
            valueOf(
                LecturesState(
                    isLoading: false,
                    lectures: [mockLecture(1)],
                    pagination: mockPagination(10, 100)
                )
            )
    }
}
