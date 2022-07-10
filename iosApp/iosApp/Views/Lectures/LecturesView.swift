//
//  LectureList.swift
//  iosApp
//
//  Created by Aleksey Blekot on 13.04.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct ResultsLectureListener : LectureListener {
    
    let component: LecturesComponent
    init(_ component: LecturesComponent) {
        self.component = component
    }
    
    func onPause() { component.onPause() }
    func onPlay(id: Int64) { component.onPlay(id: id) }
    func onFavorite(id: Int64, isFavorite: Bool) { component.onFavorite(id: id, isFavorite: isFavorite) }
}

class Data: CustomStringConvertible {
    var last: CGFloat = 0.0
    var offset: CGFloat = 0.0
    var prevDiff: CGFloat = 0.0
    var inited = false
    
    public var description: String { return "Data: \(last), \(offset), \(prevDiff)" }
}

class ObservablePage: ObservableObject {
    @Published var value: Double = 0.0
}

struct LecturesView: View {
    @EnvironmentObject var theme: Theme
    
    @ObservedObject
    private var models: ObservableValue<LecturesState>
    @Binding private var hideTopBar: Bool
    @Binding private var hideBottomBar: Bool
    @State private var hidePages: Bool = false
    private let page = ObservablePage()
    
    private let topArea: CGFloat
    private let bottomArea: CGFloat
    
    private let component: LecturesComponent
    
    private let data = Data()
    private let delta: CGFloat = 50
    private let D: CGFloat = 150
    
    let shoulPrint = true
    
    
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
    
    func updateCurrentPage(_ page: Int32) {
        self.page.value = Double(page)
    }
    
    var body: some View {
        let model = models.value
        
        let _ = updateCurrentPage(model.pagination.curr)
        
        ScrollView(.vertical, showsIndicators: false) {
            VStack(alignment: .center, spacing: 1) {
                
                if !hidePages {
                    if model.pagination.total > 1 {
                        PageControlSliderView(page, model.pagination, component)
                            .transition(
                                .move(edge: .top)
                                    .combined(with: .scale(scale: 0.1, anchor: .top))
                            )
                            .padding(.bottom, 4)
                    }
                }
                
                if !model.lectures.isEmpty {
                    ForEach(model.lectures) { lecture in
                        LectureItemView(lecture: lecture, listener: ResultsLectureListener(component))
                            .environmentObject(theme)
                    }
                } else {
                    ForEach(0...8, id: \.self) { _ in
                        LectureShimmerView()
                    }
                    .animation(.easeInOut(duration: 2).repeatForever())
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
                        ifprint("minY = \(minY)")
                        ifprint("diff = \(diff)")
                        //ifprint(data)
                        data.last = minY
                        ifprint("========================================= CHANGE DIRECTION ")
                    }
                    
                    data.prevDiff = diff
                    data.offset = minY
                    
                    if abs(minY - data.last) < D {
                        ifprint("too small \(abs(minY - data.last)) < \(D)")
                        return Color.clear
                    }
                    
                    //                    print("minY \(minY) offset \(offset)")
                    DispatchQueue.main.async {
                        if diff < 0 {
                            print("up")
                            if hideBottomBar {
                                ifprint("show bottomBar")
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
                                    ifprint("hide topBar")
                                    hideTopBar = true
                                    data.last = minY
                                }
                            }
                        }
                        
                        if diff > 0 {
                            ifprint("down")
                            
                            //                                if hidePages {
                            //                                    print("show pages")
                            //                                    hidePages = false
                            //                                } else
                            if hideTopBar {
                                ifprint("show topBar")
                                hideTopBar = false
                            }
                            
                            if (maxY > bottomArea + delta && !hideBottomBar) {
                                ifprint("hide bottomBar")
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
        }
        .coordinateSpace(name: "SCROLL")
        .padding(.horizontal, 8)
        .padding(.vertical, 0.1)
    }
    
    private func ifprint(_ text: String) {
        if (shoulPrint) {
            print(text)
        }
    }
}

extension Lecture: Identifiable {}

struct LecturesView_Previews: PreviewProvider {
    static var previews: some View {
        ResultsView(component: StubResultsComponent())
            .environmentObject(themes[0])
    }
}
