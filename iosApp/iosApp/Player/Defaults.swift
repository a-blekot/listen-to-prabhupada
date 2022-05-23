//
//  Defaults.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

final class Defaults {
    static let skipIntervalInSeconds: TimeInterval = 15.0
    static let playbackRates: [Float] = [0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0]
    static let commandCenterMode = RemoteControlMode.skip
}
