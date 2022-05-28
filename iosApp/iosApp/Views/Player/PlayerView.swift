//
//  PlayerView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct PlayerView: View {
    private let component: PlayerComponent
    
    @EnvironmentObject var theme: Theme
    
    @ObservedObject
    private var models: ObservableValue<PlayerState>
    
    init(_ component: PlayerComponent) {
        self.component = component
        self.models = ObservableValue(component.flow)
    }
    
    @State private var scrollText: Bool = false
    
    var body: some View {
        let model = models.value
        
        VStack(alignment: .center) {
            
            GeometryReader { geo in
                
                ZStack(alignment: .leading) {
                    
                    let screenWidth = geo.size.width
                    let width = screenWidth * model.progress
                    
                    Capsule()
                        .fill(Color.black.opacity(0.38))
                        .frame(width: screenWidth, height: 8)
                    
                    Capsule()
                        .fill(Color.orange)
                        .frame(width: width, height: 8)
                    
                    Circle()
                        .fill(Color.orange.opacity(0.8))
                        .position(x: width, y: 8)
                        .frame(width: 16, height: 16)
                        .gesture(DragGesture()
                                    .onChanged({ (value) in
                            let x = value.location.x - geo.frame(in: .global).minX
                            let progress = x / screenWidth
                            component.onSeekTo(timeMs: model.timeMs(progress: progress))
                        }).onEnded({ _ in
                            component.onSliderReleased()
                        }))
                }
                
            }
            .frame(height: 16, alignment: .center)
            .padding(.horizontal, 8)
            
            MarqueeText(text: model.lecture.title, color: theme.bodyTextColor)
                .padding(2)

            Text(model.lecture.displayedDescription)
                .font(theme.descriptionFont)
                .foregroundColor(theme.descriptionTextColor)
                .lineLimit(1)
                .padding(2)
            
            Text(model.displayedTime)
                .font(theme.buttonFont)
                .foregroundColor(theme.descriptionTextColor)
                .lineLimit(1)
                .padding(2)
            
            HStack(alignment: .center) {
                Spacer()
                
                button(icon: "backward.end.fill") { component.onPrev() }
                button(icon: "gobackward.10")     { component.onSeekBack() }
                
                playPauseButton(isPlaying: model.isPlaying, id: model.lecture.id, component)
                
                button(icon: "goforward.10")      { component.onSeekForward() }
                button(icon: "forward.end.fill")  { component.onNext() }
                
                Spacer()
            }
            .padding(4)
        }
        .padding(4)
        .background(theme.buttonLightBacground)
        .cornerRadius(8)
        .edgesIgnoringSafeArea(.bottom)
    }
    
    private func playPauseButton(isPlaying: Bool, id: Int64, _ component: PlayerComponent) -> some View {
        
        if isPlaying {
            return button(icon: "pause.fill", .playerControlBig) {
                component.onPause()
            }
        } else {
            return button(icon: "play.fill", .playerControlBig) {
                component.onPlay(id: id)
            }
        }
    }
    
    private func button(icon: String, _ s: Sizes = .playerControl, _ onClick: @escaping () -> Void) -> some View {
        return Button {
            onClick()
        } label: {
            Image(systemName: icon)
                .resizable(resizingMode: .tile)
                .frame(width: size(s), height: size(s))
                .fixedSize()
                .foregroundColor(theme.bodyTextColor)
        }
        .padding(4)
    }
}

struct PlayerView_Previews: PreviewProvider {
    static var previews: some View {
        PlayerView(StubPlayerComponent())
            .environmentObject(themes[0])
    }
}
