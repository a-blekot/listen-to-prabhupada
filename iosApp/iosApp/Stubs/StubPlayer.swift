//
//  StubPlayer.swift
//  iosApp
//
//  Created by Aleksey Blekot on 20.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Prabhupada

class StubPlayer: Player {
    var playerBus: PlayerBus = StubPlayerBus()
}

class StubPlayerBus: PlayerBus {
    
    func currentState() -> PlayerState {
        mockPlayerState()
    }
    
    func observeActions(onEach: @escaping (PlayerAction) -> Void) {
        
    }
    
    func observePlaylist(onEach: @escaping ([Lecture]) -> Void) {
        
    }
    
    
    func observeState(onEach: @escaping (PlayerState) -> Void) {
        
    }
    
    func update(action: PlayerAction) {
        
    }
    
    func update(state: PlayerState) {
        
    }
    
    func update(playlist: [Lecture]) {
        
    }
    
    
}
