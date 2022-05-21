//
//  Player.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.


import Foundation
import AVFoundation
import MediaPlayer
import Prabhupada

protocol Player {
    var playerBus: PlayerBus { get set }
}

class PlayerImpl: ObservableObject, Player {
    
    private let audioSession = AVAudioSession.sharedInstance()
    private var avQueuePlayer = AVQueuePlayer()
    
    var playerBus: PlayerBus
    
    private var currentPlaylist = [Lecture]()
    private var pendingPlaylist = [Lecture]()
    
    init (_ playerBus: PlayerBus) {
        self.playerBus = playerBus
        
        initSession()
        observePlayerBus()
    }
    
    func initSession() {
        
        setupNotifications()
        setupRemoteTransportControls()
        
        do {
            try audioSession.setCategory(AVAudioSession.Category.playback) // TODO check playAndRecord
            let _ = try audioSession.setActive(true)
        } catch let error as NSError {
            print("an error occurred when audio session category.\n \(error)")
        }
        
        avQueuePlayer.actionAtItemEnd = AVPlayer.ActionAtItemEnd.advance
    }
    
    func observePlayerBus() {
        playerBus.observeActions(onEach: observeActions)
        playerBus.observePlaylist(onEach: observePlaylist)
    }
    
    private func observeActions(_ action: PlayerAction) {
        print("IOs observeActions -> ")
        switch(action){
        case is PlayerActionPlay: play((action as! PlayerActionPlay).lectureId)
        case is PlayerActionPause: pause()
        default: print("default")
        }
    }
    
    private func observePlaylist(_ playlist: [Lecture]) {
        print("IOs observePlaylist -> ")
        playlist.forEach { lecture in
            print("lecture id = \(lecture.id)")
        }
        
        pendingPlaylist = playlist
        maybeUpdateCurrentPlaylist(playlist)
    }
    
    private func maybeUpdateCurrentPlaylist(_ playlist: [Lecture]) {
        if (currentPlaylist.isEmpty) {
            currentPlaylist = playlist
            resetTracks(currentPlaylist)
            return
        }
        
        handleNewList(playlist)
    }
    
    private func handleNewList(_ playlist: [Lecture]) {
        let old = currentPlaylist.map { $0.id }
        let new = playlist.map { $0.id }
        
        if (old.elementsEqual(new)) {
            currentPlaylist = playlist
        } else {
            checkRemovedItem(old, new)
        }
    }
    
    private func checkRemovedItem(_ old: [Int64], _ new: [Int64]) {
        if (old.isEmpty || new.isEmpty) { return }
        
        let removedIds = old.filter { !new.contains($0) }
        if (removedIds.count == old.count) {
            return
        } else if (removedIds.count == 1) {
            if let index = old.firstIndex(of: removedIds[0]) {
                removeMediaItem(index)
            }
        }
    }
    
    private var currentMediaItemIndex: Int? {
        get {
            if let item = avQueuePlayer.currentItem {
                return avQueuePlayer.items().firstIndex(of: item)
            }
        
            return nil
        }
    }
    
    private func removeMediaItem(_ index: Int) {
        if (currentPlaylist.isEmpty) {
            return
        }
        
        if (currentMediaItemIndex == index) {
            pause()
        }
        
        if (currentPlaylist.indices.contains(index)) {
            currentPlaylist.remove(at: index)
            removeMediaItemFromPlayer(index)
        }
    }
    
    private func removeMediaItemFromPlayer(_ index: Int) {
        if (avQueuePlayer.items().indices.contains(index)) {
            avQueuePlayer.remove(avQueuePlayer.items()[index])
        }
    }
    
    private func resetTracks(_ lectures: [Lecture]) {
        pause()
        avQueuePlayer = AVQueuePlayer(items: lectures.map { createMediaItem(from: $0) })
    }
    
    private func createMediaItem(from lecture: Lecture) -> AVPlayerItem {
        
        let fileURL = NSURL(string: lecture.remoteUrl)
        let avAsset = AVAsset(url: fileURL! as URL)
        let assetKeys = ["playable"]
        let playerItem = AVPlayerItem(asset: avAsset, automaticallyLoadedAssetKeys: assetKeys)
        
//        let a = avQueuePlayer.currentItem?.externalMetadata[0]
//        a.
        
        return playerItem
        
//        val metaData = MediaMetadata.Builder()
//            .setDescription(displayedDescription)
//            .setTitle(title)
//            .build()
//
//        return MediaItem.Builder()
//            .setUri(fileUrl?.ifEmpty { null } ?: remoteUrl.toValidUrl())
//            .setMediaId("$id")
//            .setMediaMetadata(metaData)
//            .build()
    }
    
    func pause() {
        print("Pause")
        avQueuePlayer.pause()
    }
    
    
    func play(_ id: Int64? = nil) {
        print("IOs Player Play")
        avQueuePlayer.pause()
        
        guard let lecture = currentPlaylist.first(where: { $0.id == id }) else {
            avQueuePlayer.play()
            return
        }
        
        let item = createMediaItem(from: lecture)
        
        if (avQueuePlayer.currentItem != item) {
            avQueuePlayer.replaceCurrentItem(with: item)
        }
        
        avQueuePlayer.play()
    }
    
    //    func playSongWithId(_ songId:NSNumber, title:String, artist:String) {
    //        MusicQuery().queryForSongWithId(songId, completionHandler: {[weak self] (result:MPMediaItem?) -> Void in
    //            if let nonNilResult = result {
    //                if let assetUrl = nonNilResult.value(forProperty:MPMediaItemPropertyAssetURL) as? URL {
    //                    let avSongItem = AVPlayerItem(url: assetUrl)
    //                    self!.avQueuePlayer.insert(avSongItem, after: nil)
    //                    self!.play()
    //                    //display now playing info on control center
    //                    MPNowPlayingInfoCenter.default().nowPlayingInfo = [MPMediaItemPropertyTitle: title, MPMediaItemPropertyArtist: artist]
    //                } else {
    //                    print("assetURL for song \(songId) does not exist")
    //                }
    //            }
    //        })
    //
    //    }
    
    //    func songIsAvailable(songId:NSNumber, completion:((Bool)->Void)? = nil)
    //    {
    //        MusicQuery().queryForSongWithId(songId, completionHandler: {(result:MPMediaItem?) -> Void in
    //            if let nonNilResult = result {
    //                if let _ = nonNilResult.value(forProperty:MPMediaItemPropertyAssetURL) as? URL {
    //                    completion?(true)
    //                } else {
    //                    completion?(false)
    //                }
    //            }
    //        })
    //    }
    
    
    private func setupRemoteTransportControls() {
        let commandCenter = MPRemoteCommandCenter.shared()
        
        commandCenter.playCommand.isEnabled = true
        commandCenter.playCommand.addTarget { [unowned self] event in
            if avQueuePlayer.rate == 0.0 {
                self.play()
                return .success
            }
            return .commandFailed
        }
        
        commandCenter.pauseCommand.isEnabled = true
        commandCenter.pauseCommand.addTarget { [unowned self] event in
            if avQueuePlayer.rate > 0.0 {
                self.pause()
                return .success
            }
            return .commandFailed
        }
    }
    
    
    private func setupNowPlaying() {
        // Define Now Playing Info
        var nowPlayingInfo = [String : Any]()
        nowPlayingInfo[MPMediaItemPropertyTitle] = "My Movie"
        if let image = UIImage(named: "lockscreen") {
            nowPlayingInfo[MPMediaItemPropertyArtwork] =
            MPMediaItemArtwork(boundsSize: image.size) { size in
                return image
            }
        }
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = avQueuePlayer.currentItem?.currentTime().seconds
        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = avQueuePlayer.currentItem?.asset.duration.seconds
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = avQueuePlayer.rate
        // Set the metadata
        MPNowPlayingInfoCenter.default().nowPlayingInfo = nowPlayingInfo
    }
    
    //    func remoteControlReceivedWithEvent(_ receivedEvent:UIEvent)  {
    //        if (receivedEvent.type == .remoteControl) {
    //            switch receivedEvent.subtype {
    //            case .remoteControlTogglePlayPause:
    //                if avQueuePlayer.rate > 0.0 {
    //                    pause()
    //                } else {
    //                    play()
    //                }
    //            case .remoteControlPlay:
    //                play()
    //            case .remoteControlPause:
    //                pause()
    //            default:
    //                print("received sub type \(receivedEvent.subtype) Ignoring")
    //            }
    //        }
    //    }
    
    private func setupNotifications() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(PlayerImpl.songEnded),
            name: .AVPlayerItemDidPlayToEndTime,
            object: avQueuePlayer.currentItem
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(PlayerImpl.handleRouteChange),
            name: AVAudioSession.routeChangeNotification,
            object: audioSession
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(PlayerImpl.handleInterruption),
            name: AVAudioSession.interruptionNotification,
            object: audioSession
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(PlayerImpl.handleAudioServiceReset),
            name: AVAudioSession.mediaServicesWereResetNotification,
            object: audioSession
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(PlayerImpl.handleSecondaryAudio),
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



//var audioPlayer: AVPlayer?
//
//play(URL(string: url))
//
//func play(_ url: URL?) {
//    guard let url = url else { return }
//
//    //            let url = URL(string: "https://s3.amazonaws.com/kargopolov/kukushka.mp3")
//    let playerItem:AVPlayerItem = AVPlayerItem(url: url)
//
//    do {
//        audioPlayer = try AVPlayer(playerItem: playerItem)
//        audioPlayer?.play()
//    } catch let error as NSError {
//        print("Unresolved error \(error.debugDescription)")
//    }
//}
