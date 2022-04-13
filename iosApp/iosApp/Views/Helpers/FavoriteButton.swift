//
//  FavoriteButton.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct FavoriteButton: View {
    let lecture: Lecture
    let onEvent : (CommonUiEvent) -> ()
    
    var body: some View {
        Button {
            onEvent(CommonUiEvent.Favorite(lecture: lecture, isFavorite: !lecture.isFavorite))
        } label: {
            
            Image(systemName: lecture.isFavorite ? "heart.fill" : "heart")
                .resizable(resizingMode: .tile)
                .frame(width: SIZE_FAVORITE_BUTTON, height: SIZE_FAVORITE_BUTTON)
                .fixedSize()
                .foregroundColor(.orange)
        }
    }
}

struct FavoriteButton_Previews: PreviewProvider {
    static var previews: some View {
        FavoriteButton(lecture: mockLecture(), onEvent: { _ in })
    }
}
