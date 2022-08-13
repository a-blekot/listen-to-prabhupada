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
        HStack {
            Text(text)
                .font(theme.titleSmall)
                .lineLimit(1)
                .foregroundColor(theme.colors.chipsContent)
            
            Image.init(systemName: "xmark.circle.fill")
                .resizable()
                .frame(width: 20, height: 20, alignment: .center)
                .font(theme.titleSmall)
                .foregroundColor(theme.colors.chipsContent)
        }
        .padding()
        .foregroundColor(theme.colors.chipsContent)
        .background(theme.colors.chipsBg)
        .cornerRadius(8)
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
