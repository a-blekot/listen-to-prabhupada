//
//  BookmarkButton.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct BookmarkButton: View {
    var body: some View {
        Button(action: {
            print("Button action")
        }) {
            HStack {
                Image(systemName: "bookmark.fill")
                Text("Bookmark")
            }.padding(10.0)
            .overlay(
                RoundedRectangle(cornerRadius: 10.0)
                    .stroke(lineWidth: 2.0)
            )
        }
        .foregroundColor(.orange)
    }
}

struct BookmarkButton_Previews: PreviewProvider {
    static var previews: some View {
        BookmarkButton()
    }
}
