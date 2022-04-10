//
//  PlayButton.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct PlayButton: View {
    @Binding var isPlaying: Bool

    var body: some View {
        Button {
            isPlaying.toggle()
        } label: {
            Label(
                "Toggle Playing",
                systemImage: isPlaying ? "star.fill" : "star"
            )
                .labelStyle(.iconOnly)
                .foregroundColor(isPlaying ? .yellow : .gray)
        }
    }
}

struct PlayButton_Previews: PreviewProvider {
    static var previews: some View {
        PlayButton(isPlaying: .constant(true))
    }
}
