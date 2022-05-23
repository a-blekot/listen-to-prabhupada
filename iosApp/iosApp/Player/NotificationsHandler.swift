//
//  NotificationsHandler.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import AVFoundation

class NotificationsHandler: NSObject {
    
    private let player: Player
    private let audioSession = AVAudioSession.sharedInstance()
    
    init(player: Player) {
        self.player = player
        super.init()
        
        setupNotifications()
    }
    
    private func setupNotifications() {
//        NotificationCenter.default.addObserver(
//            self,
//            selector: #selector(songEnded),
//            name: .AVPlayerItemDidPlayToEndTime,
//            object: player.currentItem
//        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(NotificationsHandler.handleRouteChange),
            name: AVAudioSession.routeChangeNotification,
            object: audioSession
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(NotificationsHandler.handleInterruption),
            name: AVAudioSession.interruptionNotification,
            object: audioSession
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(NotificationsHandler.handleAudioServiceReset),
            name: AVAudioSession.mediaServicesWereResetNotification,
            object: audioSession
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(NotificationsHandler.handleSecondaryAudio),
            name: AVAudioSession.silenceSecondaryAudioHintNotification,
            object: audioSession
        )
    }
    
    @objc class func songEnded(_ notification: Notification)
    {
        print("song ended: \(notification)")
    }
    
    @objc class func handleRouteChange(_ notification: Notification) {
        print("handleRouteChange: \(notification)")
        
        guard let userInfo = notification.userInfo,
              let reasonValue = userInfo[AVAudioSessionRouteChangeReasonKey] as? UInt,
              let reason = AVAudioSession.RouteChangeReason(rawValue: reasonValue) else { return }
        
        var headphonesConnected: Bool = false
        
        switch reason {
        case .newDeviceAvailable:
            for output in AVAudioSession.sharedInstance().currentRoute.outputs where output.portType == AVAudioSession.Port.headphones {
                headphonesConnected = true
            }
        case .oldDeviceUnavailable:
            // Handle old device removed.
            // Pause
            if let previousRoute =
                userInfo[AVAudioSessionRouteChangePreviousRouteKey] as? AVAudioSessionRouteDescription {
                for output in previousRoute.outputs where output.portType == AVAudioSession.Port.headphones {
                    headphonesConnected = false
                }
            }
            
        default: ()
        }
        
        debugPrint("headphonesConnected == \(headphonesConnected)")
    }
    
    @objc class func handleInterruption(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
              let typeValue = userInfo[AVAudioSessionInterruptionTypeKey] as? UInt,
              let type = AVAudioSession.InterruptionType(rawValue: typeValue) else { return }
        
        if type == .began {
            // Interruption began, take appropriate actions (save state, update user interface)
        }
        else if type == .ended {
            guard let optionsValue =
                    userInfo[AVAudioSessionInterruptionOptionKey] as? UInt else {
                        return
                    }
            let options = AVAudioSession.InterruptionOptions(rawValue: optionsValue)
            if options.contains(.shouldResume) {
                // Interruption Ended - playback should resume
            }
        }
    }
    
    
    
    @objc class func handleAudioServiceReset(_ notification: Notification) {
        print("AudioServiceReset: \(notification)")
        //        * Dispose of orphaned audio objects (such as players, recorders, converters, or audio queues) and create new ones
        //        * Reset any internal audio states being tracked, including all properties of AVAudioSession
        //        * When appropriate, reactivate the AVAudioSession instance using the setActive:error: method
    }
    
    @objc class func handleSecondaryAudio(_ notification: Notification) {
        // Determine hint type
        guard let userInfo = notification.userInfo,
              let typeValue = userInfo[AVAudioSessionSilenceSecondaryAudioHintTypeKey] as? UInt,
              let type = AVAudioSession.SilenceSecondaryAudioHintType(rawValue: typeValue) else {return}
        
        if type == .begin {
            // Other app audio started playing - mute secondary audio
        } else {
            // Other app audio stopped playing - restart secondary audio
        }
        
    }
}
