//
//  LectureList.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

class Data: CustomStringConvertible {
    var last: CGFloat = 0.0
    var offset: CGFloat = 0.0
    var prevDiff: CGFloat = 0.0
    var inited = false
    
    public var description: String { return "Data: \(last), \(offset), \(prevDiff)" }
}

struct LecturesView: View {
    @EnvironmentObject var theme: Theme
    
    @ObservedObject
    private var models: ObservableValue<LecturesState>
    @Binding private var hideTopBar: Bool
    @Binding private var hideBottomBar: Bool
    @State private var hidePages: Bool = false
    
    private let topArea: CGFloat
    private let bottomArea: CGFloat
    
    private let component: LecturesComponent
    
    private let data = Data()
    private let delta: CGFloat = 50
    private let D: CGFloat = 150
    
    
    init(
        _ component: LecturesComponent,
        _ hideTopBar: Binding<Bool>,
        _ hideBottomBar: Binding<Bool>,
        _ topArea: CGFloat,
        _ bottomArea: CGFloat
    ) {
        self.component = component
        self.models = ObservableValue(component.flow)
        self._hideTopBar = hideTopBar
        self._hideBottomBar = hideBottomBar
        self.topArea = topArea
        self.bottomArea = bottomArea
    }
    
    var body: some View {
        let model = models.value
        
        ScrollView(.vertical, showsIndicators: false) {
            VStack(alignment: .center, spacing: 1) {
                
                if !hidePages {
                    PageControlView(pagination: model.pagination, component: component)
                        .padding(.bottom, 4)
                        .transition(
                            .move(edge: .top)
                                .combined(with: .scale(scale: 0.1, anchor: .top))
                        )
                }
                
                ForEach(model.lectures) { lecture in
                    LectureListItem(lecture: lecture, component: component)
                        .environmentObject(theme)
                }
            }
            .overlay(
                GeometryReader { geo -> Color in
                    
                    let minY = geo.frame(in: .named("SCROLL")).minY
                    let maxY = geo.frame(in: .named("SCROLL")).maxY
                    
                    if !data.inited {
                        data.inited = true
                        data.offset = minY
                        data.last = minY
                    }
                    
                    let diff = minY - data.offset
                    
                    if (data.prevDiff <= 0 && diff > 0) || (data.prevDiff > 0 && diff <= 0) {
                        print("minY = \(minY)")
                        print("diff = \(diff)")
                        print(data)
                        data.last = minY
                        print("========================================= CHANGE DIRECTION ")
                    }
                    
                    data.prevDiff = diff
                    data.offset = minY
                    
                    if abs(minY - data.last) < D {
                        print("too small \(abs(minY - data.last)) < \(D)")
                        return Color.clear
                    }
                    
                    //                    print("minY \(minY) offset \(offset)")
                    DispatchQueue.main.async {
                        if diff < 0 {
                            print("up")
                            if hideBottomBar {
                                print("show bottomBar")
                                withAnimation(.easeInOut(duration: 0.7)) {
                                    hideBottomBar = false
                                }
                            }
                            
                            if (minY < topArea - delta) {
                                
                                //                                    if !hidePages {
                                //                                        print("hide pages")
                                //                                        hidePages = true
                                //                                        data.last = minY
                                //                                    } else
                                if !hideTopBar {
                                    print("hide topBar")
                                    hideTopBar = true
                                    data.last = minY
                                }
                            }
                        }
                        
                        if diff > 0 {
                            print("down")
                            
                            //                                if hidePages {
                            //                                    print("show pages")
                            //                                    hidePages = false
                            //                                } else
                            if hideTopBar {
                                print("show topBar")
                                hideTopBar = false
                            }
                            
                            if (maxY > bottomArea + delta && !hideBottomBar) {
                                print("hide bottomBar")
                                withAnimation(.easeInOut(duration: 0.7)) {
                                    hideBottomBar = true
                                }
                                data.last = minY
                            }
                        }
                    }
                    
                    return Color.clear
                }
            )
            .gesture(DragGesture()
                        .onChanged({ (value) in
                let y1 = value.startLocation.y
                let y2 = value.location.y
                
                print("dy = \(y2 - y1)")
                
            }).onEnded({ _ in
                
            }))
        }
        .coordinateSpace(name: "SCROLL")
        .padding(.horizontal, 8)
        .padding(.vertical, 0.1)
    }
}

extension Lecture: Identifiable {}

struct LecturesView_Previews: PreviewProvider {
    static var previews: some View {
        ResultsView(component: StubResultsComponent())
            .environmentObject(themes[0])
    }
}

struct StatefulPreviewWrapper<Value, Content: View>: View {
    @State var value: Value
    var content: (Binding<Value>) -> Content
    
    var body: some View {
        content($value)
    }
    
    init(_ value: Value, content: @escaping (Binding<Value>) -> Content) {
        self._value = State(wrappedValue: value)
        self.content = content
    }
}
