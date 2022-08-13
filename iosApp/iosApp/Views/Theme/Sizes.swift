//
//  Sizes.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import CoreGraphics

let SIZE_PLAY_BUTTON = CGFloat(35)
let SIZE_FAVORITE_BUTTON = CGFloat(15)

let CONTEXT_MENU_BTN_W = CGFloat(10)
let CONTEXT_MENU_BTN_H = CGFloat(30)

let SIZE_PLAYER_CONTROL_BUTTON = CGFloat(15)

enum Sizes: CGFloat {
    case playerControl = 25
    case playerControlBig = 30
}

func size(_ size: Sizes) -> CGFloat {
    return size.rawValue
}
