//
//  ContextMenuButton.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct ContextMenuButton: View {
    var onClick : () -> ()
    
    var body: some View {
        Button {
            onClick()
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
        ContextMenuButton(onClick: {})
            .environmentObject(themes[0])
    }
}
