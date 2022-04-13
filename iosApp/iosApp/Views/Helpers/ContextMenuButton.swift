//
//  ContextMenuButton.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ContextMenuButton: View {
    var onEvent : (CommonUiEvent) -> ()
    
    var body: some View {
        Button {
            onEvent(CommonUiEvent.FavoritesEvent())
        } label: {
            Image(systemName: "list.bullet")
                .resizable(resizingMode: .stretch)
                .frame(width: CONTEXT_MENU_BTN_H, height: CONTEXT_MENU_BTN_H)
                .foregroundColor(.orange)
        }
    }
}

struct ContextMenuButton_Previews: PreviewProvider {
    static var previews: some View {
        ContextMenuButton(onEvent: { _ in })
    }
}
