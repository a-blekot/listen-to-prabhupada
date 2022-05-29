//
//  LectureShimmerView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 29.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct LectureShimmerView: View {
    
    @State private var show: Bool = true
    private let offset = UIScreen.main.bounds.width * 1.5

    private let color = Color.white.opacity(0.5)
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.3)
                .frame(height: 50)
                .cornerRadius(4)
                .mask(
                    Rectangle()
                        .fill(
                            LinearGradient(
                                gradient: .init(colors: [.clear, color, color, color, .clear]),
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                )
            
            Color.white
                .frame(height: 50)
                .cornerRadius(4)
                
                .mask(
                    Rectangle()
                        .fill(
                            LinearGradient(
                                gradient: .init(colors: [.clear, color, .clear]),
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                        .offset(x: show ? -offset : offset)
                )
        }
        .padding(.bottom, 3)
        .onAppear {
            withAnimation {
                show.toggle()
            }
        }
        
    }
}

struct LectureShimmerView_Previews: PreviewProvider {
    static var previews: some View {
        LectureShimmerView()
    }
}
