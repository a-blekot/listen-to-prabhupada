//
//  StubPlayerComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 16.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubPlayerComponent: PlayerComponent {
    func onDownload(lecture: Lecture) {}
    func onNext() {}
    func onPause() {}
    func onPlay(id: Int64) {}
    func onPrev() {}
    func onSeekBack() {}
    func onSeekForward() {}
    func onSeekTo(timeMs: Int64) {}
    func onSliderReleased() {}
    func onSpeed(speed: Float) {}
    
    var flow: Value<PlayerState> = valueOf(
        mockPlayerState()
    )
}
