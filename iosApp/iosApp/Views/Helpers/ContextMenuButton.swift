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
    @EnvironmentObject var theme: Theme
    var onClick : () -> ()
    
    var body: some View {

            VStack(alignment: .center, spacing: 3) {
                circle()
                circle()
                circle()
            }

    }
    
    private func circle() -> some View {
        Circle()
            .frame(width: 6, height: 6)
            .foregroundColor(theme.descriptionTextColor)
    }
}

struct ContextMenuButton_Previews: PreviewProvider {
    static var previews: some View {
        ContextMenuButton(onClick: {})
            .environmentObject(themes[0])
    }
}
