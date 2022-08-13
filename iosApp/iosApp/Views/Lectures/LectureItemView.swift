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

struct LectureItemView: View {
    
    @EnvironmentObject var theme: Theme
    
    let lecture: Lecture
    let component : LectureComponent
    
    var body: some View {
        HStack(alignment: .center, spacing: 2.0) {
            
            PlayButton(lecture: lecture, component: component)
                .padding(.trailing, theme.dimens.paddingS)
            
            VStack(alignment: .leading, spacing: 2.0) {
                Text(lecture.title)
                    .font(theme.titleMedium)
                    .allowsTightening(true)
                    .lineLimit(3)
                    .truncationMode(.tail)
                    .multilineTextAlignment(.leading)
                    .foregroundColor(theme.colors.lectureTitle)
                    .onTapGesture {
                        lecture.isPlaying ? component.onPause() : component.onPlay(id: lecture.id)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Text(lecture.subTitle)
                    .font(theme.bodyMedium)
                    .lineLimit(1)
                    .truncationMode(.tail)
                    .foregroundColor(theme.colors.lectureDescr)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            
            FavoriteButton(lecture) { isFavorite in
                component.onFavorite(id: lecture.id, isFavorite: isFavorite)
            }
            .padding(.leading, theme.dimens.paddingS)
        }
        .padding(.horizontal, theme.dimens.paddingXS)
        .buttonStyle(.plain)
        .contextMenu {
            Button {
                component.onFavorite(id: lecture.id, isFavorite: !lecture.isFavorite)
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
        LectureItemView(lecture: mockLecture(12), component: StubLectureComponent())
            .environmentObject(themes[0])
    }
}
