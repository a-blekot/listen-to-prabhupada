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
    var lecture: Lecture
    var component : LecturesComponent
    
    var body: some View {
        HStack(alignment: .center, spacing: 2.0) {
            
            PlayButton(lecture: lecture, component: component)
                .padding(.trailing, 10)
            
            VStack(alignment: .leading, spacing: 4.0) {
                Text(lecture.title)
                //                    .font(.system(.title, design: .rounded))
                //                    .font(.system(size: 20))
                //                    .font(.custom("Nunito", size: 25))
                //.font(.headline)
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
        .padding(.horizontal, -10)
        .buttonStyle(.plain)
    }
}

//struct LectureListItem_Previews: PreviewProvider {
//    static var previews: some View {
//        LectureListItem(
//            lecture: mockLecture(),
//            isPlaying: false,
//            onEvent: { _ in}
//        )
//    }
//}

//
//        Spacer(modifier = Modifier.weight(2f))
//
//        Column(
//            modifier = Modifier
//                .weight(8f)
//                .background(Color(130, 0, 255, testBgAlpha))
//                .align(Top)
//        ) {
//
//            val favoriteResId = if (lecture.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
//            Image(
//                painter = painterResource(favoriteResId),
//                contentScale = ContentScale.FillBounds,
//                contentDescription = "favorite image",
//                modifier =
//                Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f)
//                    .clickable { onEvent(CommonUiEvent.Favorite(lecture, !lecture.isFavorite)) }
//            )
//
//            if (lecture.downloadProgress == FULL_PROGRESS) {
//                Image(
//                    painter = painterResource(R.drawable.ic_download_mark),
//                    contentScale = ContentScale.FillBounds,
//                    contentDescription = "download success",
//                    modifier = Modifier
//                        .padding(top = 8.dp)
//                        .fillMaxWidth()
//                        .aspectRatio(1f)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.weight(2f))
//
//        Image(
//            painter = painterResource(R.drawable.ic_context_menu),
//            contentScale = ContentScale.FillBounds,
//            contentDescription = "menu image",
//            modifier =
//            Modifier
//                .align(CenterVertically)
//                .weight(5f)
//                .background(Color(130, 0, 255, testBgAlpha))
//                .aspectRatio(0.4f)
//                .clickable { onEvent(CommonUiEvent.Share(lecture.id)) }
//        )
//    }
//
//@Preview(widthDp = 360, heightDp = 40)
//@Composable
//fun PreviewLectureListItem() {
//    AppTheme {
//        LectureListItem(
//            getLecture(
//                title = "Бхагавад-Гита. Вступление. Беседа на утренней прогулке",
//                date = "1970-08-02",
//                place = "Лос-Анджелес, США"
//            ),
//            isPlaying = false
//        )
//    }
//}
//
//fun getLecture(title: String, date: String, place: String) =
//    Lecture(
//        title = title,
//        date = date,
//        place = place,
//        durationMillis = 85_000,
//        isFavorite = true,
//        isCompleted = false,
//        downloadProgress = FULL_PROGRESS
//    )
