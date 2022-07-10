//
//  StubDownloadsFeatureComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubDownloadsFeatureComponent: DownloadsFeatureComponent {
    func onShowSettings() {}
    
    var downloadsComponent: DownloadsComponent = StubDownloadsComponent()
    var playerComponent: PlayerComponent = StubPlayerComponent()
}
