//
//  StubLecturesComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 16.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubResultsComponent: ResultsComponent {
    func onPage(page: Int32) {}
    func onFavorite(id: Int64, isFavorite: Bool) {}
    func onCurrentLecture(id: Int64, isPlaying: Bool) {}
    func onPause() {}
    func onPlay(id: Int64) {}

    let flow: Value<ResultsState> =
        valueOf(
            ResultsState(
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
                pagination: mockPagination(10, 100),
                useSimplePageView: true
            )
        )
}
