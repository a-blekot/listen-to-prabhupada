//
//  StubDownloadsComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubDownloadsComponent : DownloadsComponent {
    func onPlay(id: Int64) {}
    func onPause() {}
    func onFavorite(id: Int64, isFavorite: Bool) {}
    func onCurrentLecture(id: Int64, isPlaying: Bool) {}
    
    let flow: Value<DownloadsState> =
        valueOf(
            DownloadsState(
                lectures: [
                    mockLecture(1, isFavorite: true),
                    mockLecture(2, isFavorite: true),
                    mockLecture(3),
                    mockLecture(4, isFavorite: true),
                    mockLecture(5),
                    mockLecture(6),
                    mockLecture(7),
                    mockLecture(8),
                    mockLecture(9),
                    mockLecture(11, isFavorite: true),
                    mockLecture(12, isFavorite: true),
                    mockLecture(13),
                    mockLecture(14),
                    mockLecture(15),
                    mockLecture(16),
                    mockLecture(17),
                    mockLecture(18),
                    mockLecture(19, isFavorite: true),
                ]
            )
        )
}

