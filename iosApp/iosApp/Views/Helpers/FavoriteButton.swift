//
//  FavoriteButton.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FavoriteButton: View {
    let lecture: Lecture
    let onClick : (Bool) -> ()
    
    init(_ lecture: Lecture, _ onClick: @escaping (Bool) -> ()) {
        self.lecture = lecture
        self.onClick = onClick
    }
    
    var body: some View {
        Button {
            onClick(!lecture.isFavorite)
        } label: {
            
            Image(systemName: lecture.isFavorite ? "heart.fill" : "heart")
                .resizable()
                .frame(width: SIZE_FAVORITE_BUTTON, height: SIZE_FAVORITE_BUTTON)
                .fixedSize()
                .foregroundColor(.orange)
                .contentShape(Rectangle())
        }
        .contentShape(Rectangle())
    }
}

struct FavoriteButton_Previews: PreviewProvider {
    static var previews: some View {
        FavoriteButton(mockLecture(1)) { _ in }
            .environmentObject(themes[0])
    }
}
