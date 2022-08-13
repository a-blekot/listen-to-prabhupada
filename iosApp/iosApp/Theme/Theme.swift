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
    case lightTheme
    case darkTheme
}

enum Fonts{
    case headlineLarge
    
    case titleLarge
    case titleMedium
    case titleSmall
    
    case bodyMedium
    case bodySmall
    
    case labelLarge
    case labelMedium
}

class Theme:ObservableObject {
    @Published var colors: LightColors
    @Published var dimens: Dimens
    
    @Published var headlineLarge:Font
    @Published var titleLarge:Font
    @Published var titleMedium:Font
    @Published var titleSmall:Font
    @Published var bodyMedium:Font
    @Published var bodySmall:Font
    @Published var labelLarge:Font
    @Published var labelMedium:Font
    
    init(
        colors:LightColors,
        dimens:Dimens,
        headlineLarge:Font,
        titleLarge:Font,
        titleMedium:Font,
        titleSmall:Font,
        bodyMedium:Font,
        bodySmall:Font,
        labelLarge:Font,
        labelMedium:Font
    ){
        self.colors = colors
        self.dimens = dimens
        
        self.headlineLarge = headlineLarge
        self.titleLarge = titleLarge
        self.titleMedium = titleMedium
        self.titleSmall = titleSmall
        self.bodyMedium = bodyMedium
        self.bodySmall = bodySmall
        self.labelLarge = labelLarge
        self.labelMedium = labelMedium
    }
}

func getFont(name:Fonts) -> Font{
    switch(name){
    case .headlineLarge: return Font.system(size: 32) // ("Arial", size: 30)
        
    case .titleLarge: return Font.system(size: 22) // (size: 30)
    case .titleMedium: return Font.system(size: 16) // (size: 30)
    case .titleSmall: return Font.system(size: 14) // (size: 30)
        
    case .bodyMedium: return Font.system(size: 14) // (size: 12)
    case .bodySmall: return Font.system(size: 12) // (size: 12)
        
    case .labelLarge: return Font.system(size: 14)
    case .labelMedium: return Font.system(size: 13)
    }
}

var themes: [Theme] = [
    //default = lightTheme
    Theme(
        colors: LightColors(),
        dimens: Dimens(),
        headlineLarge: getFont(name: .headlineLarge),
        titleLarge: getFont(name: .titleLarge),
        titleMedium: getFont(name: .titleMedium),
        titleSmall: getFont(name: .titleSmall),
        bodyMedium: getFont(name: .bodyMedium),
        bodySmall: getFont(name: .bodySmall),
        labelLarge: getFont(name: .labelLarge),
        labelMedium: getFont(name: .labelMedium)
    )
]
