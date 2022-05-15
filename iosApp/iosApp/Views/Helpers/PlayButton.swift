//
//  PlayButton.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct PlayButton: View {
    let lecture: Lecture
    let component: LecturesComponent
    
    var image: String {
        switch true {
        case lecture.isPlaying: return "pause.circle"
        case lecture.isCompleted: return "checkmark.circle"
        default: return "play.circle"
        }
    }
    
    var body: some View {
        Button {
            lecture.isPlaying ? component.onPause() : component.onPlay(id: lecture.id)
        } label: {
            
            Image(systemName: image)
                .resizable(resizingMode: .tile)
                .frame(width: SIZE_PLAY_BUTTON, height: SIZE_PLAY_BUTTON)
                .fixedSize()
                .foregroundColor(.orange)
        }
    }
}

//struct PlayButton_Previews: PreviewProvider {
//    static var previews: some View {
//        PlayButton(mockLecture(), true, onEvent: { _ in })
//    }
//}

//class PlayAudio: AVAudioPlayerDelegate  {
//    var audioPlayer:AVAudioPlayer!
//    
//    @objc func playAudioButtonTapped(_ sender: UIButton) {
//        if sender.currentBackgroundImage == #imageLiteral(resourceName: "playAudio") { //Play
//            sender.setBackgroundImage(#imageLiteral(resourceName: "pause"), for: .normal) // set stop image
//            let url = URL(string: urlstring!)
//            downloadFileFromURL(url: url!)
//        } else {//stop
//            sender.setBackgroundImage(#imageLiteral(resourceName: "playAudio"), for: .normal) // set play image
//            audioPlayer.stop()
//        }
//    }
//    func downloadFileFromURL(url: URL){
//        var downloadTask:URLSessionDownloadTask
//        downloadTask = URLSession.shared.downloadTask(with: url) { (url, response, error) in
//            self.play(url: url!)
//        }
//        downloadTask.resume()
//    }
//    func play(url:URL) {
//        do {
//            audioPlayer = try AVAudioPlayer(contentsOf: url as URL)
//            audioPlayer.prepareToPlay()
//            audioPlayer.volume = 2.0
//            audioPlayer.play()
//        } catch let error as NSError {
//            print(error.localizedDescription)
//        } catch {
//            print("AVAudioPlayer init failed")
//        }
//    }
//}
