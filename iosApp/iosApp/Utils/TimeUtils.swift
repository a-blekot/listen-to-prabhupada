//
//  TimeUtils.swift
//  iosApp
//
//  Created by Aleksey Blekot on 26.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada
import AVFoundation

let MS_IN_SECOND_64: Int64 = 1000
let MS_IN_SECOND: Int32 = 1000
let SEEK_TIME = CMTime(value: 10, timescale: 1)

extension PlayerState {
    var displayedTime: String {
        "\(TimeInterval(timeMs/MS_IN_SECOND_64).hoursMinutesSeconds()) / \(TimeInterval(durationMs/MS_IN_SECOND_64).hoursMinutesSeconds())"
    }
}

extension TimeInterval{
    func hoursMinutesSeconds() -> String {
        let time = NSInteger(self)

//        let ms = Int((self.truncatingRemainder(dividingBy: 1)) * MS_IN_SECOND)
        let seconds = time % 60
        let minutes = (time / 60) % 60
        let hours = (time / 3600)

        return String(format: "%0.2d:%0.2d:%0.2d",hours,minutes,seconds)

    }
}
