//
//  StubRootComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.08.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubRootComponent : RootComponent {

    let childStack: Value<ChildStack<AnyObject, RootComponentChild>> =
        simpleChildStack(RootComponentChildResults(component: StubResultsComponent()))
    
    let playerComponent: PlayerComponent = StubPlayerComponent()
    
    func onResultsTabClicked() {}
    func onFavoritesTabClicked() {}
    func onDownloadsTabClicked() {}
    func onFiltersTabClicked() {}
    func onSearchTabClicked() {}
    func onSettingsTabClicked() {}
}
