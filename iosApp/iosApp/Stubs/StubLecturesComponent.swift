//
//  StubLecturesComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 16.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

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
                lectures: [
                    mockLecture(1),
                    mockLecture(2),
                    mockLecture(3),
                    mockLecture(4),
                    mockLecture(5),
                    mockLecture(6),
                    mockLecture(7),
                    mockLecture(8),
                    mockLecture(9),
                    mockLecture(11),
                    mockLecture(12),
                    mockLecture(13),
                    mockLecture(14),
                    mockLecture(15),
                    mockLecture(16),
                    mockLecture(17),
                    mockLecture(18),
                    mockLecture(19),
                ],
                pagination: mockPagination(10, 100)
            )
        )
}
