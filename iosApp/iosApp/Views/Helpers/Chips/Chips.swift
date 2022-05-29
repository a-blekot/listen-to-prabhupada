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
            Text(text).font(.title3).lineLimit(1)
            
            GeometryReader { proxy in
                ZStack {
                    Circle()
                        .frame(
                            width: proxy.size.width * 0.55,
                            height: proxy.size.width * 0.55,
                            alignment: .center
                        )
                        .foregroundColor(theme.bodyTextColor)
                    
                    Image.init(systemName: "xmark.circle.fill")
                        .resizable()
                        .font(.title3)
                        .foregroundColor(theme.buttonDarkBacground)
                }
            }
            .frame(width: 20, height: 20, alignment: .center)
            
        }
        .padding()
        .foregroundColor(theme.bodyTextColor)
        .background(theme.buttonLightBacground)
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
