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

class Player: ObservableObject {
    private let audioSession = AVAudioSession.sharedInstance()
    private let player = AVQueuePlayer()
    
    private var lectures = [Lecture]()
    private var currentPlaylist = [PlayerItem]()
    private var pendingPlaylist = [PlayerItem]()
    
    private var remoteControlHandler: RemoteControlHandler? = nil
    private var notificationsHandler: NotificationsHandler? = nil
    private var periodicTimeObserver: PeriodicTimeObserver? = nil
    private var playOnSliderRelease: Bool? = nil
    
    private let playerBus: PlayerBus
    private let savedPositionProvider: SavedPositionProvider
    
    private var isSessionSetup = false
    
    var status: PlayerStatus = .none {
        didSet {
            // self.delegate?.aQPlayerManager(self, statusDidChange: status)
        }
    }
    
    init (_ playerBus: PlayerBus, _ savedPositionProvider: SavedPositionProvider) {
        self.playerBus = playerBus
        self.savedPositionProvider = savedPositionProvider
        
        initPlayer()
        initHandlers()
        observePlayerBus()
    }
    
    deinit {
        UIApplication.shared.endReceivingRemoteControlEvents()
        self.clean()
        periodicTimeObserver = nil
        notificationsHandler = nil
        remoteControlHandler = nil
    }
    
    private func clean() {
        player.pause()
        player.removeAllItems()
        lectures.removeAll()
        currentPlaylist.removeAll()
        pendingPlaylist.removeAll()
    }
    
    private func initPlayer() {
        player.actionAtItemEnd = AVPlayer.ActionAtItemEnd.advance
    }
    
    func initHandlers() {
        remoteControlHandler = RemoteControlHandler(player: self)
        notificationsHandler = NotificationsHandler(player: self)
        periodicTimeObserver = PeriodicTimeObserver(avQueuePlayer: player, player: self, playerBus: playerBus)
    }
    
    func observePlayerBus() {
        playerBus.observeActions(onEach: observeActions)
        playerBus.observePlaylist(onEach: observePlaylist)
    }
    
    private func setupAudioSession() {
        do {
            try audioSession.setCategory(.playback)
            let _ = try audioSession.setActive(true)
            UIApplication.shared.beginReceivingRemoteControlEvents()
        } catch let error as NSError {
            debugPrint("Activate AVAudioSession failed.\n \(error)")
        }
    }
    
    private func observeActions(_ action: PlayerAction) {
        debugPrint("IOs observeActions -> ")
        switch(action){
        case is PlayerActionPlay: play((action as! PlayerActionPlay).lectureId)
        case is PlayerActionPause: pause()
        case is PlayerActionNext: next()
        case is PlayerActionPrev: prev()
        case is PlayerActionSeekForward: seekForward()
        case is PlayerActionSeekBack: seekBack()
        case is PlayerActionSeekTo: seekTo(timeMs: (action as! PlayerActionSeekTo).timeMs)
        case is PlayerActionSliderReleased: onSliderReleased()
        default: debugPrint("default")
        }
    }
    

//  is Speed -> setSpeed(playerA
//  SliderReleased -> onSliderReleased()
    
    private func observePlaylist(_ lectures: [Lecture]) {
        debugPrint("IOs observePlaylist -> ")
        lectures.forEach { lecture in
            debugPrint("lecture id = \(lecture.id)")
        }
        
        self.lectures = lectures
        let playlist = lectures.enumerated().map { (index, lecture) in playerItem(lecture, index)
        }
        
        pendingPlaylist = playlist
        maybeUpdateCurrentPlaylist(playlist)
    }
    
    private func resetTracks(with items: [PlayerItem], fromIndex: Int) {
        debugPrint("IOs resetTracks -> ")
        pause()
        
        player.removeAllItems()
        for i in fromIndex..<items.count {
            let item = items[i]
            if player.canInsert(item, after: nil) {
                item.seek(
                    to: CMTime(value: savedPositionProvider.getPosition(id: item.itemInfo.id), timescale: 1000),
                    toleranceBefore: CMTime.zero,
                    toleranceAfter: CMTime.zero,
                    completionHandler: nil
                )
                
                player.insert(item, after: nil)
            }
        }
    }
    
    func pause() {
        debugPrint("IOs Player Pause")
        player.pause()
    }
    
    func play(_ id: Int64? = nil) {
        debugPrint("IOs Player Play")
        
        if !isSessionSetup {
            setupAudioSession()
        }
        
        guard let id = id else {
            player.play()
            return
        }

        player.pause()
        
        if (currentId != id) {
            switchTrack(id)
        }
        
        player.play()
    }
    
    func next() {
        debugPrint("IOs Player Next")
        player.advanceToNextItem()
    }
    
    func prev() {
        debugPrint("IOs Player Prev")
        guard currentIndex > 0 else { return }
        play(currentPlaylist[currentIndex - 1].itemInfo.id)
    }
    
    func switchTrack(_ id: Int64) {
        if (currentId == id) {
            return
        }
        
        if (pendingPlaylist.contains(where: { $0.itemInfo.id == id })) {
            currentPlaylist = pendingPlaylist
        }
        
        guard let item = currentPlaylist.first(where: { $0.itemInfo.id == id }) else {
            debugPrint("IOs player does not contain lecture with id == \(id)")
            printPlaylists()
            return
        }
        resetTracks(with: currentPlaylist, fromIndex: item.index)
    }
    
    private func printPlaylists() {
        debugPrint("IOs currentPlaylist -> ")
        currentPlaylist.forEach { debugPrint("id = \($0.itemInfo.id)")}
        debugPrint("IOs pendingPlaylist -> ")
        pendingPlaylist.forEach { debugPrint("id = \($0.itemInfo.id)")}
    }
    
    func seekForward() {
        debugPrint("IOs seekForward -> ")
        let time = player.currentTime() + SEEK_TIME
        player.seek(to: time, toleranceBefore: CMTime.zero, toleranceAfter: CMTime.zero)
    }
    
    func seekBack() {
        debugPrint("IOs seekBack -> ")
        let time = player.currentTime() - SEEK_TIME
        player.seek(to: time, toleranceBefore: CMTime.zero, toleranceAfter: CMTime.zero)
    }
    
    private func seekTo(timeMs: Int64) {
        if (playOnSliderRelease == nil) {
            playOnSliderRelease = isPlaying
            pause()
        }
        
        playerBus.update(
            state: currentState(timeMs: timeMs)
        )
        
        let time = CMTime(value: timeMs, timescale: MS_IN_SECOND)
        player.seek(to: time, toleranceBefore: CMTime.zero, toleranceAfter: CMTime.zero)
    }
    
    private func onSliderReleased() {
        guard let playOnSliderRelease = playOnSliderRelease else { return }
        
        if (playOnSliderRelease) {
            play()
        }
        self.playOnSliderRelease = nil
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
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = player.currentItem?.currentTime().seconds
        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = player.currentItem?.asset.duration.seconds
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = player.rate
        // Set the metadata
        MPNowPlayingInfoCenter.default().nowPlayingInfo = nowPlayingInfo
    }
}

extension Player {
    private func playerItem(_ lecture: Lecture, _ index: Int) -> PlayerItem {
        PlayerItem(
            asset: asset(lecture),
            index: index,
            itemInfo: itemInfo(lecture)
        )
    }
    
    private func asset(_ lecture: Lecture) -> AVAsset {
        AVAsset(url: URL(string: lecture.remoteUrl)!)
    }
    
    private func itemInfo(_ lecture: Lecture) -> PlayerItemInfo {
        PlayerItemInfo (
            id: lecture.id,
            title: lecture.title,
            subtitle: lecture.subTitle,
            duration: lecture.durationMillis,
            startAt: savedPositionProvider.getPosition(id: lecture.id)
        )
    }
}

extension Player {
    private func maybeUpdateCurrentPlaylist(_ playlist: [PlayerItem]) {
        debugPrint("IOs maybeUpdateCurrentPlaylist -> ")
        if (currentPlaylist.isEmpty) {
            currentPlaylist = playlist
            resetTracks(with: currentPlaylist, fromIndex: 0)
            return
        }
        
        handleNewList(playlist)
    }
    
    private func handleNewList(_ playlist: [PlayerItem]) {
        let old = currentPlaylist.map { $0.itemInfo.id }
        let new = playlist.map { $0.itemInfo.id }
        
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
    
    private func removeMediaItem(_ index: Int) {
        if (currentPlaylist.isEmpty) {
            return
        }
        
        if (currentIndex == index) {
            pause()
        }
        
        if (currentPlaylist.indices.contains(index)) {
            currentPlaylist.remove(at: index)
            
            
            if (currentPlaylist.isEmpty) {
                clean()
            }
            
            currentPlaylist.enumerated().forEach { (index, item) in
                item.index = index
            }
            
            var fromIndex = currentIndex
            if (fromIndex >= index) {
                fromIndex -= 1
            }
            if (fromIndex < 0) {
                fromIndex = 0
            }
            
            resetTracks(with: currentPlaylist, fromIndex: fromIndex)
        }
    }
}

extension Player {
    
    var isPlaying: Bool {
        player.rate > 0.0
    }
    
    var currentItem: PlayerItem? {
        player.currentItem as? PlayerItem
    }
    
    var currentIndex: Int {
        currentItem?.index ?? -1
    }
    
    var currentId: Int64 {
        currentItem?.itemInfo.id ?? -1
    }
    
    func saveCurrentPosition(timeMs: Int64) {
        if (currentId != -1 && isPlaying) {
            
            if (currentItem?.itemInfo.duration ?? 0 < SAVE_POSITION_INTERVAL_MS) {
                return
            }

            if (currentTrackIsAlmostCompleted(timeMs)) {
                savedPositionProvider.setCompleted(id: currentId)
            } else {
                savedPositionProvider.savePosition(id: currentId, timeMs: timeMs)
            }
        }
    }
            
    func currentTrackIsAlmostCompleted(_ timeMs: Int64) -> Bool {
        (currentItem?.itemInfo.duration ?? 0) - timeMs < SAVE_POSITION_INTERVAL_MS
    }
    
    func currentState(timeMs: Int64) -> PlayerState {
        return PlayerState(
            lecture: lectures.first(where: {$0.id == currentItem?.itemInfo.id}) ?? emptyLecture(),
            isPlaying: isPlaying,
            isBuffering: status == .loading || status == .none,
            hasNext: currentIndex != -1 && currentIndex < currentPlaylist.endIndex,
            hasPrevious: currentIndex > 0,
            timeMs: timeMs,
            durationMs: currentItem?.itemInfo.duration ?? 0
        )
    }
}
