////
////  AQQueuePlayer.swift
////  iosApp
////
////  Created by Aleksey Blekot on 21.05.2022.
////  Copyright © 2022 orgName. All rights reserved.
////
//
//import Foundation
//import AVFoundation
//
//public enum AQPlayerStatus: Int {
//    case none
//    case loading
//    case failed
//    case readyToPlay
//    case playing
//    case paused
//}
//
//class AQQueuePlayer: AVQueuePlayer {
//    var currentIndex: Int! {
//        if let item = self.currentItem as? AQPlayerItem {
//            return item.index
//        }
//        return nil
//    }
//}
//
//class AQPlayerItem: AVPlayerItem {
//    var index: Int!
//    var itemInfo: AQPlayerItemInfo!
//
//    init(asset: AVAsset, index: Int, itemInfo: AQPlayerItemInfo! = nil) {
//        super.init(asset: asset, automaticallyLoadedAssetKeys: nil)
//        if let startAt = itemInfo.startAt {
//            super.seek(to: CMTime(seconds: startAt , preferredTimescale: Defaults.preferredTimescale), completionHandler: nil)
//        }
//        self.index = index
//        self.itemInfo = itemInfo
//    }
//}
//
//public class AQPlayerItemInfo: Equatable {
//    public var id: Int!
//    public var url: URL!
//    public var title: String!
//    public var albumTitle: String!
//    public var coverImage: UIImage!
//    public var startAt: TimeInterval!
//
//    public init(id: Int!, url: URL!, title: String!, albumTitle: String!, coverImageURL: String!, startAt: TimeInterval!) {
//        self.id = id
//        self.url = url
//        self.title = title
//        self.albumTitle = albumTitle
//
//        if let urlStr = coverImageURL, let url = URL(string: urlStr) {
//            DispatchQueue.global().async {
//                if let data = try? Data( contentsOf:url)
//                {
//                    DispatchQueue.main.async {
//                        self.coverImage = UIImage(data: data)
//                    }
//                }
//            }
//        }
//
//        self.startAt = startAt
//    }
//
//    public init(id: Int!, url: URL!, title: String!, albumTitle: String!, coverImage: UIImage!, startAt: TimeInterval!) {
//        self.id = id
//        self.url = url
//        self.title = title
//        self.albumTitle = albumTitle
//        self.coverImage = coverImage
//        self.startAt = startAt
//    }
//
//    public static func == (lhs: AQPlayerItemInfo, rhs: AQPlayerItemInfo) -> Bool {
//        return lhs.id == rhs.id || lhs.url == rhs.url
//    }
//}
