//
//  StubFavoritesComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubFavoritesFeatureComponent: FavoritesFeatureComponent {
    func onShowSettings() {}

    var favoritesComponent: FavoritesComponent = StubFavoritesComponent()

    var playerComponent: PlayerComponent = StubPlayerComponent()
}
