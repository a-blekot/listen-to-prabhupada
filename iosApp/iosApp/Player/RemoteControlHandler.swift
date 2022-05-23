//
//  RemoteControlHandler.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import MediaPlayer

extension Player {
    func playOrPause() -> PlayerStatus { return .none}
    func skipForward() {}
    func skipBackward() {}
    func updateNowPlaying(time: TimeInterval? = nil) {}
    func seek(toTime: TimeInterval) {}
}

class RemoteControlHandler {
    
    private let commandCenter = MPRemoteCommandCenter.shared()
    
    private let player: Player
    
    init(player: Player) {
        self.player = player
        
        setupRemoteControl()
    }
    
    private func setupRemoteControl() {
        
        setCommandCenterMode(mode: Defaults.commandCenterMode)
        
        commandCenter.togglePlayPauseCommand.addTarget(handler: {[weak self]  (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("togglePlayPauseCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            if strongSelf.player.playOrPause() != .none {
                return .success
            } else {
                return .commandFailed
            }
        })
        commandCenter.playCommand.addTarget { [weak self] event in
            debugPrint("playCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            strongSelf.player.play()
            return checkSatatus(strongSelf)
        }
        commandCenter.pauseCommand.addTarget { [weak self] event in
            debugPrint("pauseCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            strongSelf.player.pause()
            return checkSatatus(strongSelf)
        }
        commandCenter.stopCommand.addTarget(handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("stopCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            strongSelf.player.pause()
            return checkSatatus(strongSelf)
        })
        
        // next , prev
        commandCenter.nextTrackCommand.addTarget( handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("nextTrackCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            strongSelf.player.next()
            return checkSatatus(strongSelf)
        })
        commandCenter.previousTrackCommand.addTarget( handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("previousTrackCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            strongSelf.player.prev()
            return checkSatatus(strongSelf)
        })
        
        // seek fwd , seek bwd
        commandCenter.seekForwardCommand.addTarget(handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("seekForwardCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            debugPrint("Seek forward ")
            return .commandFailed
        })
        commandCenter.seekBackwardCommand.addTarget(handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("seekBackwardCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            debugPrint("Seek backward ")
            return .commandFailed
        })
        
        // skip fwd , skip bwd
        commandCenter.skipForwardCommand.addTarget(handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("skipForwardCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            strongSelf.player.skipForward()
            return checkSatatus(strongSelf)
        })
        commandCenter.skipBackwardCommand.addTarget(handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("skipBackwardCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            strongSelf.player.skipBackward()
            return checkSatatus(strongSelf)
        })
        
        // playback rate
        commandCenter.changePlaybackRateCommand.addTarget(handler: {[weak self] (_) -> MPRemoteCommandHandlerStatus in
            debugPrint("changePlaybackRateCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            debugPrint("Change Rate ")
            return .commandFailed
        })
        
        // seek to position
        commandCenter.changePlaybackPositionCommand.addTarget(handler: {[weak self] ( event) -> MPRemoteCommandHandlerStatus in
            debugPrint("changePlaybackPositionCommand")
            guard let strongSelf = self else {return .commandFailed}
            
            //   var playAfterSeek = strongSelf.status == .playing
            // strongSelf.pause()
            
            let e = event as! MPChangePlaybackPositionCommandEvent
            strongSelf.player.updateNowPlaying(time: e.positionTime)
            strongSelf.player.seek(toTime: e.positionTime)
            return checkSatatus(strongSelf)
            
        })
        
        // check status and return MPRemoteCommandHandlerStatus
        func checkSatatus(_ strongSelf: RemoteControlHandler) -> MPRemoteCommandHandlerStatus {
            if strongSelf.player.status != .none {
                return .success
            } else {
                return .commandFailed
            }
        }
        
        // Enters Background
        NotificationCenter.default.addObserver(forName: UIApplication.willResignActiveNotification, object: nil, queue: nil, using: {[weak self] (_) in
            debugPrint("willResignActiveNotification")
            guard let strongSelf = self else {return}
            
            strongSelf.player.updateNowPlaying()
        })
    }
    
    public func setCommandCenterMode(mode: RemoteControlMode) {
        commandCenter.skipBackwardCommand.isEnabled = mode == .skip
        commandCenter.skipForwardCommand.isEnabled = mode == .skip
        
        commandCenter.nextTrackCommand.isEnabled = mode == .nextprev
        commandCenter.previousTrackCommand.isEnabled = mode == .nextprev
        
        commandCenter.seekForwardCommand.isEnabled = false
        commandCenter.seekBackwardCommand.isEnabled = false
        commandCenter.changePlaybackRateCommand.isEnabled = false
    }
}
