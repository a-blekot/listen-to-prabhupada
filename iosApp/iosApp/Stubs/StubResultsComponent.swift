//
//  StubResultsComponent.swift
//  iosApp
//
//  Created by Aleksey Blekot on 16.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

class StubResultsComponent: ResultsComponent {
    
    func onEditFilters() {}
    
    func onShowDownloads() {}
    
    func onShowFavorites() {}
    
    func onShowSettings() {}
    
    func onUpdateFilters() {}
    
    var lecturesComponent: LecturesComponent = StubLecturesComponent()
    
    var playerComponent: PlayerComponent = StubPlayerComponent()
}
