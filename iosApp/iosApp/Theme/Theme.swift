//
//  Theme.swift
//  iosApp
//
//  Created by Aleksey Blekot on 20.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

enum ThemeName {
    case redTheme
    case darkTheme
}

enum Fonts{
    case headlineFont
    case mainTitleFont
    case bodyFont
    case buttonFont
    case warningFont
}

class Theme:ObservableObject {
    @Published var brandColor: Color
    @Published var backgroundColor: Color
    @Published var contrastBackgroundColor: Color
    @Published var secondaryColor: Color
    @Published var shadowColor: Color
    @Published var bodyTextColor: Color
    
    @Published var buttonLightBacground: Color
    @Published var buttonMidBacground: Color
    @Published var buttonDarkBacground: Color
    
    @Published var headlineFont:Font
    @Published var mainTitleFont:Font
    @Published var bodyFont:Font
    @Published var buttonFont:Font
    @Published var warningFont:Font
    
    @Published var largeSpacing:CGFloat
    @Published var mediumSpacing:CGFloat
    @Published var smallSpacing:CGFloat
    
    init(
        brandColor:Color,
        backgroundColor:Color,
        contrastBackgroundColor:Color,
        secondaryColor:Color,
        shadowColor:Color,
        bodyTextColor:Color,
        
        buttonLightBacground: Color,
        buttonMidBacground: Color,
        buttonDarkBacground: Color,
        
        headlineFont:Font,
        mainTitleFont:Font,
        bodyFont:Font,
        buttonFont:Font,
        warningFont:Font,
        largeSpacing:CGFloat,
        mediumSpacing:CGFloat,
        smallSpacing:CGFloat){
            self.brandColor = brandColor
            self.backgroundColor = backgroundColor
            self.contrastBackgroundColor = contrastBackgroundColor
            self.secondaryColor = secondaryColor
            self.shadowColor = shadowColor
            self.bodyTextColor = bodyTextColor
            self.buttonLightBacground = buttonLightBacground
            self.buttonMidBacground = buttonMidBacground
            self.buttonDarkBacground = buttonDarkBacground
            
            self.headlineFont = headlineFont
            self.mainTitleFont = mainTitleFont
            self.bodyFont = bodyFont
            self.buttonFont = buttonFont
            self.warningFont = warningFont
            self.largeSpacing = largeSpacing
            self.mediumSpacing = mediumSpacing
            self.smallSpacing = smallSpacing
            
        }
}

extension Color {
    init(hex: Int, opacity: Double = 1.0) {
        let red = Double((hex & 0xff0000) >> 16) / 255.0
        let green = Double((hex & 0xff00) >> 8) / 255.0
        let blue = Double((hex & 0xff) >> 0) / 255.0
        self.init(.sRGB, red: red, green: green, blue: blue, opacity: opacity)
    }
}

func getFont(name:Fonts) -> Font{
    switch(name){
    case .headlineFont: return Font.custom("Arial", size: 30)
    case .mainTitleFont: return Font.system(size: 30)
    case .bodyFont: return Font.system(size: 12)
    case .buttonFont: return Font.custom("Arial", size:16)
    case .warningFont: return Font.system(size: 8)
    default: return Font.custom("SanFrancisco", size: 20)
    }
}

//private val BrownDark = Color(0xFF603004)
//private val BrownMed = Color(0xB2DE924D)
//private val BrownMedLight = Color(0xFFFEC795)
//private val BrownMedLight2 = Color(0xFFE8B07E)
//private val BrownLight = Color(0xB2FFECDB)
//private val BrownXLight = Color(0xFFFFFFFF)
//val AlphaDarkBg = Color(0x44000000)
////private val BrownXLight = Color(0xFFE5E5E5)
//
//private val Orange = Color(0xFFB65D0D)
////private val OrangeLight = Color(0xFFFAD1B4) ?? BrownMedLight
//val GrayLight = Color(0xFF919191)

var themes: [Theme] = [
    //default
    Theme(brandColor: Color(hex: 0x552233),
          backgroundColor: Color(hex: 0xddee44),
          contrastBackgroundColor: Color(hex: 0xf2c233),
          secondaryColor: Color(hex: 0x26ba2d),
          shadowColor: Color(hex: 0x336688),
          bodyTextColor: Color(hex: 0x603004),
          
          buttonLightBacground: Color(hex: 0xFFF7EF),
          buttonMidBacground: Color(hex: 0xFFE7D1),
          buttonDarkBacground: Color(hex: 0xDE924D),
          
          headlineFont: getFont(name: .headlineFont),
          mainTitleFont:getFont(name: .mainTitleFont),
          bodyFont: getFont(name: .bodyFont),
          buttonFont: getFont(name: .buttonFont),
          warningFont: getFont(name: .warningFont),
          
          largeSpacing: 16.0,
          mediumSpacing: 12.0,
          smallSpacing: 4.0
         ),
    
    
    //redTheme
    Theme(brandColor: Color(hex: 0x89aa3d),
          backgroundColor: Color(hex:0xf5ec13),
          contrastBackgroundColor: Color(hex: 0xf2c233),
          secondaryColor: Color(hex: 0x4f2ba1),
          shadowColor: Color(hex: 0x336688),
          bodyTextColor: Color(hex: 0x603004),
          
          buttonLightBacground: Color(hex: 0xFFF7EF),
          buttonMidBacground: Color(hex: 0xFFE7D1),
          buttonDarkBacground: Color(hex: 0xDE924D),
          
          headlineFont: getFont(name: .headlineFont),
          mainTitleFont: getFont(name: .mainTitleFont),
          bodyFont: getFont(name: .bodyFont),
          buttonFont: getFont(name: .buttonFont),
          warningFont: getFont(name: .warningFont),
          
          largeSpacing: 16.0,
          mediumSpacing: 6.0,
          smallSpacing: 4.0
         )
    
]
