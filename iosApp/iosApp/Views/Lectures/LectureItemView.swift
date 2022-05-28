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

struct LectureListItem: View {
    
    @EnvironmentObject var theme: Theme
    
    let lecture: Lecture
    let component : LecturesComponent
    
    var body: some View {
        HStack(alignment: .center, spacing: 2.0) {
            
            PlayButton(lecture: lecture, component: component)
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
                        lecture.isPlaying ? component.onPause() : component.onPlay(id: lecture.id)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Text(lecture.subTitle)
                    .font(.system(size: 12))
                    .lineLimit(3)
                    .truncationMode(.tail)
                    .multilineTextAlignment(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            
            FavoriteButton(lecture: lecture, onClick: { isFavorite in component.onFavorite(id: lecture.id, isFavorite: isFavorite) })
            // ContextMenuButton(onEvent: onEvent)
        }
        .buttonStyle(.plain)
    }
}

struct LectureListItem_Previews: PreviewProvider {
    static var previews: some View {
        LectureListItem(lecture: mockLecture(12), component: StubLecturesComponent())
            .environmentObject(themes[0])
    }
}
