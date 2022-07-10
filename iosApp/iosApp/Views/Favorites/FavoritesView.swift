//
//  FavoritesView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 10.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada

struct FavoritesLectureListener : LectureListener {
    
    let component: FavoritesComponent
    init(_ component: FavoritesComponent) {
        self.component = component
    }
    
    func onPause() { component.onPause() }
    func onPlay(id: Int64) { component.onPlay(id: id) }
    func onFavorite(id: Int64, isFavorite: Bool) { component.onFavorite(id: id, isFavorite: isFavorite) }
}

struct FavoritesView: View {
    @EnvironmentObject var theme: Theme

    @ObservedObject
    private var models: ObservableValue<FavoritesState>
    
    @Binding private var hideTopBar: Bool
    @Binding private var hideBottomBar: Bool
    private let page = ObservablePage()

    private let topArea: CGFloat
    private let bottomArea: CGFloat

    private let component: FavoritesComponent

    private let data = Data()
    private let delta: CGFloat = 50
    private let D: CGFloat = 150

    let shoulPrint = true

    init(
        _ component: FavoritesComponent,
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
                ForEach(model.lectures) { lecture in
                    LectureItemView(lecture: lecture, listener: FavoritesLectureListener(component))
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
                        data.last = minY
                    }

                    data.prevDiff = diff
                    data.offset = minY

                    if abs(minY - data.last) < D {
                        return Color.clear
                    }

                    //                    print("minY \(minY) offset \(offset)")
                    DispatchQueue.main.async {
                        if diff < 0 {
                            if hideBottomBar {
                                withAnimation(.easeInOut(duration: 0.7)) {
                                    hideBottomBar = false
                                }
                            }

                            if (minY < topArea - delta) {
                                if !hideTopBar {
                                    hideTopBar = true
                                    data.last = minY
                                }
                            }
                        }

                        if diff > 0 {
                            if hideTopBar {
                                hideTopBar = false
                            }

                            if (maxY > bottomArea + delta && !hideBottomBar) {
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

struct FavoritesView_Previews: PreviewProvider {
    static var previews: some View {
        FavoritesFeatureView(StubFavoritesFeatureComponent())
            .environmentObject(themes[0])
    }
}
