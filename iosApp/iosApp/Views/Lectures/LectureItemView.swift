//
//  LectureListItem.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct CustomCenter: AlignmentID {
  static func defaultValue(in context: ViewDimensions) -> CGFloat {
      context[HorizontalAlignment.leading]
  }
}

extension HorizontalAlignment {
  static let customLeading: HorizontalAlignment = .init(CustomCenter.self)
}

protocol LectureListener {
    func onPause()
    func onPlay(id: Int64)
    func onFavorite(id: Int64, isFavorite: Bool)
}

struct StubLectureListener : LectureListener {
    func onPause() {}
    func onPlay(id: Int64) {}
    func onFavorite(id: Int64, isFavorite: Bool) {}
}

struct LectureItemView: View {
    
    @EnvironmentObject var theme: Theme
    
    let lecture: Lecture
    let listener : LectureListener
    
    var body: some View {
        HStack(alignment: .center, spacing: 2.0) {
            
            PlayButton(lecture: lecture, listener: listener)
                .padding(.trailing, 10)
            
            VStack(alignment: .leading, spacing: 4.0) {
                Text(lecture.title)
                    .font(.system(size: 16))
                    .allowsTightening(true)
                    .lineLimit(3)
                    .truncationMode(.tail)
                    .multilineTextAlignment(.leading)
                    .foregroundColor(.orange)
                    .onTapGesture {
                        lecture.isPlaying ? listener.onPause() : listener.onPlay(id: lecture.id)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Text(lecture.subTitle)
                    .font(.system(size: 12))
                    .lineLimit(3)
                    .truncationMode(.tail)
                    .foregroundColor(theme.descriptionTextColor)
                    .multilineTextAlignment(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            
            FavoriteButton(lecture) { isFavorite in
                listener.onFavorite(id: lecture.id, isFavorite: isFavorite)
            }
        }
        .buttonStyle(.plain)
        .contextMenu {
            Button {
                listener.onFavorite(id: lecture.id, isFavorite: !lecture.isFavorite)
            } label: {
                let text = lecture.isFavorite ? "Удалить из избранного" : "Добавить в избранное"
                let icon = lecture.isFavorite ? "heart.fill" : "heart"
                
                Label(text, systemImage: icon)
            }

            Button {
                print("Enable geolocation")
            } label: {
                Label("Detect Location", systemImage: "location.circle")
            }
        }
    }
}

struct LectureListItem_Previews: PreviewProvider {
    static var previews: some View {
        LectureItemView(lecture: mockLecture(12), listener: StubLectureListener())
            .environmentObject(themes[0])
    }
}
