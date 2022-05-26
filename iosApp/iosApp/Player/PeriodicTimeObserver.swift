//
//  PeriodicTimeObserver.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import AVFoundation
import Prabhupada

private let ONE_SECOND = CMTime(value: 1, timescale: 1)
let SAVE_POSITION_INTERVAL_SECONDS = 5
let SAVE_POSITION_INTERVAL_MS = SAVE_POSITION_INTERVAL_SECONDS * 1000

class PeriodicTimeObserver {
    
    private let avQueuePlayer: AVQueuePlayer
    private let player: Player
    private let playerBus: PlayerBus
    
    private var updateCounter = 1
    private var timeObserverToken: Any?
    
    init(avQueuePlayer: AVQueuePlayer, player: Player, playerBus: PlayerBus) {
        self.avQueuePlayer = avQueuePlayer
        self.player = player
        self.playerBus = playerBus
        addPeriodicTimeObserver()
    }
    
    deinit {
        removePeriodicTimeObserver()
    }
    
    private func update(timeMs: Int64) {
        playerBus.update(
            state: player.currentState(timeMs: timeMs)
        )
        
        updateCounter += 1
        
        if (updateCounter % SAVE_POSITION_INTERVAL_SECONDS == 0) {
            player.saveCurrentPosition(timeMs: timeMs)
        }
    }
    
    private func addPeriodicTimeObserver() {
        print("IOs addPeriodicTimeObserver -> ")
        timeObserverToken = avQueuePlayer.addPeriodicTimeObserver(forInterval: ONE_SECOND, queue: .main) { [weak self] time in
            guard let strongSelf = self else {return}
            strongSelf.update(timeMs: Int64(CMTimeGetSeconds(time)) * MS_IN_SECOND_64)
        }
    }

    private func removePeriodicTimeObserver() {
        if let timeObserverToken = timeObserverToken {
            avQueuePlayer.removeTimeObserver(timeObserverToken)
            self.timeObserverToken = nil
        }
    }
}
