//
//  MarqueeText.swift
//  iosApp
//
//  Created by Aleksey Blekot on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct MarqueeText : View {
    public var text: String
    public var font: UIFont
    public var color: Color
    public var leftFade: CGFloat
    public var rightFade: CGFloat
    public var startDelay: Double
    public var alignment: Alignment
    
    @State private var animate = false
    
    public var body : some View {
        let stringWidth = text.widthOfString(usingFont: font)
        let stringHeight = text.heightOfString(usingFont: font)
        
        let animation = Animation
            .linear(duration: Double(stringWidth) / 30)
            .delay(startDelay)
            .repeatForever(autoreverses: false)
        
        return ZStack {
            GeometryReader { geo in
                if stringWidth > geo.size.width { // don't use self.animate as conditional here
                    Group {
                        Text(self.text)
                            .lineLimit(1)
                            .font(.init(font))
                            .foregroundColor(color)
                            .offset(x: self.animate ? -stringWidth - stringHeight * 2 : 0)
                            .animation(self.animate ? animation : nil, value: self.animate)
                            .onAppear {
                                Task.init {
                                    try await Task.sleep(nanoseconds: 500_000_000)
                                    self.animate = geo.size.width < stringWidth
                                }
                            }
                            .fixedSize(horizontal: true, vertical: false)
                            .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .topLeading)
                        
                        Text(self.text)
                            .lineLimit(1)
                            .font(.init(font))
                            .foregroundColor(color)
                            .offset(x: self.animate ? 0 : stringWidth + stringHeight * 2)
                            .animation(self.animate ? animation : nil, value: self.animate)
                            .onAppear {
                                Task.init {
                                    try await Task.sleep(nanoseconds: 500_000_000)
                                    self.animate = geo.size.width < stringWidth
                                }
                            }
                            .fixedSize(horizontal: true, vertical: false)
                            .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .topLeading)
                    }
                    .onChange(of: self.text, perform: {text in
                        self.animate = geo.size.width < stringWidth
                    })
                    
                    .offset(x: leftFade)
                    .mask(
                        HStack(spacing:0) {
                            Rectangle()
                                .frame(width:2)
                                .opacity(0)
                            LinearGradient(gradient: Gradient(colors: [Color.black.opacity(0), Color.black]), startPoint: /*@START_MENU_TOKEN@*/.leading/*@END_MENU_TOKEN@*/, endPoint: /*@START_MENU_TOKEN@*/.trailing/*@END_MENU_TOKEN@*/)
                                .frame(width:leftFade)
                            LinearGradient(gradient: Gradient(colors: [Color.black, Color.black]), startPoint: /*@START_MENU_TOKEN@*/.leading/*@END_MENU_TOKEN@*/, endPoint: /*@START_MENU_TOKEN@*/.trailing/*@END_MENU_TOKEN@*/)
                            LinearGradient(gradient: Gradient(colors: [Color.black, Color.black.opacity(0)]), startPoint: /*@START_MENU_TOKEN@*/.leading/*@END_MENU_TOKEN@*/, endPoint: /*@START_MENU_TOKEN@*/.trailing/*@END_MENU_TOKEN@*/)
                                .frame(width:rightFade)
                            Rectangle()
                                .frame(width:2)
                                .opacity(0)
                        })
                    .frame(width: geo.size.width + leftFade)
                    .offset(x: leftFade * -1)
                } else {
                    Text(self.text)
                        .font(.init(font))
                        .foregroundColor(color)
                        .onChange(of: self.text, perform: {text in
                            self.animate = geo.size.width < stringWidth
                        })
                        .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: alignment)
                }
            }
        }
        .frame(height: stringHeight)
        .onDisappear { self.animate = false }
        
    }
    
    public init(
        text: String,
        font: UIFont = UIFont.preferredFont(forTextStyle: .subheadline),
        color: Color,
        leftFade: CGFloat = 16,
        rightFade: CGFloat = 16,
        startDelay: Double = 1,
        alignment: Alignment? = .center) {
            self.text = text
            self.font = font
            self.color = color
            self.leftFade = leftFade
            self.rightFade = rightFade
            self.startDelay = startDelay
            self.alignment = alignment != nil ? alignment! : .topLeading
        }
}

extension String {
    
    func widthOfString(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedString.Key.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.width
    }
    
    func heightOfString(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedString.Key.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.height
    }
}

struct MarqueeText_Previews: PreviewProvider {
    static var previews: some View {
        MarqueeText(
            text: "Бхагавад-Гита. Вступление. Беседа на утренней прогулке. Лекция прочитана в Бомбее куча учеников Его Божественной Милости",
            font: UIFont.preferredFont(forTextStyle: .subheadline),
            color: themes[0].bodyTextColor,
            leftFade: 16,
            rightFade: 16,
            startDelay: 1,
            alignment: .center
        )
    }
}
