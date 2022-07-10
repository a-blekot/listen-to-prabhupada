//
//  StubFavoritesComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubFavoritesComponent: FavoritesComponent {
    func onPlay(id: Int64) {}
    func onPause() {}
    func onFavorite(id: Int64, isFavorite: Bool) {}
    func onCurrentLecture(id: Int64, isPlaying: Bool) {}
    
    let flow: Value<FavoritesState> =
        valueOf(
            FavoritesState(
                lectures: [
                    mockLecture(1, isFavorite: true),
                    mockLecture(2, isFavorite: true),
                    mockLecture(3, isFavorite: true),
                    mockLecture(4, isFavorite: true),
                    mockLecture(5, isFavorite: true),
                    mockLecture(6, isFavorite: true),
                    mockLecture(7, isFavorite: true),
                    mockLecture(8, isFavorite: true),
                    mockLecture(9, isFavorite: true),
                    mockLecture(11, isFavorite: true),
                    mockLecture(12, isFavorite: true),
                    mockLecture(13, isFavorite: true),
                    mockLecture(14, isFavorite: true),
                    mockLecture(15, isFavorite: true),
                    mockLecture(16, isFavorite: true),
                    mockLecture(17, isFavorite: true),
                    mockLecture(18, isFavorite: true),
                    mockLecture(19, isFavorite: true),
                ]
            )
        )
}
