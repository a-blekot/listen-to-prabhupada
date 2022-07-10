//
//  Mocks.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Prabhupada

func valueOf<T: AnyObject>(_ value: T) -> Value<T> {
    return MutableValueBuilderKt.MutableValue(initialValue: value) as! MutableValue<T>
}

func mockPagination(_ page: Int32, _ total: Int32) -> Pagination {
    return Pagination(prev: KotlinInt(value: page - 1), curr: page, next: KotlinInt(value: page + 1), total: total)
}

func mockLecture(_ id: Int64, isFavorite: Bool = true) -> Lecture {
    getLecture(
        id: id,
        title: "Бхагавад-Гита. Вступление. Беседа на утренней прогулке. Лекция прочитана в Бомбее куча учеников Его Божественной Милости",
        date: "1970-08-02",
        place: "Лос-Анджелес, США",
        isFavorite: isFavorite
    )
}

func emptyLecture() -> Lecture {
    Lecture(
        id: 0,
        title: "",
        description: nil,
        date: "",
        place: "",
        durationMillis: 0,
        fileUrl: nil,
        remoteUrl: "",
        isFavorite: false,
        isCompleted: false,
        isPlaying: false,
        downloadProgress: nil
    )
}

func getLecture(id: Int64, title: String, date: String, place: String, isFavorite: Bool) -> Lecture {
    Lecture(
        id: id,
        title: title,
        description: nil,
        date: date,
        place: place,
        durationMillis: 100000,
        fileUrl: nil,
        remoteUrl: "",
        isFavorite: isFavorite,
        isCompleted: true,
        isPlaying: false,
        downloadProgress: nil
    )
}

func mockFilter(_ title: String, isExpanded: Bool = false) -> Filter {
    Filter(
        name: title,
        title: title,
        parent: "",
        options: [
            mockOption("Option 1"),
            mockOption("Option 2", isSelected: true),
            mockOption("Option 3"),
            mockOption("Option 4"),
        ],
        isExpanded: isExpanded
    )
}

func mockOption(_ name: String, isSelected: Bool = false) -> Option {
    Option(
        value: name,
        text: name,
        isSelected: isSelected
    )
}

func mockPlayerState() -> PlayerState {
    PlayerState(
        lecture: mockLecture(121),
        isPlaying: false,
        isBuffering: false,
        hasNext: true,
        hasPrevious: true,
        timeMs: 1231000,
        durationMs: 1800000
    )
}
