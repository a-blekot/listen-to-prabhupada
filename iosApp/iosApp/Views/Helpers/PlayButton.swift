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

struct PlayButton_Previews: PreviewProvider {
    static var previews: some View {
        PlayButton(lecture: mockLecture(12), component: StubLecturesComponent())
            .environmentObject(themes[0])
    }
}

