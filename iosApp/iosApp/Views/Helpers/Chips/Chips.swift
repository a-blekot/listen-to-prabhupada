//
//  Chips.swift
//  iosApp
//
//  Created by Aleksey Blekot on 29.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct Chips: View {
    
    @EnvironmentObject var theme: Theme
    let text: String
    let onClick: () -> Void
    
    var body: some View {
        HStack(alignment: .center) {
            Text(text)
                .font(theme.titleSmall)
                .lineLimit(1)
            
            Image.init(systemName: "xmark")
                .resizable()
                .frame(width: 8, height: 8, alignment: .center)
        }
        .padding(theme.dimens.paddingS)
        .foregroundColor(theme.colors.chipsContent)
        .background(
            RoundedRectangle(cornerRadius: theme.dimens.radiusM)
                .fill(theme.colors.chipsBg)
        )
        .background(
            RoundedRectangle(cornerRadius: theme.dimens.radiusM)
                .stroke(theme.colors.chipsContent, lineWidth: theme.dimens.borderS)
        )
        .onTapGesture {
            onClick()
        }
    }
}

struct Chips_Previews: PreviewProvider {
    static var previews: some View {
        Chips(text: "1971", onClick: {})
            .previewLayout(PreviewLayout.fixed(width: 200, height: 200))
            .previewDisplayName("Chips preview")
            .environmentObject(themes[0])
    }
}
