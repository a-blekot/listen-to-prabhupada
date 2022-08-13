//
//  Colors.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.08.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension Color {
    init(hex: Int, opacity: Double = 1.0) {
        let red = Double((hex & 0xff0000) >> 16) / 255.0
        let green = Double((hex & 0xff00) >> 8) / 255.0
        let blue = Double((hex & 0xff) >> 0) / 255.0
        self.init(.sRGB, red: red, green: green, blue: blue, opacity: opacity)
    }
}

struct LightColors {
    private let primary2 = Color(hex: 0xFFCB8552)
    private let primary3 = Color(hex: 0xFFE8B07E)
    
    private let secondary = Color(hex: 0xFFFFD5AF)
    private let secondary2 = Color(hex: 0xFFFEEAD8)
    private let secondary3 = Color(hex: 0xFFFFE7D1)
    private let secondary4 = Color(hex: 0xFFDE924D)
    private let secondary5 = Color(hex: 0xFFFAD1B4)
    private let secondary6 = Color(hex: 0xFFFFEADA)
    
    private let gray1 = Color(hex: 0xFFB0B0B0)
    private let gray2 = Color(hex: 0xFF9E9E9E)
    
    let primary = Color(hex: 0xFFD0690D)
    let tertiary = Color(hex: 0xFF603004)
    let background = Color(hex: 0xFFFFFFFF)
    let surface = Color(hex: 0xFFFFF7EF)
    
    var toolbar: Color {surface}
    var chipsBg: Color {surface}
    var chipsContent: Color {primary}
    
    var lectureTitle: Color {primary}
    var lectureDescr: Color {gray1}
    var lecturePlay: Color {primary3}
    var lecturePause: Color {primary3}
    
    var favSelected: Color {primary}
    var favUnselected: Color {secondary2}
    var btnPages: Color {surface}
    
    var playerBg: Color {surface}
    var playerTimeLineSelector: Color {primary}
    var playerTimeLineBg: Color {secondary5}
    var playerTimer: Color {tertiary}
    var playerTitle: Color {tertiary}
    var playerDescr: Color {gray2}
    var playerButtons: Color {tertiary}
    var playerSpeedBg: Color {secondary6}
    
    var navBarBg: Color {secondary6}
    var navBarText: Color {tertiary}
    var navBarIcon: Color {tertiary}
    var navBarIconSelected: Color {secondary2}
    var navBarIconSelectedBg: Color {primary}
    var navBarIconSelectedText: Color {primary}
    
    var filtersCategory: Color {primary3}
    var filtersSelected: Color {secondary3}
    var filtersNeutral: Color {surface}
    var filtersText: Color {tertiary}
    var filtersCountText: Color {primary2}
}
