//
//  HeaderItemView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.08.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct HeaderItemView: View {
    
    @EnvironmentObject var theme: Theme
    
    let icon: String
    let number: Int
    let description: String
    
    init(_ icon: String, _ number: Int, _ description: String) {
        self.icon = icon
        self.number = number
        self.description = description
    }
    
    var body: some View {
        HStack {
            Image(systemName: icon)
                .resizable()
                .frame(width: SIZE_FAVORITE_BUTTON, height: SIZE_FAVORITE_BUTTON/1.1)
                .fixedSize()
                .foregroundColor(theme.colors.favSelected)
                .contentShape(Rectangle())
            
            VStack {
                Text("\(number)")
                    .font(theme.titleMedium)
                    .allowsTightening(true)
                    .lineLimit(1)
                    .truncationMode(.tail)
                    .foregroundColor(theme.colors.lectureTitle)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Text(description)
                    .font(theme.bodyMedium)
                    .lineLimit(1)
                    .truncationMode(.tail)
                    .foregroundColor(theme.colors.lectureDescr)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }
}

//struct HeaderItemView_Previews: PreviewProvider {
//    static var previews: some View {
//        HeaderItemView()
//    }
//}
