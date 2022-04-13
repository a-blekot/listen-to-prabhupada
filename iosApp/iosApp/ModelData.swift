//
//  ModelData.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Combine
import shared


final class ModelData: ObservableObject {
    @Published var lectures: [Lecture] = [
        mockLecture(),
        mockLecture(),
        mockLecture()
    ]
}
