//
//  Sizes.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import CoreGraphics

let SIZE_PLAY_BUTTON = CGFloat(36)
let SIZE_FAVORITE_BUTTON = CGFloat(28)

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

struct Dimens {
    let iconSizeS = CGFloat(16)
    let iconSizeM = CGFloat(24)
    let iconSizeL = CGFloat(36)
    let iconSizeXL = CGFloat(48)
    let iconSizeXXL = CGFloat(64)

    let radiusS = 4
    let radiusM = 8
    let radiusL = 16
    let radiusXL = 32

    let borderXS = 1
    let borderS = 2

    let paddingZero = CGFloat(0)
    let paddingXS = CGFloat(4)
    let paddingS = CGFloat(8)
    let paddingM = CGFloat(16)
    let paddingL = CGFloat(24)
    let paddingXL = CGFloat(48)

    let horizontalScreenPadding = CGFloat(8)

    let buttonHeight = CGFloat(40)
    let rowHeightM = 44
    let rowHeightL = 48
    let toolbarHeightL = 60

    let bottomSheetHeight = 208
    let bottomSheetPeekHeight = 72
    let sliderHeight = 32
}
