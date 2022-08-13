//
//  SavedPositionProvider.swift
//  iosApp
//
//  Created by Aleksey Blekot on 23.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

struct SavedPositionProvider {
    
    let db: Database
    
    func getPosition(id: Int64) -> Int64 {
        db.selectSavedPosition(id_: id)
    }
    
    func savePosition(id: Int64, timeMs: Int64) {
        db.insertSavedPosition(id: id, pos_: timeMs)
    }
    
    func setCompleted(id: Int64) {
        db.insertCompleted(id: id, isCompleted_: true)
        db.insertSavedPosition(id: id, pos_: 0)
    }
}
