//
//  StubLectureComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.08.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubLectureComponent: LectureComponent {
    func onPause() {}
    func onPlay(id: Int64) {}
    func onFavorite(id: Int64, isFavorite: Bool) {}
    func onCurrentLecture(id: Int64, isPlaying: Bool) {}
}
