//
//  PlayerStatus.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

enum PlayerStatus: Int {
    case none
    case loading
    case failed
    case readyToPlay
    case playing
    case paused
}
