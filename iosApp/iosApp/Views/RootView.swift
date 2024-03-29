//
//  RootView.swift
//  iosApp
//
//  Created by Aleksey Blekot on 14.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Prabhupada
import AVFoundation
import BottomSheet

struct RootView: View {
    
    @EnvironmentObject var theme: Theme
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, RootComponentChild>>
    
    private var activeChild: RootComponentChild { childStack.value.active.instance }
    
    private let component: RootComponent
    
    init(_ component: RootComponent) {
        self.component = component
        self.childStack = ObservableValue(component.childStack)
        
        NapierProxyKt.debugBuild()
    }
    
    @State var bottomSheetPosition: BottomSheetPosition = .absolute(200)
    let bottomSheetPositions: [BottomSheetPosition] = [.absolute(200), .absolute(70)]
    
    var body: some View {
        VStack(spacing: 0) {
            ChildView(child: activeChild)
                .frame(maxHeight: .infinity)
                .bottomSheet(
                    bottomSheetPosition: $bottomSheetPosition,
                    switchablePositions: bottomSheetPositions
                ) {
                    PlayerView(component.playerComponent)
                }
                .enableContentDrag(true)
                .showDragIndicator(true)
                .dragIndicatorColor(theme.colors.playerTimeLineBg)
                .customBackground(RoundedRectangle(cornerRadius: theme.dimens.radiusXL).fill(theme.colors.playerBg), alignment: .center)
                .enableSwipeToDismiss(false)
                .enableTapToDismiss(false)
                .customThreshold(0.11)
//                .cornerRadius(theme.dimens.radiusXL)
//                .cornerRadius(theme.dimens.radiusXL, corners: [.topLeft, .topRight])
                
            
            //            if !hideBottomBar {
            //                PlayerView(component.playerComponent)
            //                    .environmentObject(theme)
            
            //            }
            
            HStack(alignment: .bottom, spacing: theme.dimens.paddingS) {
                navItem("hearingdevice.ear", "Лекции", activeChild is RootComponentChildResults) { component.onResultsTabClicked() }
                .padding(.top, theme.dimens.paddingS)
                navItem("line.3.horizontal.decrease.circle", "Фильтры", activeChild is RootComponentChildFilters) { component.onFiltersTabClicked() }
                navItem("magnifyingglass", "Поиск", activeChild is RootComponentChildSettings) {}
                navItem("gearshape", "Настройки", activeChild is RootComponentChildSettings) {}
            }
            .background(theme.colors.navBarBg)
        }
        
    }
        

    
    private func navItem(_ icon: String, _ text: String, _ selected: Bool, _ onClick: @escaping () -> Void) -> some View {
        return Button(action: onClick) {
            Label(text, systemImage: icon)
                .labelStyle(VerticalLabelStyle(theme, selected))
                .frame(maxWidth: .infinity)
        }
    }
    
    private struct VerticalLabelStyle: LabelStyle {
        let theme: Theme
        let selected: Bool
        
        init(_ theme: Theme, _ selected: Bool) {
            self.theme = theme
            self.selected = selected
        }
        
        func makeBody(configuration: Configuration) -> some View {
            VStack(alignment: .center, spacing: theme.dimens.paddingXS) {
                
                ZStack {
                    Capsule()
                        .fill(selected ? theme.colors.navBarIconSelectedBg : Color.clear)
                        .frame(width: theme.dimens.iconSizeL*2, height: theme.dimens.iconSizeL)
                    
                    configuration.icon
                        .frame(width: theme.dimens.iconSizeL, height: theme.dimens.iconSizeL)
                        .fixedSize()
                        .scaleEffect(1.5)
                        .foregroundColor(selected ? theme.colors.navBarIconSelected : theme.colors.navBarIcon)
                }
                
                configuration.title
                    .font(theme.labelMedium)
                    .foregroundColor(selected ? theme.colors.navBarIconSelectedText : theme.colors.navBarText)
            }
        }
    }
}

private struct ChildView: View {
    let child: RootComponentChild
    
    var body: some View {
        switch child {
        case let results as RootComponentChildResults:
            ResultsView(results.component)
            
        case let filters as RootComponentChildFilters:
            FiltersView(filters.component)
            
            //        case let downloads as RootComponentChildDownloads:
            //            FavoritesFeatureView(favorites.component)
            
        case let favorites as RootComponentChildFavorites:
            FavoritesView(favorites.component)
            
        default: EmptyView() // fatalError("Unexpected router state \(child)")
        }
    }
}

private struct IconLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.icon
    }
}

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView(StubRootComponent())
            .environmentObject(themes[0])
    }
}
