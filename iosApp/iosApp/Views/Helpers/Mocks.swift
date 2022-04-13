//
//  Mocks.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

func mockLecture() -> Lecture {
    return getLecture(
        title: "Бхагавад-Гита. Вступление. Беседа на утренней прогулке",
        date: "1970-08-02",
        place: "Лос-Анджелес, США"
    )
}

func getLecture(title: String, date: String, place: String) -> Lecture {
    return Lecture(
        id: 1000,
        title: title,
        description: nil,
        date: date,
        place: place,
        durationMillis: 1000,
        fileUrl: nil,
        remoteUrl: "",
        isFavorite: false,
        isCompleted: true,
        downloadProgress: nil
    )
}
