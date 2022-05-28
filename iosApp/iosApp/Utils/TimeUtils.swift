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
    
    var progress: CGFloat {
        CGFloat(timeMs) / CGFloat(durationMs)
    }
    
    func timeMs(progress: CGFloat) -> Int64 {
        var progress = progress
        if (progress < 0) {
            progress = 0
        } else if (progress > 1) {
            progress = 1
        }
        
        return Int64(CGFloat(durationMs) * progress)
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

class Utils {
    private static var timeHMSFormatter: DateComponentsFormatter = {
        let formatter = DateComponentsFormatter()
        formatter.unitsStyle = .positional
        formatter.allowedUnits = [.minute, .second]
        formatter.zeroFormattingBehavior = [.pad]
        return formatter
    }()
    
    static func formatMinutesSeconds(timeMs: Int64) -> String {
        guard timeMs >= 0, let text = timeHMSFormatter.string(from: Double(timeMs / 1000)) else {
            return "00:00"
        }
        
        return text
    }
}

