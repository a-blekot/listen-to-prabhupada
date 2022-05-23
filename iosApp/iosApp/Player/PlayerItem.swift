//
//  PlayerItem.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import AVFoundation

class PlayerItem: AVPlayerItem {
    var index: Int
    var itemInfo: PlayerItemInfo
    
    init(asset: AVAsset, index: Int, itemInfo: PlayerItemInfo) {
        self.index = index
        self.itemInfo = itemInfo
        
        super.init(asset: asset, automaticallyLoadedAssetKeys: ["playable"])
//        super.seek(
//            to: CMTime(value: itemInfo.startAt, timescale: 1),
//            toleranceBefore: CMTime.zero,
//            toleranceAfter: CMTime.zero,
//            completionHandler: nil
//        )
    }
}
